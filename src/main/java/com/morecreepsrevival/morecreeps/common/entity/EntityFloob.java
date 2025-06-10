package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.items.CreepsItemHandler;
import com.morecreepsrevival.morecreeps.common.items.LootTables;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityFloob extends EntityCreepBase implements IRangedAttackMob, IMob
{
    public EntityFloob(World worldIn)
    {
        super(worldIn);
        setCreepTypeName("Floob");
        creatureType = EnumCreatureType.MONSTER;
        baseHealth = (float)rand.nextInt(15) + 10.0f;
        baseSpeed = 0.3d;
        setHeldItem(EnumHand.MAIN_HAND, new ItemStack(CreepsItemHandler.floobRaygun));
        updateAttributes();
    }

    @Override
    protected void updateTexture()
    {
        setTexture("textures/entity/floob.png");
    }

    @Override
    protected void initEntityAI()
    {
        clearAITasks();
        NodeProcessor nodeProcessor = getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        nodeProcessor.setCanEnterDoors(true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIBreakDoor(this));
        tasks.addTask(3, new EntityAIAttackRanged(this, 1.0d, 25, 75, 50.0f));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.5d));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0d));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(6, new EntityAILookIdle(this));
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
        return 1;
    }

    //@Override
    //public float getBlockPathWeight(BlockPos blockPos)
    //{
    //    Block block = world.getBlockState(blockPos).getBlock();
    //    if (block == Blocks.SAND || block == Blocks.GRAVEL || block == Blocks.GRASS)
    //    {
    //        return 10.0f;
    //    }
    //    return super.getBlockPathWeight(blockPos);
    //}

    protected boolean isValidLightLevel() {
        return this.world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) <= 15;
    }

    protected ResourceLocation getLootTable() {
        return LootTables.floob;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return CreepsSoundHandler.floobSound;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return CreepsSoundHandler.floobHurtSound;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return CreepsSoundHandler.floobDeathSound;
    }

    @Override
    protected void dropItemsOnDeath()
    {
        if (rand.nextInt(6) == 0)
        {
            dropItem(CreepsItemHandler.raygun, 1);
        }
    }

    @Override
    public void attackEntityWithRangedAttack(@Nonnull EntityLivingBase target, float distanceFactor)
    {
        EntityRay ray = new EntityRay(world, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - ray.posY;
        double d2 = target.posZ - this.posZ;
        ray.shoot(d0, d1, d2, 1.6F, (float)(14 - world.getDifficulty().getDifficultyId() * 4));
        world.spawnEntity(ray);
        playSound(CreepsSoundHandler.raygunSound, getSoundVolume(), getSoundPitch());
    }

    @Override
    public void setSwingingArms(boolean swingingArms)
    {
    }
}
