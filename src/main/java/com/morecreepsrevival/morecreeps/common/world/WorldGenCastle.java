package com.morecreepsrevival.morecreeps.common.world;

import com.morecreepsrevival.morecreeps.common.MoreCreepsAndWeirdos;
import com.morecreepsrevival.morecreeps.common.config.MoreCreepsConfig;
import com.morecreepsrevival.morecreeps.common.entity.EntityCastleKing;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class WorldGenCastle extends WorldGenerator {

    private static final int maxObstruct = 300;
    private static final int castleHeight = 5;
    private static Template STRUCTURE = null;
    private static BlockPos SIZE_STRUCTURE = null;

    public static boolean tryCacheStructure(World world) {
        if (STRUCTURE != null) return true;

        ResourceLocation location = new ResourceLocation(MoreCreepsAndWeirdos.modid, "castle(noentities)");
        MinecraftServer serv = world.getMinecraftServer();
        TemplateManager manager = ((WorldServer) world).getStructureTemplateManager();
        STRUCTURE = manager.get(serv, location);
        if (STRUCTURE == null) return false;
        SIZE_STRUCTURE = STRUCTURE.getSize();

        return true;
    }

    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        return newGenerate(world, rand, pos);
    }

    public boolean newGenerate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        if (!tryCacheStructure(world)) return false;

        BlockPos structurepos = findStructurePos(world, rand, pos);
        if (structurepos == null) return false;

        BlockPos structuresize = SIZE_STRUCTURE;

        PlacementSettings settings = new PlacementSettings().setIgnoreStructureBlock(false).setIgnoreEntities(false);

        IBlockState state = world.getBlockState(structurepos);

        world.notifyBlockUpdate(structurepos, state, state, 3);

        STRUCTURE.addBlocksToWorld(world, structurepos, settings);

        // Collect chests to set loot tables
        ArrayList<TileEntityChest> collectedChests = new ArrayList<>();
        for (int x = 0; x < structuresize.getX(); x++) {
            for (int y = 0; y < structuresize.getY(); y++) {
                for (int z = 0; z < structuresize.getZ(); z++) {
                    BlockPos currentPos = structurepos.add(x, y, z);
                    if (world.getBlockState(currentPos).getBlock() == Blocks.CHEST) {
                        TileEntity tileEntity = world.getTileEntity(currentPos);
                        if (tileEntity instanceof TileEntityChest) {
                            collectedChests.add((TileEntityChest) tileEntity);
                        }
                    }
                }
            }
        }

        // Add all the gems loot tables, then fill the rest with the default
        ArrayList<ResourceLocation> lootTablesToSet = new ArrayList<>();
        lootTablesToSet.add(new ResourceLocation(MoreCreepsAndWeirdos.modid, "chests/castle_earth_gem"));
        lootTablesToSet.add(new ResourceLocation(MoreCreepsAndWeirdos.modid, "chests/castle_fire_gem"));
        lootTablesToSet.add(new ResourceLocation(MoreCreepsAndWeirdos.modid, "chests/castle_healing_gem"));
        lootTablesToSet.add(new ResourceLocation(MoreCreepsAndWeirdos.modid, "chests/castle_mining_gem"));
        lootTablesToSet.add(new ResourceLocation(MoreCreepsAndWeirdos.modid, "chests/castle_sky_gem"));
        for(int i = 0; i < collectedChests.size() - 5; i++) {
            lootTablesToSet.add(new ResourceLocation(MoreCreepsAndWeirdos.modid, "chests/castle"));
        }

        // Set the loot tables
        for (TileEntityChest collectedChest : collectedChests) {
            collectedChest.clear();
            collectedChest.setLootTable(lootTablesToSet.remove(rand.nextInt(lootTablesToSet.size())), rand.nextLong());
        }

        int stx = structuresize.getX() + structurepos.getX();
        int sty = structurepos.getY() - 1;
        int stz = structuresize.getZ() + structurepos.getZ();

        int bgx = structurepos.getX() - 1;
        int bgy = structurepos.getY() - 11;
        int bgz = structurepos.getZ() - 1;

        /*
        for(int xl = bgx; xl <= stx; ++xl)
        {
            for(int yl = bgy; yl <= sty; ++yl)
            {
                for(int zl = bgz; zl <= stz; ++zl)
                {
                    BlockPos curr = new BlockPos(xl, yl, zl);

                    IBlockState bstate = world.getBlockState(curr);

                    if(!bstate.isFullBlock())
                    {
                        world.setBlockState(curr, Blocks.DIRT.getDefaultState());
                    }
                }
            }
        }*/

        Biome biome = world.getBiome(structurepos);

        Block topblock = biome.topBlock.getBlock();
        Block fillerblock = biome.fillerBlock.getBlock();

        fillCorners(world, topblock, bgx, sty + 1, bgz, stx, sty + 1, stz);
        fillCorners(world, fillerblock, bgx, bgy, bgz, stx, sty, stz);

        EntityCastleKing king = new EntityCastleKing(world);

        king.setLocationAndAngles(
                structurepos.getX() + structuresize.getX() / 2,
                structurepos.getY() + structuresize.getY(),
                structurepos.getZ() + structuresize.getZ() / 2,
                360f, 0f
        );

        king.determineBaseTexture();

        king.setInitialHealth();

        king.enablePersistence();

        world.spawnEntity(king);

        return true;
    }

    public BlockPos findStructurePos(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
        Biome biome = world.getBiome(pos);

        if (!((MoreCreepsConfig.Spawn.spawnInNonVanillaBiomes && MoreCreepsConfig.hasBiome(Objects.requireNonNull(biome.getRegistryName()).toString())) || Objects.requireNonNull(biome.getRegistryName()).getResourceDomain().equals("minecraft"))) {
            return null;
        }

        for (Type type : BiomeDictionary.getTypes(biome)) {
            if (type == Type.NETHER || type == Type.END) {
                return null;
            }
        }

        int x = pos.getX();

        int z = pos.getZ();

        /*EntityPlayer player = world.getClosestPlayer((double)x, (double)y, (double)z, 64.0d, false);

        if (player == null)
        {
            return false;
        }*/

        int castleX = x + rand.nextInt(8) - rand.nextInt(8);

        int castleY = 200;

        int castleZ = z + rand.nextInt(8) - rand.nextInt(8);

        /*if (Math.abs(castleX - player.posX) < 10.0d || Math.abs(castleZ - player.posZ) < 10.0d)
        {
            return false;
        }
        else*/

        int castlePosX = castleX;

        int castlePosY = castleY;

        int castlePosZ = castleZ;

        int foundation = 0;

        int foundationDepth = 0;

        boolean alternate = true;

        while (foundation < 3) {
            foundation = 0;

            Block block1 = world.getBlockState(new BlockPos(castlePosX - 4, castlePosY - foundationDepth, castlePosZ - 4)).getBlock();

            Block block2 = world.getBlockState(new BlockPos(castlePosX + 30, castlePosY - foundationDepth, castlePosZ - 4)).getBlock();

            Block block3 = world.getBlockState(new BlockPos(castlePosX - 4, castlePosY - foundationDepth, castlePosZ + 30)).getBlock();

            Block block4 = world.getBlockState(new BlockPos(castlePosX + 30, castlePosY - foundationDepth, castlePosZ + 30)).getBlock();

            if (block1 == Blocks.WATER || block1 == Blocks.FLOWING_WATER || block2 == Blocks.WATER || block2 == Blocks.FLOWING_WATER || block3 == Blocks.WATER || block3 == Blocks.FLOWING_WATER || block4 == Blocks.WATER || block4 == Blocks.FLOWING_WATER) {
                return null;
            } else if (block1 != Blocks.AIR && block1 != Blocks.LEAVES) {
                foundation++;
            }

            if (block2 != Blocks.AIR && block2 != Blocks.LEAVES) {
                foundation++;
            }

            if (block3 != Blocks.AIR && block3 != Blocks.LEAVES) {
                foundation++;
            }

            if (block4 != Blocks.AIR && block4 != Blocks.LEAVES) {
                foundation++;
            }

            foundationDepth++;
        }

        castlePosY = castleY = castlePosY - foundationDepth + 2;

        if (!world.isBlockLoaded(new BlockPos(castleX - 4, castleY, castleZ - 4)) || !world.isBlockLoaded(new BlockPos(castleX + 30, castleY, castleZ - 4)) || !world.isBlockLoaded(new BlockPos(castleX + 30, castleY, castleZ + 30)) || !world.isBlockLoaded(new BlockPos(castleX - 4, castleY, castleZ + 30))) {
            return null;
        }

        int maxI = (castleHeight * 7) + 7;

        int area = 0;

        for (int i = 0; i < maxI; i += 2) {
            for (int k = (-12 + i); k < 38; k += 2) {
                for (int j = (-12 + i); j < 38; j += 2) {
                    Block block = world.getBlockState(new BlockPos(x + k, castlePosY + i, z + j)).getBlock();

                    if (block != Blocks.AIR && block != Blocks.LEAVES && block != Blocks.LEAVES2 && block != Blocks.LOG && block != Blocks.LOG2) {
                        area++;

                        if (area > maxObstruct) {
                            return null;
                        }
                    }
                }
            }
        }

        return new BlockPos(castleX, castleY - 1, castleZ);
    }

    private void fillCorners(World world, Block block, int xm, int ym, int zm, int xmx, int ymx, int zmx) {
        IBlockState state = block.getDefaultState();

        for (int ynow = ym; ynow <= ymx; ++ynow) {
            for (int i = xm; i <= xmx; ++i) {

                BlockPos pos = new BlockPos(i, ynow, zm);

                if (!world.getBlockState(pos).isFullBlock())
                    world.setBlockState(pos, state);

                BlockPos pos2 = new BlockPos(i, ynow, zmx);
                if (!world.getBlockState(pos2).isFullBlock())
                    world.setBlockState(pos2, state);
            }

            for (int i = zm; i <= zmx; ++i) {

                BlockPos pos = new BlockPos(xm, ynow, i);

                if (!world.getBlockState(pos).isFullBlock())
                    world.setBlockState(pos, state);

                BlockPos pos2 = new BlockPos(xmx, ynow, i);

                if (!world.getBlockState(pos2).isFullBlock())
                    world.setBlockState(pos2, state);
            }
        }
    }

}
