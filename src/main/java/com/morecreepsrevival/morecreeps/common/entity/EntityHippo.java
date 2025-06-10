package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.items.LootTables;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityHippo extends EntityCreepBase {
    private static final String[] textures = new String[]{"textures/entity/hippo2"};

    public EntityHippo(World worldin) {
        super(worldin);
        this.setCreepTypeName("Hippo");
        this.setSize(2.5F, 2.0F);
        this.setModelSize(2.0F);
        this.baseHealth = 35.0F;
        this.baseSpeed = 0.2D;
        this.baseAttackDamage = 2.0D;
        this.updateAttributes();
    }

    protected void func_184651_r() {
        this.clearAITasks();
        NodeProcessor nodeProcessor = this.getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        nodeProcessor.setCanEnterDoors(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityHippo.GoToWaterGoal(this, 1.0D));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    protected String[] getAvailableTextures() {
        return textures;
    }

    protected ResourceLocation getLootTable() {
        return LootTables.hippo;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CreepsSoundHandler.hippoHurtSound;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CreepsSoundHandler.hippoDeathSound;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return CreepsSoundHandler.hippoAmbientSound;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.45F, 1.0F);
    }

    public int getMaxSpawnedInChunk() {
        return 2;
    }

    public float maxShrink() {
        return 0.3F;
    }

    public float getShrinkRayAmount() {
        return 0.25F;
    }

    public void onShrink(EntityShrink source) {
    }

    public float maxGrowth() {
        return 3.0F;
    }

    public float getGrowRayAmount() {
        return 0.25F;
    }

    public void onGrow(EntityGrow source) {
        this.increaseMoveSpeed(0.15F);
    }

    public static class GoToWaterGoal extends EntityAIMoveToBlock {
        private final EntityHippo hippo;
        private boolean isAboveDestination2;
        private int timeoutCounter2;
        private double speed;

        public GoToWaterGoal(EntityHippo hippo, double speed) {
            super(hippo, speed, 24);
            this.hippo = hippo;
            this.speed = speed;
        }

        public boolean shouldContinueExecuting() {
            return !this.hippo.isInWater() && this.timeoutCounter2 <= 1200 && this.shouldMoveTo(this.hippo.world, this.destinationBlock);
        }

        public boolean shouldExecute() {
            if (this.hippo.isChild() && !this.hippo.isInWater()) {
                return this.shouldExecute2();
            } else {
                return !this.hippo.isInWater() && this.shouldExecute2();
            }
        }

        public boolean shouldExecute2() {
            if (this.runDelay > 0) {
                --this.runDelay;
                return false;
            } else {
                this.runDelay = 200 + this.hippo.getRNG().nextInt(200);
                return this.searchForDestination2();
            }
        }

        public void startExecuting() {
            super.startExecuting();
            this.timeoutCounter2 = 0;
        }

        private boolean searchForDestination2() {
            int i = 24;
            BlockPos blockpos = new BlockPos(this.hippo);

            for(int k = -1; k <= 1; k = k > 0 ? -k : 1 - k) {
                for(int l = 0; l < i; ++l) {
                    for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                        for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                            BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);
                            if (this.hippo.isWithinHomeDistanceFromPosition(blockpos1) && this.shouldMoveTo(this.hippo.world, blockpos1)) {
                                this.destinationBlock = blockpos1;
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }

        public void updateTask() {
            if (this.hippo.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D) {
                this.isAboveDestination2 = false;
                ++this.timeoutCounter2;
                if (this.timeoutCounter2 % 160 == 0) {
                    this.hippo.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.speed);
                }
            } else {
                this.isAboveDestination2 = true;
                --this.timeoutCounter2;
            }

        }

        protected boolean getIsAboveDestination() {
            return this.isAboveDestination2;
        }

        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            Block block = worldIn.getBlockState(pos).getBlock();
            return block == Blocks.WATER;
        }
    }
}
