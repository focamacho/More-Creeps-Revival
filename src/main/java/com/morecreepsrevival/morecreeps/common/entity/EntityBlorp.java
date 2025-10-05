package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.items.CreepsItemHandler;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityBlorp extends EntityCreepBaseOwnable implements IEntityCanChangeSize {

    private static final DataParameter<Boolean> hungry = EntityDataManager.<Boolean>createKey(EntityBlorp.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> hungryTime = EntityDataManager.createKey(EntityBlorp.class, DataSerializers.VARINT);

    private BlockPos targetTree = null;

    public EntityBlorp(World worldIn) {
        super(worldIn);

        setModelSize(1.0f);
        setSize(1.0f, 1.5f);

        baseAttackDamage = 2.0d;
        baseHealth = 35.0f;
        baseSpeed = 0.25d;

        updateAttributes();
    }

    @Override
    protected void updateTexture() {
        setTexture("textures/entity/blorp.png");
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(hungry, false);
        dataManager.register(hungryTime, rand.nextInt(20) + 20);
    }

    @Override
    public void onLivingUpdate() {
        if (getModelSize() > 2.0f) {
            ignoreFrustumCheck = true;
        }

       super.onLivingUpdate();

        if (getAttackTarget() != null) {
            setHungry(false);
            targetTree = null;
            setHungryTime(100);
        }

        if (getHungry()) {
            BlockPos blockPos;
            if(targetTree != null) blockPos = targetTree;
            else blockPos = findTree(2.0d);

            if (blockPos != null) {
                playSound(CreepsSoundHandler.blorpEatSound, getSoundVolume(), getSoundPitch());
                world.setBlockToAir(blockPos);
                setHungryTime(getHungryTime() + rand.nextInt(100) + 25);

                if (getHungryTime() > 1000) {
                    setHungry(false);

                    if (getModelSize() < maxGrowth()) {
                        growModelSize(0.15f, maxGrowth());

                        float growthDifference = 100 - (currentSize * 100f / getModelSize());
                        growHitboxSize(currentSize / 100f * growthDifference);
                    }

                    setLevel(getLevel() + 1);
                    updateAttributes();
                    addHealth(getLevelHealthMultiplier());

                    playSound(CreepsSoundHandler.blorpGrowSound, getSoundVolume(), getSoundPitch());
                }

                faceTreeTop();
                navigator.tryMoveToXYZ(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0);
            } else {
                setHungryTime(100);
            }
        } else {
            setHungryTime(getHungryTime() - 1);

            if (getHungryTime() < 1) {
                setHungry(true);

                setHungryTime(1);
            }
        }
    }

    public boolean getHungry() {
        return ((Boolean) dataManager.get(hungry)).booleanValue();
    }

    private void setHungry(boolean b) {
        dataManager.set(hungry, Boolean.valueOf(b));
    }

    public int getHungryTime() {
        return dataManager.get(hungryTime);
    }

    private void setHungryTime(int i) {
        dataManager.set(hungryTime, i);
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    protected void dropItemsOnDeath() {
        dropItem(CreepsItemHandler.blorpCola, getLevel());
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 3;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return CreepsSoundHandler.blorpSound;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CreepsSoundHandler.blorpHurtSound;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CreepsSoundHandler.blorpDeathSound;
    }

    @Override
    public void initEntityAI() {
        clearAITasks();

        NodeProcessor nodeProcessor = getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        nodeProcessor.setCanEnterDoors(true);

        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIBreakDoor(this));
        tasks.addTask(3, new EntityAIAttackMelee(this, 1.0d, true));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.5d));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0d));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityBlorp.class, true));
    }

    private BlockPos findTree(double d1) {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox().grow(d1, d1, d1);

        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.floor(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.floor(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.floor(axisalignedbb.maxZ + 1.0D);

        for (int k1 = i; k1 < j; k1++) {
            for (int l1 = k; l1 < l; l1++) {
                for (int i2 = i1; i2 < j1; i2++) {
                    BlockPos blockPos = new BlockPos(k1, l1, i2);

                    Block block = world.getBlockState(blockPos).getBlock();

                    if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {
                        return blockPos;
                    } else if (getLevel() > 3 && (block == Blocks.LOG || block == Blocks.LOG2)) {
                        return blockPos;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos) {
        Block block = world.getBlockState(blockPos).getBlock();

        if (block == Blocks.LEAVES || block == Blocks.LEAVES2 || block == Blocks.LOG || block == Blocks.LOG2) {
            return 10.0f;
        }

        return super.getBlockPathWeight(blockPos);
    }

    private void faceTreeTop() {
        rotationYaw = (float) (Math.atan2(motionX, motionZ) / Math.PI);
    }

    @Override
    protected float getLevelHealthMultiplier() {
        return 10.0f;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource damageSource, float amt) {
        boolean flag = super.attackEntityFrom(damageSource, amt);

        if (flag) {
            setHungry(false);

            setHungryTime(100);
        }

        return flag;
    }

    @Override
    protected void doAttackJump(Entity entity) {
        playSound(CreepsSoundHandler.blorpBounceSound, getSoundVolume(), getSoundPitch());

        double d = entity.posX - posX;
        double d2 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt(d * d + d2 * d2);

        motionX = (d / (double) f1) * 0.20000000000000001D * 0.80000001192092896D + motionX * 0.20000000298023224D;
        motionZ = (d2 / (double) f1) * 0.20000000000000001D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
        motionY = 0.70000000596246448D + (double) getModelSize() * 0.050000004559D;

        fallDistance = -25.0f + (getModelSize() * -5.0f);
    }

    @Override
    protected boolean shouldJumpWhileAttacking(Entity entity) {
        return true;
    }

    @Override
    public float maxShrink() {
        return 0.3f;
    }

    @Override
    public float getRayAmount() {
        return 0.25f;
    }

    @Override
    public float maxGrowth() {
        return 5.0f;
    }

}
