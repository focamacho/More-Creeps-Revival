package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.items.LootTables;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityRockMonster extends EntityCreepBase implements IMob {

    public EntityRockMonster(World worldIn)
    {
        super(worldIn);
        setCreepTypeName("Rock Monster");
        creatureType = EnumCreatureType.MONSTER;
        setSize(2f, 2f);
        getEntityBoundingBox().offset(0d, 0d, 2d);
        baseSpeed = 0.20d;
        baseHealth = 60f;
        experienceValue = 10;
        updateAttributes();
    }

    @Override
    protected void updateTexture()
    {
        setTexture("textures/entity/rockmonster.png");
    }

    @Override
    protected void initEntityAI()
    {
        clearAITasks();
        NodeProcessor nodeProcessor = getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        nodeProcessor.setCanEnterDoors(false);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIBreakDoor(this));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.45d));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0d));
        tasks.addTask(5, new EntityAIAttackMelee(this, 3.0d, false));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
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
        return 1;
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos)
    {
        Block block = world.getBlockState(blockPos).getBlock();
        if (block == Blocks.SAND || block == Blocks.GRAVEL || block == Blocks.GRASS)
        {
            return 10.0f;
        }
        return super.getBlockPathWeight(blockPos);
    }

    protected boolean isValidLightLevel() {
        return this.world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) <= 15;
    }

    protected void attackEntity(Entity entity, float f)
    {
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt(d * d + d1 * d1);
        motionX = (d / (double)f1) * 0.5D * 0.30000000192092896D + motionX * 0.38000000098023223D;
        motionZ = (d1 / (double)f1) * 0.5D * 0.17000000192092896D + motionZ * 0.38000000098023223D;
    }

    protected ResourceLocation getLootTable() {
        return LootTables.rockmonster;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.BLOCK_GRAVEL_BREAK, 0.45F, 1.5F);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return CreepsSoundHandler.rockMonster;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return CreepsSoundHandler.rockMonsterHurt;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return CreepsSoundHandler.rockMonsterDeath;
    }
}
