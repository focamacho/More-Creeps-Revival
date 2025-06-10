package com.morecreepsrevival.morecreeps.client.models;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

public class ModelRobotTed extends ModelBase {
    public ModelRenderer tedhead;
    public ModelRenderer body;
    public ModelRenderer body2;
    public ModelRenderer legL;
    public ModelRenderer legR;
    public ModelRenderer arm1L;
    public ModelRenderer arm1R;
    public ModelRenderer arm2L;
    public ModelRenderer arm2R;
    public ModelRenderer hornL;
    public ModelRenderer hornR;
    public ModelRenderer eyeL;
    public ModelRenderer eyeR;
    public ModelRenderer visor;
    public boolean heldItemLeft;
    public boolean heldItemRight;
    public boolean isSneak;
    long bouncePeriod;
    long sugarPeriod;
    long lastMS;
    float bounceDelta;
    float sugarDelta;
    float lastOffsetModification;
    Random sugarRandom;
    ModelRenderer[] models;

    public ModelRobotTed() {
        this(0.0F);
    }

    public ModelRobotTed(float f) {
        this(f, 0.0F);
    }

    public ModelRobotTed(float f, float f1) {
        this.bouncePeriod = 100000L;
        this.sugarPeriod = 25L;
        this.bounceDelta = 0.0F;
        this.sugarDelta = 0.0F;
        this.lastOffsetModification = 0.0F;
        this.sugarRandom = new Random();
        float f2 = 0.0F;
        this.tedhead = new ModelRenderer(this, 0, 0);
        this.tedhead.addBox(-2.5F, -6.0F, -3.0F, 5, 6, 6, f2);
        this.tedhead.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.body = new ModelRenderer(this, 26, 2);
        this.body.addBox(-5.0F, -10.0F, -4.0F, 10, 10, 8, f2);
        this.body.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.body2 = new ModelRenderer(this, 36, 24);
        this.body2.addBox(-4.0F, 0.0F, -3.0F, 8, 2, 6, f2);
        this.body2.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.legL = new ModelRenderer(this, 22, 22);
        this.legL.addBox(-1.5F, 0.0F, -1.5F, 3, 2, 3, f2);
        this.legL.setRotationPoint(2.0F, 19.0F, 0.0F);
        this.legR = new ModelRenderer(this, 22, 22);
        this.legR.addBox(-1.5F, 0.0F, -1.5F, 3, 2, 3, f2);
        this.legR.setRotationPoint(-2.0F, 19.0F, 0.0F);
        this.arm1L = new ModelRenderer(this, 0, 12);
        this.arm1L.addBox(0.0F, 0.0F, -1.0F, 2, 5, 2, f2);
        this.arm1L.setRotationPoint(5.0F, 7.0F, 0.0F);
        this.arm1L.rotateAngleZ = -0.17453F;
        this.arm1R = new ModelRenderer(this, 0, 12);
        this.arm1R.addBox(-2.0F, 0.0F, -1.0F, 2, 5, 2, f2);
        this.arm1R.setRotationPoint(-5.0F, 7.0F, 0.0F);
        this.arm1R.rotateAngleZ = 0.17453F;
        this.arm2L = new ModelRenderer(this, 0, 24);
        this.arm2L.addBox(1.0F, 4.5F, 1.0F, 2, 5, 2, f2);
        this.arm2L.setRotationPoint(5.0F, 7.0F, 0.0F);
        this.arm2L.rotateAngleX = 0.08727F;
        this.arm2R = new ModelRenderer(this, 0, 24);
        this.arm2R.addBox(-3.0F, 4.5F, 1.0F, 2, 5, 2, f2);
        this.arm2R.setRotationPoint(-5.0F, 7.0F, 0.0F);
        this.arm2R.rotateAngleX = 0.08727F;
        this.hornL = new ModelRenderer(this, 8, 12);
        this.hornL.addBox(0.0F, -10.0F, -0.5F, 1, 4, 1, f2);
        this.hornL.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.hornL.rotateAngleZ = 0.2618F;
        this.hornR = new ModelRenderer(this, 8, 12);
        this.hornR.addBox(-1.0F, -10.0F, -0.5F, 1, 4, 1, f2);
        this.hornR.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.hornR.rotateAngleZ = -0.2618F;
        this.eyeL = new ModelRenderer(this, 8, 24);
        this.eyeL.addBox(0.5F, -4.5F, -4.5F, 1, 1, 1, f2);
        this.eyeL.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.eyeR = new ModelRenderer(this, 8, 24);
        this.eyeR.addBox(-1.5F, -4.5F, -4.5F, 1, 1, 1, f2);
        this.eyeR.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.visor = new ModelRenderer(this, 9, 20);
        this.visor.addBox(-3.0F, -5.0F, -3.8F, 6, 2, 1, f2);
        this.visor.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.models = new ModelRenderer[]{this.tedhead, this.body, this.body2, this.legL, this.legR, this.arm1L, this.arm1R, this.arm2L, this.arm2R, this.hornL, this.hornR, this.eyeL, this.eyeR, this.visor};
        this.lastMS = System.currentTimeMillis();
    }

    public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        float bounceAmount = 0.1F;
        long currentMS = System.currentTimeMillis();
        long difference = currentMS - this.lastMS;
        this.bounceDelta = (float)((double)this.bounceDelta + (double)difference / (double)this.bouncePeriod);
        this.sugarDelta = (float)((double)this.sugarDelta + (double)difference / (double)this.sugarPeriod);
        this.lastMS = currentMS;
        this.bounceDelta -= (float)((int)this.bounceDelta);
        boolean shouldNotSugarSpawn = this.sugarDelta < 1.0F;
        this.sugarDelta -= (float)((int)this.sugarDelta);
        float sindelta = MathHelper.sin(this.bounceDelta * 180.0F);
        float sProgress = sindelta * bounceAmount;
        this.setBounce(sProgress);
        boolean wichLeg = true;

        for(int i = 0; i < 8; ++i) {
            this.spawnSugar(shouldNotSugarSpawn, entity, sProgress, f3, wichLeg);
            wichLeg = !wichLeg;
        }

        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.tedhead.render(f5);
        this.body.render(f5);
        this.body2.render(f5);
        this.legL.render(f5);
        this.legR.render(f5);
        this.arm1L.render(f5);
        this.arm1R.render(f5);
        this.arm2L.render(f5);
        this.arm2R.render(f5);
        this.hornL.render(f5);
        this.hornR.render(f5);
        this.eyeL.render(f5);
        this.eyeR.render(f5);
        this.visor.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.tedhead.rotateAngleY = f3 / 57.295776F;
        this.tedhead.rotateAngleX = f4 / 57.295776F;
        this.hornR.rotateAngleY = f3 / 57.295776F;
        this.hornR.rotateAngleX = f4 / 57.295776F;
        this.hornL.rotateAngleY = f3 / 57.295776F;
        this.hornL.rotateAngleX = f4 / 57.295776F;
        this.visor.rotateAngleY = f3 / 57.295776F;
        this.visor.rotateAngleX = f4 / 57.295776F;
        this.eyeL.rotateAngleY = f3 / 57.295776F;
        this.eyeL.rotateAngleX = f4 / 57.295776F;
        this.eyeR.rotateAngleY = f3 / 57.295776F;
        this.eyeR.rotateAngleX = f4 / 57.295776F;
        this.arm1R.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 3.0F * f1 * 0.5F - 0.5F;
        this.arm1L.rotateAngleX = MathHelper.cos(f * 0.6662F) * 3.0F * f1 * 0.5F - 0.5F;
        this.arm2R.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.1415927F) * 3.0F * f1 * 0.5F - 0.95F;
        this.arm2L.rotateAngleX = MathHelper.cos(f * 0.6662F) * 3.0F * f1 * 0.5F - 0.95F;
    }

    @SideOnly(Side.CLIENT)
    private void setBounce(float bounce) {
        if (!Minecraft.getMinecraft().isGamePaused()) {
            ModelRenderer[] var2 = this.models;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                ModelRenderer current = var2[var4];
                current.offsetY += bounce - this.lastOffsetModification;
            }

            this.lastOffsetModification = bounce;
        }

    }

    @SideOnly(Side.CLIENT)
    private void spawnSugar(boolean shouldNot, Entity entity, float ychange, float entityYaw, boolean rightLeg) {
        if (!Minecraft.getMinecraft().isGamePaused() && !shouldNot) {
            double xMov = (double)(this.sugarRandom.nextInt(8) - 4 + 1) * 0.06;
            double zMov = (double)(this.sugarRandom.nextInt(8) - 4 + 1) * 0.06;
            double xVariation = (double)(this.sugarRandom.nextInt(200) - 100) * 0.005;
            double zVariation = (double)(this.sugarRandom.nextInt(200) - 100) * 0.005;
            double legMiddleX = entity.posX + xVariation + (double)this.legR.offsetX + (double)(this.legR.offsetX - this.legL.offsetX) * 0.5;
            double legMiddleZ = entity.posZ + zVariation + (double)this.legR.offsetZ + (double)(this.legR.offsetZ - this.legL.offsetZ) * 0.5;
            double legY = entity.posY - (double)ychange;
            ParticleBreaking.Factory maker = new ParticleBreaking.Factory();
            Particle sugarParticle = maker.createParticle(-1, entity.world, legMiddleX, legY + 0.5, legMiddleZ, xMov, -0.5, zMov, new int[]{Item.getIdFromItem(Items.SUGAR)});
            Minecraft.getMinecraft().effectRenderer.addEffect(sugarParticle);
        }

    }
}
