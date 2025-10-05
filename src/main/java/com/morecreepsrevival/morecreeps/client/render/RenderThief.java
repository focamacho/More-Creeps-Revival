package com.morecreepsrevival.morecreeps.client.render;

import com.morecreepsrevival.morecreeps.common.entity.EntityThief;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;

import javax.annotation.Nonnull;

public class RenderThief<T extends EntityThief> extends RenderCreep<T> {
    public RenderThief(RenderManager renderManager) {
        super(renderManager, new ModelBiped(), 0.5f);
        addLayer(new LayerHeldItem(this));
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    @Override
    public void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entity.getClosestEntity() != null) {
            entity.faceEntity(entity.getClosestEntity(), 1000F, 1000F);
            entity.rotationYawHead = entity.rotationYaw;
            entity.prevRotationPitch = entity.rotationPitch;
            entity.prevRotationYawHead = entity.rotationYaw;
            entity.prevRotationYaw = entity.rotationYaw;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
