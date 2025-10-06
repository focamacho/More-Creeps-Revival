package com.morecreepsrevival.morecreeps.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.client.IClientCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;

public class DebugCastleLoot extends CommandBase implements IClientCommand {
    @Override
    @Nonnull
    public String getName() {
        return "debugcastleloot";
    }

    @Override
    @Nonnull
    public String getUsage(@Nullable ICommandSender sender) {
        return "/debugcastleloot";
    }

    @Override
    public void execute(@Nullable MinecraftServer server, @Nullable ICommandSender sender, @Nullable String[] args) {
        if(!(sender instanceof EntityPlayer)) return;
        World world = sender.getEntityWorld();
        if(world.isRemote) return;

        ArrayList<LootTable> allLootTables = new ArrayList<>();
        allLootTables.add(getLootTable(world, new ResourceLocation("morecreeps:chests/castle_earth_gem")));
        allLootTables.add(getLootTable(world, new ResourceLocation("morecreeps:chests/castle_fire_gem")));
        allLootTables.add(getLootTable(world, new ResourceLocation("morecreeps:chests/castle_sky_gem")));
        allLootTables.add(getLootTable(world, new ResourceLocation("morecreeps:chests/castle_healing_gem")));
        allLootTables.add(getLootTable(world, new ResourceLocation("morecreeps:chests/castle_mining_gem")));
        for(int i = 0; i < 45; i++) {
            allLootTables.add(getLootTable(world, new ResourceLocation("morecreeps:chests/castle")));
        }

        ArrayList<ItemStack> generatedItems = new ArrayList<>();
        for(LootTable lootTable : allLootTables) {
            generatedItems.addAll(lootTable.generateLootForPools(world.rand, new LootContext.Builder((WorldServer) world)
                    .withPlayer((EntityPlayer) sender).build()));
        }

        ArrayList<ItemStack> organizedStacks = new ArrayList<>();
        for (ItemStack generatedItem: generatedItems) {
            ItemStack toSum = organizedStacks.stream().filter(
                    it -> it.isItemEqual(generatedItem) && it.getCount() < it.getMaxStackSize()
            ).findFirst().orElse(null);
            if(toSum != null) {
                if(toSum.getCount() + generatedItem.getCount() <= toSum.getMaxStackSize())
                    toSum.setCount(toSum.getCount() + generatedItem.getCount());
                else {
                    generatedItem.setCount(generatedItem.getCount() - (toSum.getMaxStackSize() - toSum.getCount()));
                    toSum.setCount(toSum.getMaxStackSize());
                    organizedStacks.add(generatedItem);
                }
            } else {
                organizedStacks.add(generatedItem);
            }
        }

        organizedStacks.sort(Comparator.comparing(it -> it.getItem().getRegistryName().toString()));

        // Fill chests until the list is empty
        TileEntityChest currentChest = null;
        for (int i = 0; i < organizedStacks.size(); i++) {
            if(i % 27 == 0) {
                int posX = i / 27;
                if(posX >= 2) posX += posX / 2;
                world.setBlockState(sender.getPosition().add(posX, 0, 0), Blocks.CHEST.getDefaultState());
                currentChest = (TileEntityChest) world.getTileEntity(sender.getPosition().add(posX, 0, 0));
            }

            currentChest.setInventorySlotContents(i % 27, organizedStacks.get(i));
        }
    }

    private LootTable getLootTable(World world, ResourceLocation resourceLocation) {
        return world.getLootTableManager()
                .getLootTableFromLocation(resourceLocation);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }
}
