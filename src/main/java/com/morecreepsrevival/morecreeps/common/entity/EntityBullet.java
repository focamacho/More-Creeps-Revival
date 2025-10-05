package com.morecreepsrevival.morecreeps.common.entity;

import com.morecreepsrevival.morecreeps.common.config.MoreCreepsConfig;
import com.morecreepsrevival.morecreeps.common.sounds.CreepsSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBullet extends EntityRay {

    public EntityBullet(World worldIn) {
        super(worldIn);
    }

    public EntityBullet(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public EntityBullet(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityBullet(World worldIn, EntityLivingBase shooter, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    private void blood(Vec3d hitPlace) {
        double x = hitPlace.x;
        double y = hitPlace.y;
        double z = hitPlace.z;

        // TODO: blood effects
        world.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0f, 0f, 0f);
    }

    @Override
    protected void onHit(RayTraceResult rtr) {
        if(world.isRemote) return;
        Entity entityHit = rtr.entityHit;

        if (entityHit != null && entityHit == shootingEntity) {
            return;
        }

        if (entityHit != null && entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, shootingEntity), (float) damage)) {
            if (MoreCreepsConfig.Miscellaneous.blood && !(entityHit instanceof EntityCreepBase && !((EntityCreepBase) entityHit).canBleed())) {
                blood(rtr.hitVec);
            }
        }

        if (rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockHitPos = rtr.getBlockPos();

            if (!world.isAirBlock(blockHitPos)) {
                Block blockHit = world.getBlockState(blockHitPos).getBlock();

                if (blockHit == Blocks.ICE) {
                    world.setBlockState(blockHitPos, Blocks.WATER.getDefaultState());
                } else if (blockHit == Blocks.GLASS) {
                    blast();

                    world.destroyBlock(blockHitPos, false);
                } else {
                    blast();
                }
            }
        }

        playSound(CreepsSoundHandler.raygunSound, 0.2f, 1.0f / (rand.nextFloat() * 0.1f + 0.95f));

        setDead();
    }

    @Override
    protected void checkMeltBlock(double x, double y, double z) {}
}
