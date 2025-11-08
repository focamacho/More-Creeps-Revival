package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.entity.ai.EntityAIThief;
import com.morecreepsrevival.morecreeps.common.entity.ai.EntityAIThiefAvoidPlayer;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import java.util.Arrays;

public class EntityThief extends EntityCreepBase implements IMob, IEntityCanChangeSize {

    private Entity closestEntity;

    public EntityThief(World worldIn) {
        super(worldIn);
        creatureType = EnumCreatureType.MONSTER;
        baseHealth = (float) rand.nextInt(20) + 10.0f;
        baseSpeed = 0.35d;
        Arrays.fill(inventoryHandsDropChances, 1F);
        updateAttributes();
    }

    @Override
    public boolean canDespawn() {
        return !getStolen();
    }

    @Override
    protected void updateTexture() {
        setTexture("textures/entity/thief.png");
    }

    @Override
    protected void initEntityAI() {
        clearAITasks();

        NodeProcessor nodeProcessor = getNavigator().getNodeProcessor();
        nodeProcessor.setCanSwim(true);
        nodeProcessor.setCanEnterDoors(true);

        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIThiefAvoidPlayer(this, 25.0F, 1.0D, 1.2D));
        tasks.addTask(2, new EntityAIBreakDoor(this));
        tasks.addTask(3, new EntityAIThief(this));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.5d));
        tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0d));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        EntityPlayer closestPlayer = world.getClosestPlayerToEntity(this, 25.0d);
        if (closestPlayer != null && !getStolen()) {
            findPlayerToAttack();
        } else {
            setAttackTarget(null);
        }

        if (getStolen()) {
            setAttackTarget(null);
            closestEntity = closestPlayer;
        } else {
            closestEntity = null;
            EntityLivingBase target = getAttackTarget();

            if (target instanceof EntityPlayer && getDistanceSq(target) < 16.0d && canEntityBeSeen(target) && getHealth() > 0) {
                EntityPlayer player = (EntityPlayer) target;

                ItemStack itemStack = null;

                for (ItemStack itemStack1 : player.inventory.mainInventory) {
                    if (!itemStack1.isEmpty()) {
                        itemStack = itemStack1;

                        if (rand.nextInt(4) == 0) {
                            break;
                        }
                    }
                }

                if (itemStack == null) {
                    setAttackTarget(null);
                } else {
                    playSound(SoundEvents.BLOCK_LAVA_POP, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 6.2f + 1.0f);

                    int count = itemStack.getCount();

                    int stolenAmount = rand.nextInt(count) + 1;

                    if (stolenAmount > count) {
                        stolenAmount = count;
                    }

                    if (!world.isRemote) {
                        ItemStack copy = itemStack.copy();

                        copy.setCount(stolenAmount);

                        setHeldItem(EnumHand.MAIN_HAND, copy);
                        itemStack.shrink(stolenAmount);
                    }

                    playSound(CreepsSoundHandler.thiefStealSound, getSoundVolume(), getSoundPitch());

                    setAttackTarget(null);

                    for (int i = 0; i < 10; i++) {
                        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (posX + (double) (rand.nextFloat() * width * 2.0f)) - (double) width, posY + (double) (rand.nextFloat() * height), (posZ + (double) (rand.nextFloat() * width * 2.0f)) - (double) width, rand.nextGaussian() * 0.02d, rand.nextGaussian() * 0.02d, rand.nextGaussian() * 0.02d);
                    }
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return CreepsSoundHandler.thiefSound;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CreepsSoundHandler.thiefHurtSound;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CreepsSoundHandler.thiefDeathSound;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public boolean getStolen() {
        return !this.getHeldItemMainhand().isEmpty();
    }

    public void findPlayerToAttack() {
        if (getStolen() || getAttackTarget() != null) {
            return;
        }

        EntityPlayer player = world.getNearestPlayerNotCreative(this, 16.0d);

        if (player != null) {
            for (ItemStack itemStack : player.inventory.mainInventory) {
                if (!itemStack.isEmpty()) {
                    if (rand.nextInt(2) == 0) {
                        playSound(CreepsSoundHandler.thiefFindPlayerSound, getSoundVolume(), getSoundPitch());
                    }

                    setAttackTarget(player);

                    return;
                }
            }
        }
    }

    @Override
    public float maxShrink() {
        return 0.4f;
    }

    @Override
    public float getRayAmount() {
        return 0.2f;
    }

    @Override
    public float maxGrowth() {
        return 4.0f;
    }

    public Entity getClosestEntity() {
        return closestEntity;
    }
}
