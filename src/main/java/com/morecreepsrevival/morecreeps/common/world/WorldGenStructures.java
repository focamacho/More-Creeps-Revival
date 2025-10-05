package com.morecreepsrevival.morecreeps.common.world;

import com.morecreepsrevival.morecreeps.common.config.MoreCreepsConfig;
import com.morecreepsrevival.morecreeps.common.networking.CreepsPacketHandler;
import com.morecreepsrevival.morecreeps.common.networking.message.MessagePlayBattleCastleSound;
import com.morecreepsrevival.morecreeps.common.networking.message.MessagePlayPyramidDiscoveredSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenStructures implements IWorldGenerator {
    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateStructures(world, rand, chunkX, chunkZ);
    }

    private void generateStructures(World world, Random rand, int chunkX, int chunkZ) {
        if (MoreCreepsConfig.WorldGen.pyramidGen) {
            float pyramidChance = Math.max(0f, MoreCreepsConfig.WorldGen.pyramidRarityChance / 100f);

            if (pyramidChance > 0 && rand.nextFloat() < pyramidChance) {
                BlockPos pos = new BlockPos((chunkX << 4) + rand.nextInt(16), 65, (chunkZ << 4) + rand.nextInt(16));

                if ((new WorldGenPyramid()).generate(world, rand, pos)) {
                    for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(pos)).grow(256.0d, 256.0d, 256.0d))) {
                        CreepsPacketHandler.INSTANCE.sendTo(new MessagePlayPyramidDiscoveredSound(), (EntityPlayerMP) player);
                    }
                }
            }
        }

        if (MoreCreepsConfig.WorldGen.castleGen) {
            float castleChance = Math.max(0f, MoreCreepsConfig.WorldGen.castleRarityChance / 100f);
            if (rand.nextFloat() < castleChance) {
                BlockPos pos = new BlockPos((chunkX << 4) + rand.nextInt(16), rand.nextInt(40) + 80, (chunkZ << 4) + rand.nextInt(16));

                if ((new WorldGenCastle()).generate(world, rand, pos)) {
                    for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(pos)).grow(256.0d, 256.0d, 256.0d))) {
                        CreepsPacketHandler.INSTANCE.sendTo(new MessagePlayBattleCastleSound(), (EntityPlayerMP) player);
                    }
                }
            }
        }
    }
}
