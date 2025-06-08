package com.morecreepsrevival.morecreeps.common.items;

import net.minecraft.block.state.IBlockState;

public class ItemVhammer extends CreepsItemSword {

    public ItemVhammer() {
        super("vhammer", ToolMaterial.IRON);
        setMaxDamage(40);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return true;
    }
}
