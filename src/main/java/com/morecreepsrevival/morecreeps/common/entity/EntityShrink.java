    package com.morecreepsrevival.morecreeps.common.entity;
    
    import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
    import net.minecraft.block.state.BlockStateBase;
    import net.minecraft.block.state.IBlockState;
    import net.minecraft.entity.Entity;
    import net.minecraft.entity.EntityLiving;
    import net.minecraft.entity.EntityLivingBase;
    import net.minecraft.entity.player.EntityPlayer;
    import net.minecraft.entity.projectile.EntityThrowable;
    import net.minecraft.init.Blocks;
    import net.minecraft.util.EnumParticleTypes;
    import net.minecraft.util.math.*;
    import net.minecraft.world.World;
    import net.minecraft.world.WorldServer;
    
    import javax.annotation.Nonnull;
    
    public class EntityShrink extends EntityThrowable {
    
        public EntityLivingBase lightValueOwn;
        protected int hitX;
        protected int hitY;
        protected int hitZ;
        protected boolean aoLightValueZPos;
        protected int aoLightValueScratchXYZNNP;
        protected int aoLightValueScratchXYNN;
        protected boolean playerFire;
        protected float shrinkSize;
        protected int vibrate;
        protected IBlockState blockHit;
    
        public EntityShrink(World world) {
            super(world);
    
            setSize(0.0325f, 0.01125f);
    
            hitX = -1;
            hitY = -1;
            hitZ = -1;
    
            aoLightValueZPos = false;
            aoLightValueScratchXYNN = 0;
            playerFire = false;
            shrinkSize = 1.0f;
            vibrate = 1;
        }
    
        public EntityShrink(World world, EntityLivingBase entity) {
            this(world);
    
            setLocationAndAngles(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
    
            posX -= MathHelper.cos((rotationYaw / 180.0f) * (float) Math.PI) * 0.16f;
            posY += 0.20000000149011612d;
            posZ -= MathHelper.sin((rotationYaw / 180.0f) * (float) Math.PI) * 0.16f;
    
            if (entity instanceof EntityPlayer) {
                posY -= 0.40000000596046448d;
            }
    
            setPosition(posX, posY, posZ);
    
            motionX = -MathHelper.sin((rotationYaw / 180.0f) * (float) Math.PI) * MathHelper.cos((rotationPitch / 180.0f) * (float) Math.PI);
            motionZ = MathHelper.cos((rotationYaw / 180.0f) * (float) Math.PI) * MathHelper.cos((rotationPitch / 180.0f) * (float) Math.PI);
            motionY = -MathHelper.sin((rotationPitch / 180.0f) * (float) Math.PI);
    
            float f1 = 1.0f;
    
            if (entity instanceof EntityPlayer) {
                playerFire = true;
    
                float f2 = 0.3333333f;
    
                float f3 = f2 / 0.1f;
    
                if (f3 > 0.0f) {
                    f1 = (float) ((double) f1 * (1.0d + 2.0d / (double) f3));
                }
            }
    
            if (Math.abs(entity.motionX) > 0.10000000000000001d || Math.abs(entity.motionY) > 0.10000000000000001d || Math.abs(entity.motionZ) > 0.10000000000000001d) {
                f1 *= 2.0f;
            }
    
            adjustMotion(motionX, motionY, motionZ, (float) (2.5d + ((double) world.rand.nextFloat() - 0.5d)), f1);
        }
    
        private void adjustMotion(double d, double d1, double d2, float f, float f1) {
            float f2 = MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
    
            d /= f2;
            d1 /= f2;
            d2 /= f2;
    
            d += rand.nextGaussian() * 0.0074999998323619366d * (double) f1;
            d1 += rand.nextGaussian() * 0.0074999998323619366d * (double) f1;
            d2 += rand.nextGaussian() * 0.0074999998323619366d * (double) f1;
    
            d *= f;
            d1 *= f;
            d2 *= f;
    
            motionX = d;
            motionY = d1;
            motionZ = d2;
            float f3 = MathHelper.sqrt(d * d + d2 * d2);
    
            prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180.0d) / Math.PI);
            prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f3) * 180.0d) / Math.PI);
            aoLightValueScratchXYZNNP = 0;
        }
    
        @Override
        protected void onImpact(@Nonnull RayTraceResult rtr) {
            if (isDead || rtr.entityHit instanceof EntityPlayer || world.isRemote)
                return;
    
            // If the entity hit is null, it has hit a block
            // Try to get an entity nearby the hit range.
            // This is made to avoid accuracy issues and the ray passing through the entity.
            if (rtr.entityHit == null) {
                BlockPos pos = rtr.getBlockPos();
                rtr.entityHit = world.getEntitiesInAABBexcluding(null,
                        new AxisAlignedBB(pos.getX() - 1.5, pos.getY() - 1.5, pos.getZ() - 1.5,
                                pos.getX() + 1.5, pos.getY() + 1.5, pos.getZ() + 1.5),
                        (e) -> !(e instanceof EntityPlayer)
                ).stream().findFirst().orElse(null);
    
                // if entity is still null, but the block is not a full cube
                // we can just ignore it and let the ray pass through
                if (rtr.entityHit == null && !world.getBlockState(pos).isFullCube()) {
                    return;
                }
            }
    
            if (rtr.entityHit != null) {
                if (rtr.entityHit instanceof EntityLiving) {
                    boolean flag = false;
                    Entity Hit = rtr.entityHit;
    
                    if (Hit instanceof EntityCreepBase && Hit instanceof IEntityCanChangeSize) {
                        EntityCreepBase entityCreep = (EntityCreepBase) Hit;
                        IEntityCanChangeSize entitySizable = (IEntityCanChangeSize) Hit;
    
                        float currentSize = entityCreep.getModelSize();
                        if (currentSize > entitySizable.maxShrink()) {
                            float shrink = entitySizable.getShrinkRayAmount();
                            entityCreep.shrinkModelSize(shrink, entitySizable.maxShrink());
    
                            // Calculate the difference between the old model size and new
                            // Then, apply it to the hitbox size.
                            float shrinkDifference = 100 - (entityCreep.getModelSize() * 100f / currentSize);
                            entityCreep.shrinkHitboxSize(entityCreep.currentSize / 100f * shrinkDifference);
    
                            entitySizable.onShrink(this);
                        } else {
                            entityCreep.setDead();
                            flag = true;
                        }
                    }
    
                    if (flag) {
                        smoke();
                        playSound(CreepsSoundHandler.shrinkKillSound, 1.0f, 1.0f / (rand.nextFloat() * 0.1f + 0.95f));
                    }
                }
            } else {
                BlockPos hitBlockPos = rtr.getBlockPos();
    
                hitX = hitBlockPos.getX();
    
                hitY = hitBlockPos.getY();
    
                hitZ = hitBlockPos.getZ();
    
                blockHit = world.getBlockState(hitBlockPos);
    
                motionX = (float) (rtr.hitVec.x - posX);
    
                motionY = (float) (rtr.hitVec.y - posY);
    
                motionZ = (float) (rtr.hitVec.z - posZ);
    
                float f1 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
    
                posX -= (motionX / (double) f1) * 0.05000000074505806d;
    
                posY -= (motionY / (double) f1) * 0.05000000074505806d;
    
                posZ -= (motionZ / (double) f1) * 0.05000000074505806d;
    
                aoLightValueZPos = true;
    
                if (blockHit.getBlock() == Blocks.ICE) {
                    world.setBlockState(hitBlockPos, Blocks.FLOWING_WATER.getDefaultState());
                }
            }
    
            playSound(CreepsSoundHandler.raygunSound, 0.2f, 1.0f / (rand.nextFloat() * 0.1f + 0.95f));
    
            setDead();
        }
    
        @Override
        public boolean isInRangeToRenderDist(double d) {
            return true;
        }
    
        @Override
        public void onUpdate() {
            super.onUpdate();
    
            if (aoLightValueScratchXYNN == 5) {
                setDead();
            }
    
            if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
                float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
    
                prevRotationYaw = rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180.0d) / Math.PI);
    
                prevRotationPitch = rotationPitch = (float) ((Math.atan2(motionY, f) * 180.0d) / Math.PI);
            }
    
            if (aoLightValueZPos) {
                if (world.getBlockState(new BlockPos(hitX, hitY, hitZ)) != blockHit) {
                    aoLightValueZPos = false;
    
                    motionX *= rand.nextFloat() * 0.2f;
    
                    motionZ *= rand.nextFloat() * 0.2f;
    
                    aoLightValueScratchXYZNNP = 0;
    
                    aoLightValueScratchXYNN = 0;
                } else {
                    aoLightValueScratchXYZNNP++;
    
                    if (aoLightValueScratchXYZNNP == 5) {
                        setDead();
                    }
    
                    return;
                }
            } else {
                aoLightValueScratchXYNN++;
            }
    
            posX += motionX;
    
            posY += motionY;
    
            posZ += motionZ;
    
            float f2 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
    
            rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180.0d) / Math.PI);
    
            for (rotationPitch = (float) ((Math.atan2(motionY, f2) * 180.0d) / Math.PI); (rotationPitch - prevRotationPitch) < -180.0f; prevRotationPitch -= 360.0f)
                ;
    
            for (; (rotationPitch - prevRotationPitch) >= 180.0f; prevRotationPitch += 360.0f) ;
    
            for (; (rotationYaw - prevRotationYaw) < -180.0f; prevRotationYaw -= 360.0f) ;
    
            for (; (rotationYaw - prevRotationYaw) >= 180.0f; prevRotationYaw += 360.0f) ;
    
            rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2f;
    
            rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2f;
    
            float f3 = 0.99f;
    
            if (handleWaterMovement()) {
                for (int l = 0; l < 4; l++) {
                    float f7 = 0.25f;
    
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * (double) f7, posY - motionY * (double) f7, posZ - motionZ * (double) f7, motionX, motionY, motionZ);
                }
    
                f3 = 0.8f;
    
                setDead();
            }
    
            motionX *= f3;
    
            motionZ *= f3;
    
            setPosition(posX, posY, posZ);
        }
    
        @Override
        public void setDead() {
            super.setDead();
            lightValueOwn = null;
        }
    
        private void smoke() {
            ((WorldServer) world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 140, 0.2d, 0.2d, 0.2d, 0.12d);
        }
    
    }
