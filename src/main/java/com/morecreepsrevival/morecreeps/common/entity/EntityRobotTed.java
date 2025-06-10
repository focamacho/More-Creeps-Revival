package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.MoreCreepsAndWeirdos;
import com.morecreepsrevival.morecreeps.common.items.CreepsItemHandler;
import com.morecreepsrevival.morecreeps.common.items.LootTables;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class EntityRobotTed extends EntityCreepBase implements IMob
{
    public int floattimer;

    public EntityRobotTed(World worldIn)
    {
        super(worldIn);
        setCreepTypeName("Robot Ted");
        creatureType = EnumCreatureType.MONSTER;
        setModelSize(2.5f);
        setSize(2.5f * 0.75f, 2.5f * 1.85f);
        baseHealth = (float)rand.nextInt(20) + 25.0f;
        baseSpeed = 0.25d;
        floattimer = 0;
        updateAttributes();
    }

    @Override
    protected void updateTexture()
    {
        setTexture("textures/entity/robotted.png");
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
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityRobotTodd.class, 8.0f));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityRobotTodd.class, true));
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

    protected ResourceLocation getLootTable() {
        return LootTables.robotted;
    }

    protected boolean isValidLightLevel() {
        return this.world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) <= 15;
    }

    @Override
    public float getEyeHeight()
    {
        return 2.0f;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public boolean canBleed() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        int x = (int) this.posX;
        int y = (int) this.posY;
        int z = (int) this.posZ;
        Entity entity = this;
        {
            Map<String, Object> $_dependencies = new HashMap<>();
            $_dependencies.put("entity", entity);
        }
        if (source == DamageSource.FALL)
            return false;
        return super.attackEntityFrom(source, amount);
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 0.0F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return CreepsSoundHandler.tedInsultSound;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return CreepsSoundHandler.robotHurtSound;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return CreepsSoundHandler.tedDeadSound;
    }
}
