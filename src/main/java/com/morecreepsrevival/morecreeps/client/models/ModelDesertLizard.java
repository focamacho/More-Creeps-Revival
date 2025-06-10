package com.morecreepsrevival.morecreeps.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelDesertLizard extends ModelBase {
    public ModelRenderer headDesertlizard;
    public ModelRenderer fin;
    public ModelRenderer body;
    public ModelRenderer mouth;
    public ModelRenderer tail0;
    public ModelRenderer tail1;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;
    public float tailwag;
    public int taildirection;

    public ModelDesertLizard() {
        this(6, 0.0F);
        this.tailwag = 0.0F;
        this.taildirection = 1;
    }

    public ModelDesertLizard(float f) {
        this(6, f);
    }

    public ModelDesertLizard(int i, float f) {
        this.fin = new ModelRenderer(this, 16, 9);
        this.fin.addBox(0.0F, -8.0F, -2.0F, 1, 16, 7, f);
        this.fin.setRotationPoint(0.0F, (float)(17 - i), 2.0F);
        this.tail0 = new ModelRenderer(this, 32, 64);
        this.tail0.addBox(-4.0F, 12.0F, -7.0F, 8, 12, 3, f);
        this.tail0.setRotationPoint(0.0F, (float)(15 - i), 2.0F);
        this.tail1 = new ModelRenderer(this, 30, 64);
        this.tail1.addBox(-3.0F, 24.0F, -7.0F, 6, 10, 2, f);
        this.tail1.setRotationPoint(0.0F, (float)(14 - i), 2.0F);
        this.mouth = new ModelRenderer(this, 16, 0);
        this.mouth.addBox(-2.0F, 4.0F, -14.0F, 4, 2, 6, f);
        this.mouth.setRotationPoint(0.0F, (float)(18 - i), -6.0F);
        this.headDesertlizard = new ModelRenderer(this, 0, 0);
        this.headDesertlizard.addBox(-3.0F, 2.0F, -9.0F, 6, 3, 8, f);
        this.headDesertlizard.setRotationPoint(0.0F, (float)(18 - i), -6.0F);
        this.body = new ModelRenderer(this, 32, 64);
        this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 22, 5, f);
        this.body.setRotationPoint(0.0F, (float)(17 - i), 2.0F);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, i, 4, f);
        this.leg1.setRotationPoint(-3.0F, (float)(24 - i), 11.0F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, i, 4, f);
        this.leg2.setRotationPoint(3.0F, (float)(24 - i), 11.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, i, 4, f);
        this.leg3.setRotationPoint(-3.0F, (float)(24 - i), -5.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, i, 4, f);
        this.leg4.setRotationPoint(3.0F, (float)(24 - i), -5.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5);
        this.headDesertlizard.render(f5);
        this.fin.render(f5);
        this.body.render(f5);
        this.mouth.render(f5);
        this.tail0.render(f5);
        this.tail1.render(f5);
        this.leg1.render(f5);
        this.leg2.render(f5);
        this.leg3.render(f5);
        this.leg4.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        this.headDesertlizard.rotateAngleX = -(f4 / 57.29578F);
        this.headDesertlizard.rotateAngleY = f3 / 57.29578F;
        this.mouth.rotateAngleX = -(f4 / 57.29578F);
        this.mouth.rotateAngleY = f3 / 57.29578F;
        this.fin.rotateAngleX = 1.570796F;
        this.body.rotateAngleX = 1.570796F;
        if (this.taildirection > 0) {
            this.tailwag += 8.0E-4F;
            if (this.tailwag > 0.18F) {
                this.taildirection *= -1;
            }
        } else {
            this.tailwag -= 8.0E-4F;
            if ((double)this.tailwag < -0.2D) {
                this.taildirection *= -1;
            }
        }

        this.tail0.rotateAngleY = MathHelper.cos(f * 0.6662F) * this.tailwag;
        this.tail1.rotateAngleY = MathHelper.cos(f * 0.6662F) * this.tailwag;
        this.tail0.rotateAngleX = 1.440796F;
        this.tail1.rotateAngleX = 1.380796F;
        this.leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        this.leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        this.leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}

