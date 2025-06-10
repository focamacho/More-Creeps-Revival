package com.morecreepsrevival.morecreeps.client.render;

import com.morecreepsrevival.morecreeps.common.entity.EntityMummy;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderMummy<U extends EntityMummy> extends RenderCreep<U>
{
    public RenderMummy(RenderManager renderManager)
    {
        super(renderManager, new ModelZombie(), 0.5f);
    }
}
