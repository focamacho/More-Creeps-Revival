package com.morecreepsrevival.morecreeps.client.gui;

import com.morecreepsrevival.morecreeps.common.entity.EntityCreepBaseOwnable;
import com.morecreepsrevival.morecreeps.common.networking.CreepsPacketHandler;
import com.morecreepsrevival.morecreeps.common.networking.message.MessageChangeTamedEntityName;
import com.morecreepsrevival.morecreeps.common.networking.message.MessageSetEntityWanderState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiTamableEntity extends GuiScreen {

    private final EntityCreepBaseOwnable entity;
    private GuiTextField nameScreen;

    public GuiTamableEntity(EntityCreepBaseOwnable entityIn) {
        entity = entityIn;
    }

    @Override
    public void updateScreen() {
        nameScreen.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        buttonList.clear();
        byte byte0 = -16;

        buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 108 + byte0, 98, 20, "§6XX§f FIGHT §6XX"));
        buttonList.add(new GuiButton(3, width / 2 + 2, height / 4 + 108 + byte0, 98, 20, "§6|| §f STAY §6||"));
        buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 128 + byte0, 98, 20, "§6<<§f WANDER §6>>"));
        buttonList.add(new GuiButton(5, width / 2 + 2, height / 4 + 128 + byte0, 98, 20, "§6-[§f TRAIN §6]-"));
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 158 + byte0, 98, 20, "Save"));
        buttonList.add(new GuiButton(1, width / 2 + 2, height / 4 + 158 + byte0, 98, 20, I18n.format("gui.cancel")));
        nameScreen = new GuiTextField(1, fontRenderer, width / 2 - 100, height / 4 - 20, 200, 20);

        nameScreen.setMaxStringLength(31);
        nameScreen.setCanLoseFocus(true);
        nameScreen.setText(entity.getName());
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException ignore) {
        }

        nameScreen.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }

        mc.displayGuiScreen(null);

        int entityId = entity.getEntityId();

        if (button.id != 1 && button.id != 5) {
            CreepsPacketHandler.INSTANCE.sendToServer(new MessageChangeTamedEntityName(entityId, nameScreen.getText()));
        }

        switch (button.id) {
            case 2:
                CreepsPacketHandler.INSTANCE.sendToServer(new MessageSetEntityWanderState(entityId, 2));

                break;
            case 3:
                CreepsPacketHandler.INSTANCE.sendToServer(new MessageSetEntityWanderState(entityId, 0));

                break;
            case 4:
                CreepsPacketHandler.INSTANCE.sendToServer(new MessageSetEntityWanderState(entityId, 1));

                break;
            case 5:
                mc.displayGuiScreen(new GuiTamableEntityTraining(entity));

                break;
            default:
                break;
        }
    }

    @Override
    protected void keyTyped(char c, int i) {
        nameScreen.textboxKeyTyped(c, i);

        if (i == 1) {
            mc.displayGuiScreen(null);
        } else if (c == '\r') {
            actionPerformed(buttonList.get(0));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        byte byte0 = -16;

        int k = 0;

        drawWorldBackground(1);

        drawCenteredString(fontRenderer, entity.getName().toUpperCase() + " COMMAND CENTER", width / 2, height / 4 - 40, 0xffffff);

        nameScreen.drawTextBox();

        drawString(fontRenderer, "§6LEVEL  §f" + entity.getLevel(), (width / 2 - 100) + k, height / 4 + 28 + byte0, 0xff8d13);

        drawString(fontRenderer, "§6NEXT LVL  §f" + entity.getTotalDamage() + "§3/§f" + entity.getLevelDamage(), width / 2 + 2 + k, height / 4 + 28 + byte0, 0xff8d13);
        drawString(fontRenderer, "§6HEALTH  §f" + entity.getHealth() + "§3/§f" + entity.getMaxHealth(), (width / 2 - 100) + k, height / 4 + 43 + byte0, 0xff8d13);
        drawString(fontRenderer, "§6EXPERIENCE  §f" + entity.getExperience(), width / 2 + 2 + k, height / 4 + 43 + byte0, 0xff8d13);
        drawString(fontRenderer, "§6ATTACK  §f" + entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue(), (width / 2 - 100) + k, height / 4 + 58 + byte0, 0xff8d13);
        drawString(fontRenderer, "§6SPEED  §f" + entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue(), width / 2 + 2 + k, height / 4 + 58 + byte0, 0xff8d13);
        drawCenteredString(fontRenderer, "§3" + entity.getLevelName(), width / 2 + 2 + k, height / 4 + 78 + byte0, 0xff8d13);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
