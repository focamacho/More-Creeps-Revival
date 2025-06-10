package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.items.LootTables;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMummy extends EntityCreepBase implements IMob
{
    public EntityMummy(World world)
    {
        super(world);
        setCreepTypeName("Mummy");
        creatureType = EnumCreatureType.MONSTER;
        spawnOnlyAtNight = true;
        baseHealth = (float)rand.nextInt(10) + 20.0f;
        baseSpeed = 0.25d;
        baseAttackDamage = 2.0d;
        updateAttributes();
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
        tasks.addTask(3, new EntityAIAttackMelee(this, 1.0d, true));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.5d));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0d));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    //@Override
    //public boolean getCanSpawnHere() {
    //    return super.getCanSpawnHere() && !this.world.canSeeSky(new BlockPos(this)) && (this.posY < 50.0D);
    //}

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.world.canSeeSky(new BlockPos(this));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return CreepsSoundHandler.mummyHurtSound;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return CreepsSoundHandler.mummyDeathSound;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return CreepsSoundHandler.mummySound;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.35F, 1.0F);
    }

    @Override
    protected boolean shouldBurnInDay()
    {
        return true;
    }

    protected boolean isDaylightMob() {
        return false;
    }

    protected ResourceLocation getLootTable() {
        return LootTables.mummy;
    }

    @Override
    protected void updateTexture()
    {
        setTexture("textures/entity/mummy.png");
    }

    }

