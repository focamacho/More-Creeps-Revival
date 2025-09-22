package com.morecreepsrevival.morecreeps.client.render;

import com.morecreepsrevival.morecreeps.common.MoreCreepsAndWeirdos;
import com.morecreepsrevival.morecreeps.common.entity.EntityCreepBase;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class RenderCreep<T extends EntityCreepBase> extends RenderLiving<T> {
    public RenderCreep(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    public RenderCreep(RenderManager renderManager, ModelBase modelBase) {
        super(renderManager, modelBase, 1.0f);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(MoreCreepsAndWeirdos.modid, entity.getTexture());
    }

    @Override
    public void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        shadowSize = getShadowSize(entity);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void doScaling(T entity) {
        float modelSize = entity.getModelSize();
        GlStateManager.scale(modelSize, modelSize, modelSize);
    }

    @Override
    protected void preRenderCallback(T entity, float f) {
        super.preRenderCallback(entity, f);

        if (shouldDoScaling(entity)) {
            doScaling(entity);
        }
    }

    protected boolean shouldDoScaling(T entity) {
        return true;
    }

    protected float getShadowSize(T entity) {
        return shadowSize;
    }

    protected boolean shouldDrawHealthBar() {
        return true;
    }

}