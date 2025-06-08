package com.morecreepsrevival.morecreeps.common.items;

import com.google.common.collect.Sets;
import com.morecreepsrevival.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class CreepsItemTool extends ItemTool {

    private static final Set<Block> EffectiveOn = Sets.newHashSet(Blocks.STONE);

    public CreepsItemTool(String itemName, ToolMaterial toolMaterial) {
        super(toolMaterial, EffectiveOn);

        setRegistryName(new ResourceLocation(MoreCreepsAndWeirdos.modid, itemName));

        setUnlocalizedName(MoreCreepsAndWeirdos.modid + "." + itemName);

        setCreativeTab(MoreCreepsAndWeirdos.creativeTab);
    }

    public float getSoundVolume() {
        return 0.5f;
    }

    public float getSoundPitch() {
        return (0.4f / ((itemRand.nextFloat() * 0.4f) + 0.8f));
    }
}
