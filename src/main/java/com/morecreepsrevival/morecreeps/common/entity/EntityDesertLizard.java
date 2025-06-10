package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.items.LootTables;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityDesertLizard extends EntityCreepBase implements IMob {
    private static final String[] textures = new String[]{"textures/entity/desertlizard1", "textures/entity/desertlizard2", "textures/entity/desertlizard3", "textures/entity/desertlizard4", "textures/entity/desertlizard5"};

    public EntityDesertLizard(World worldin) {
        super(worldin);
        this.setCreepTypeName("Desert Lizard");
        this.creatureType = EnumCreatureType.MONSTER;
        this.baseHealth = 15.0F;
        this.setSize(1.75F, 0.75F);
        this.baseSpeed = 0.25D;
        this.baseAttackDamage = 2.0D;
        this.experienceValue = 10;
        this.updateAttributes();
    }

    @Override
    public void initEntityAI()
    {
        clearAITasks();
        NodeProcessor nodeProcessor = getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackMelee(this, 1.0d, true));
        tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.5d));
        tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0d));
        tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(5, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected boolean isDaylightMob() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.world.canSeeSky(new BlockPos(this));
    }

    @Override
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos)
    {
        Block block = world.getBlockState(blockPos).getBlock();
        if (block == Blocks.SAND || block == Blocks.GRAVEL)
        {
            return 10.0f;
        }
        return super.getBlockPathWeight(blockPos);
    }

    protected boolean isValidLightLevel() {
        return this.world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) <= 15;
    }

    public float getEyeHeight() {
        return 0.1F;
    }

    protected ResourceLocation getLootTable() {
        return LootTables.desertlizard;
    }

    protected String[] getAvailableTextures() {
        return textures;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CreepsSoundHandler.desertLizardHurt;
    }

    protected SoundEvent getDeathSound() {
        return CreepsSoundHandler.desertLizardDeath;
    }

    protected SoundEvent getAmbientSound() {
        return CreepsSoundHandler.desertLizard;
    }

    public float maxShrink() {
        return 0.4F;
    }

    public float getShrinkRayAmount() {
        return 0.2F;
    }

    public void onShrink(EntityShrink source) {
    }

    public float maxGrowth() {
        return 4.0F;
    }

    public float getGrowRayAmount() {
        return 0.2F;
    }

    public void onGrow(EntityGrow source) {
    }
}

