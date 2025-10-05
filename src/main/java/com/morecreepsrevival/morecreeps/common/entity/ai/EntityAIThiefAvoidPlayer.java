package com.morecreepsrevival.morecreeps.common.entity.ai;

import com.morecreepsrevival.morecreeps.common.entity.EntityThief;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIThiefAvoidPlayer extends EntityAIAvoidEntity<EntityPlayer> {

    public EntityAIThiefAvoidPlayer(EntityThief entityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, EntityPlayer.class, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @Override
    public boolean shouldExecute() {
        return ((EntityThief) entity).getStolen() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return ((EntityThief) entity).getStolen() && super.shouldContinueExecuting();
    }

}
