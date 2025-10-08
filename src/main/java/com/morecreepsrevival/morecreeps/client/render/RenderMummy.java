package com.morecreepsrevival.morecreeps.client.render;

import com.morecreepsrevival.morecreeps.common.entity.EntityMummy;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderMummy<T extends EntityMummy> extends RenderCreep<T> {
    public RenderMummy(RenderManager renderManager) {
        super(renderManager, new ModelZombie(), 0.5f);
    }
}
