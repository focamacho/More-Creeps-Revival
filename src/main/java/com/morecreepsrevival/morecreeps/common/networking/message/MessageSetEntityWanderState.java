package com.morecreepsrevival.morecreeps.common.networking.message;

import com.morecreepsrevival.morecreeps.common.entity.EntityCreepBaseOwnable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetEntityWanderState implements IMessage {
    private int entityId;

    private int wanderState;

    public MessageSetEntityWanderState() {
    }

    public MessageSetEntityWanderState(int entityIdIn, int wanderStateIn) {
        entityId = entityIdIn;

        wanderState = wanderStateIn;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);

        buf.writeShort(wanderState);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();

        wanderState = buf.readShort();
    }

    public static class MessageHandler implements IMessageHandler<MessageSetEntityWanderState, IMessage> {
        @Override
        public IMessage onMessage(MessageSetEntityWanderState message, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().player;

            WorldServer world = player.getServerWorld();

            world.addScheduledTask(() -> {
                Entity entity = world.getEntityByID(message.entityId);

                if (entity instanceof EntityCreepBaseOwnable) {
                    EntityCreepBaseOwnable creep = (EntityCreepBaseOwnable) entity;

                    if (creep.isPlayerOwner(player)) {
                        creep.setWanderState(message.wanderState);

                        switch (message.wanderState) {
                            case 0:
                                player.sendMessage(new TextComponentTranslation("entity.morecreeps.wanderstate.0", creep.getName()));
                                break;
                            case 1:
                                player.sendMessage(new TextComponentTranslation("entity.morecreeps.wanderstate.1", creep.getName()));
                                break;
                            case 2:
                                player.sendMessage(new TextComponentTranslation("entity.morecreeps.wanderstate.2", creep.getName()));
                                break;
                            default:
                                break;
                        }
                    }
                }
            });

            return null;
        }
    }
}
