package com.morecreepsrevival.morecreeps.client.render;

import com.morecreepsrevival.morecreeps.common.entity.EntityRockMonster;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderRockMonsterFactory implements IRenderFactory<EntityRockMonster> {


    @Override
    public Render<? super EntityRockMonster> createRenderFor(RenderManager renderManager) {
        return new RenderRockMonster<>(renderManager);
    }
}
