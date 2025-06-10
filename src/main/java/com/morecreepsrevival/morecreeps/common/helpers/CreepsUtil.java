package com.morecreepsrevival.morecreeps.common.helpers;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBreaking.Factory;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class
CreepsUtil {
    static final float fullRadian = 3.1415927F;
    @SideOnly(Side.CLIENT)
    static Factory eatingParticlesFactory = new Factory();

    public CreepsUtil() {
    }

    public static Vec2f RandomVector2dNormalized(Random rand) {
        float randfactor = (float)GetRandomUnit(rand);
        float resultangle = 3.1415927F * randfactor;
        return new Vec2f(MathHelper.cos(resultangle), MathHelper.sin(resultangle));
    }

    public static Vec3d RandomVector3dNormalized(Random rand) {
        float randfactorhorizontal = (float)GetRandomUnit(rand);
        float randfactorvertical = (float)GetRandomUnit(rand);
        float horizontalangle = 3.1415927F * randfactorhorizontal;
        float verticalangle = 3.1415927F * randfactorvertical;
        return new Vec3d((double)MathHelper.cos(horizontalangle), (double)MathHelper.cos(verticalangle), (double)MathHelper.sin(horizontalangle));
    }

    public static double GetRandomUnit(Random rand) {
        return (double)rand.nextInt(100000) * 1.0E-5D;
    }

    public static double GetRandomUnitMinus(Random rand) {
        return (double)(rand.nextInt(200000) - 100000) * 1.0E-5D;
    }

    @SideOnly(Side.CLIENT)
    public static Particle SpawnEatingParticle(World world, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float size, Item itemFodder) {
        if (Minecraft.getMinecraft().isGamePaused()) {
            return null;
        } else {
            Particle toSpawn = eatingParticlesFactory.createParticle(-1, world, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, new int[]{Item.getIdFromItem(itemFodder)});
            toSpawn.multipleParticleScaleBy(size);
            Minecraft.getMinecraft().effectRenderer.addEffect(toSpawn);
            return toSpawn;
        }
    }

    public static void SpawnEatingParticleRGB(World world, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float size, float colorRed, float colorGreen, float colorBlue, Item itemFodder) {
        if (!Minecraft.getMinecraft().isGamePaused()) {
            Particle toSpawn = eatingParticlesFactory.createParticle(-1, world, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, new int[]{Item.getIdFromItem(itemFodder)});
            toSpawn.multipleParticleScaleBy(size);
            toSpawn.setRBGColorF(colorRed, colorGreen, colorBlue);
            Minecraft.getMinecraft().effectRenderer.addEffect(toSpawn);
        }
    }

    public static boolean TryPlaceTorch(World world, BlockPos pos) {
        IBlockState basestate = Blocks.TORCH.getDefaultState();
        if (!world.isAirBlock(pos)) {
            return false;
        } else {
            Block block = basestate.getBlock();
            EnumFacing facing = EnumFacing.DOWN;
            if (CanPlaceTorchOn(world, pos, EnumFacing.NORTH, block)) {
                facing = EnumFacing.NORTH;
            }

            if (CanPlaceTorchOn(world, pos, EnumFacing.SOUTH, block)) {
                facing = EnumFacing.SOUTH;
            }

            if (CanPlaceTorchOn(world, pos, EnumFacing.WEST, block)) {
                facing = EnumFacing.WEST;
            }

            if (CanPlaceTorchOn(world, pos, EnumFacing.EAST, block)) {
                facing = EnumFacing.EAST;
            }

            if (CanPlaceTorchOn(world, pos, EnumFacing.UP, block)) {
                facing = EnumFacing.UP;
            }

            boolean sucess = facing != EnumFacing.DOWN;
            if (sucess) {
                world.setBlockState(pos, basestate.withProperty(BlockTorch.FACING, facing), 2);
            }

            return sucess;
        }
    }

    public static boolean TryPlaceRedstoneTorch(World world, BlockPos pos) {
        IBlockState basestate = Blocks.REDSTONE_TORCH.getDefaultState();
        if (!world.isAirBlock(pos)) {
            return false;
        } else {
            Block block = basestate.getBlock();
            EnumFacing facing = EnumFacing.DOWN;
            if (CanPlaceTorchOn(world, pos, EnumFacing.NORTH, block)) {
                facing = EnumFacing.NORTH;
            }

            if (CanPlaceTorchOn(world, pos, EnumFacing.SOUTH, block)) {
                facing = EnumFacing.SOUTH;
            }

            if (CanPlaceTorchOn(world, pos, EnumFacing.WEST, block)) {
                facing = EnumFacing.WEST;
            }

            if (CanPlaceTorchOn(world, pos, EnumFacing.EAST, block)) {
                facing = EnumFacing.EAST;
            }

            if (CanPlaceTorchOn(world, pos, EnumFacing.UP, block)) {
                facing = EnumFacing.UP;
            }

            boolean sucess = facing != EnumFacing.DOWN;
            if (sucess) {
                world.setBlockState(pos, basestate.withProperty(BlockTorch.FACING, facing), 2);
            }

            return sucess;
        }
    }

    private static boolean CanPlaceTorchOn(World worldIn, BlockPos pos, EnumFacing facing, Block block) {
        Axis enumfacing$axis = facing.getAxis();
        EnumFacing enumfacing1 = facing.getOpposite();
        BlockPos blockpos = pos.offset(enumfacing1);
        worldIn.getBlockState(pos);
        return worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, facing) == BlockFaceShape.SOLID;
    }

    public static int getLightLevel(World world, BlockPos position, boolean ignoreSkyLight, boolean ignoreBlockLight) {
        if (ignoreBlockLight && ignoreSkyLight) {
            ignoreBlockLight = false;
            ignoreSkyLight = false;
        }
        return 0;
    }
}
