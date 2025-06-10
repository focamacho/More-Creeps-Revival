package com.morecreepsrevival.morecreeps.common.entity;

public interface IEntityCanChangeSize {
    float maxGrowth();

    float getGrowRayAmount();

    void onGrow(EntityGrow var1);

    float maxShrink();

    float getShrinkRayAmount();

    void onShrink(EntityShrink var1);
}
