package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.items.CreepsItemHandler;
import com.morecreepsrevival.morecreeps.common.items.LootTables;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import java.util.List;
import java.util.UUID;

public class EntityInvisibleMan extends EntityCreepBase implements IMob {
	private static final DataParameter<Boolean> anger = EntityDataManager.<Boolean>createKey(EntityInvisibleMan.class, DataSerializers.BOOLEAN);
	
    private int angerLevel;
    private boolean hasAngryTexture = false;
    private UUID angerTargetUUID;

    public EntityInvisibleMan(World world) {
        super(world);
        setCreepName("Invisible Man");
        creatureType = EnumCreatureType.MONSTER;
        baseHealth = (float)rand.nextInt(40) + 40.0f;
        baseSpeed = 0.3d;
        setSize(0.8f, 1.8f);
        dataManager.set(anger, false);
        this.angerLevel = 0;
        super.setTexture("textures/entity/invisibleman.png");
        setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.STICK));
        updateAttributes();

    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(anger, false);
    }

    @Override
    protected void initEntityAI()
    {
        clearAITasks();
        NodeProcessor nodeProcessor = getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        nodeProcessor.setCanEnterDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackMelee(this, 1.0d, true));
        tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.5d));
        tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0d));
        tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(5, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));

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

    @Override
    public void setRevengeTarget(EntityLivingBase livingBase)
    {
        super.setRevengeTarget(livingBase);

        if (livingBase != null)
        {
            this.angerTargetUUID = livingBase.getUniqueID();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();
        if(entity != null)
        {
            if (entity instanceof EntityPlayer)
            {
                List list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(32D, 32D, 32D));
                for (int j = 0; j < list.size(); j++)
                {
                    Entity entity1 = (Entity)list.get(j);

                    if (entity1 instanceof EntityInvisibleMan)
                    {
                        EntityInvisibleMan entityinvisibleman = (EntityInvisibleMan)entity1;
                        entityinvisibleman.becomeAngryAt(entity);
                    }
                }
                becomeAngryAt(entity);
            }
        }
        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    private void becomeAngryAt(Entity entity) {
        this.setAttackTarget((EntityLivingBase)entity);
        angerLevel += 40 + rand.nextInt(40);
        dataManager.set(anger, true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        boolean serverSaysWeAreAngry = dataManager.get(anger);
        if(hasAngryTexture && !serverSaysWeAreAngry) {
            this.setTexture("textures/entity/invisibleman.png");
            hasAngryTexture = false;
        }
        if(!hasAngryTexture && serverSaysWeAreAngry) {
        	this.setTexture("textures/entity/invisiblemanmad.png");
        	hasAngryTexture = true;
        }
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if(isAngry()) {
            --angerLevel;
            //If just got calmed down, tell the client he isn't angry anymore.
            if(!isAngry()) dataManager.set(anger, false);
        }
        else{
        	this.setAttackTarget(null);
        }
    }

    public boolean isAngry()
    {
        return angerLevel > 0;
    }
    
    @Override
    public int getRevengeTimer()
    {
    	return angerLevel;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return this.angerLevel == 0 ? CreepsSoundHandler.invisibleManSound : CreepsSoundHandler.invisibleManAngry;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return CreepsSoundHandler.invisibleManHurt;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return CreepsSoundHandler.invisibleManDeath;
    }

    protected ResourceLocation getLootTable() {
        return LootTables.invisibleman;
    }
}
