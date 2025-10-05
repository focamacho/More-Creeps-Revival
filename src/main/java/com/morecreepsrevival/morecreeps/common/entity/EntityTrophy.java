package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.helpers.CreepsUtil;
import com.morecreepsrevival.morecreeps.common.items.CreepsItemHandler;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityTrophy extends EntityCreepBase {
    private static final int defaultLifetime = 75;
    private static final DataParameter<Integer> partyTime = EntityDataManager.createKey(EntityTrophy.class, DataSerializers.VARINT);

    private static final DataParameter<Integer> trophyLifespan = EntityDataManager.createKey(EntityTrophy.class, DataSerializers.VARINT);

    private static final DataParameter<Float> rotationDegrees = EntityDataManager.createKey(EntityTrophy.class, DataSerializers.FLOAT);

    public EntityTrophy(World world) {
        super(world);


        setModelSize(1.5f);

        experienceValue = 0;

        baseHealth = 1f;

        baseSpeed = 0.0d;

        setEntityInvulnerable(true);

        setSize(1.0f, 5f);

        updateAttributes();
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        dataManager.register(partyTime, rand.nextInt(30) + 40);

        dataManager.register(trophyLifespan, defaultLifetime);

        dataManager.register(rotationDegrees, 0f);
    }

    @Override
    protected void initEntityAI() {
        clearAITasks();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CreepsSoundHandler.trophySmashSound;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void updateTexture() {
        setTexture("textures/entity/trophy.png");
    }

    @Override
    public void onLivingUpdate() {
        //super.onLivingUpdate();
        float rotationCurr = dataManager.get(rotationDegrees);
        if (rotationCurr < 360f) dataManager.set(rotationDegrees, rotationCurr + 9f);
        else dataManager.set(rotationDegrees, rotationCurr - 351f);

        this.setRotationYawHead(rotationCurr);
        this.setRenderYawOffset(rotationCurr);
        //this.world.updateEntityWithOptionalForce(this, false);

        this.setRotation(rotationCurr, 0f);

        this.motionX = 0f;
        this.motionZ = 0f;
        if (this.motionY > 0f) this.motionY = 0f;

        if (world.isRemote) return;

        if (dataManager.get(partyTime) > 1) {
            dataManager.set(partyTime, dataManager.get(partyTime) - 1);

            spawnConfetti();
        }

        if (dataManager.get(trophyLifespan) > 0) {
            dataManager.set(trophyLifespan, dataManager.get(trophyLifespan) - 1);

            if (dataManager.get(trophyLifespan) < 1) {
                setDead();
                int amt = rand.nextInt(25) + 10;

                for (int i = 0; i < amt; i++) {
                    dropItem(CreepsItemHandler.money, 1);
                }
            }
        }
    }

    @Override
    protected void dropItemsOnDeath() {
        int amt = rand.nextInt(25) + 10;

        for (int i = 0; i < amt; i++) {
            dropItem(CreepsItemHandler.money, 1);
        }
    }

    private void spawnConfetti() {
        // TODO: confetti

        for (int i = 0; i < 20; ++i) {
            double factor1 = CreepsUtil.GetRandomUnitMinus(rand);
            double factor2 = CreepsUtil.GetRandomUnitMinus(rand);

            Vec2f movDir = new Vec2f((float) factor1, (float) factor2);

            Item selecteditem = Item.REGISTRY.getRandomObject(rand);

            Particle particle = CreepsUtil.SpawnEatingParticle(
                    world,
                    posX, posY + 1, posZ,
                    movDir.x * 0.11F, 0.6D, movDir.y * 0.11F,
                    1.5f, selecteditem
            );

            if (particle == null) break;

            particle.setMaxAge(80);
        }
    }

    public void positionCorrectlyAround(EntityPlayer player) {
        World world = player.world;

        Vec3d plook = player.getLook(1.0f);

        int result = -1;
        int ychange = 0;

        for (int i = 1; i <= 10; ++i) {
            double xv = player.posX + plook.x * i;
            double yv = 0.01 + ((int) player.posY);
            double zv = player.posZ + plook.z * i;
            ;
            BlockPos p0 = new BlockPos(xv, yv + 1d, zv);
            BlockPos p1 = new BlockPos(xv, yv - 1d, zv);
            BlockPos p2 = new BlockPos(xv, yv - 2d, zv);
            BlockPos p3 = new BlockPos(xv, yv + 2d, zv);

            if (
                    !world.isBlockLoaded(p0) || !world.isBlockLoaded(p1) ||
                            !world.isBlockLoaded(p2) || !world.isBlockLoaded(p3)
            ) break;

            if (!world.isAirBlock(p0)) break;

            if (world.isAirBlock(new BlockPos(xv, yv, zv))) {
                if (world.isAirBlock(p1)) {
                    if (world.isAirBlock(p2)) break;

                    ychange = -1;
                    result = i;
                    continue;
                } else {
                    ychange = 0;
                    result = i;
                    continue;
                }
            } else {
                if (world.isAirBlock(p3)) {
                    ychange = 1;
                    result = i;

                    continue;
                }
            }

            break;
        }

        if (result == -1) this.setPosition(player.posX, player.posY, player.posZ);
        else {
            this.setPosition(player.posX + plook.x * result,
                    0.001 + ((int) player.posY) + ychange,
                    player.posZ + plook.z * result);
        }
    }
}
