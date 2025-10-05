package com.morecreepsrevival.morecreeps.client.gui;

import com.morecreepsrevival.morecreeps.common.MoreCreepsAndWeirdos;
import com.morecreepsrevival.morecreeps.common.entity.EntityCreepBase;
import com.morecreepsrevival.morecreeps.common.networking.CreepsPacketHandler;
import com.morecreepsrevival.morecreeps.common.networking.message.MessageChangeTamedEntityName;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiTamableEntityName extends GuiScreen {
    private static final int xSize = 256;
    private static final int ySize = 180;
    private final static ResourceLocation backgroundTexture = new ResourceLocation(MoreCreepsAndWeirdos.modid, "textures/gui/gui-screen.png");
    private final EntityCreepBase entity;
    private final String entityTypeName;
    private GuiTextField nameScreen;

    public GuiTamableEntityName(EntityCreepBase entityIn) {
        entity = entityIn;

        String s = EntityList.getEntityString(entityIn);
        if (s == null) {
            s = "generic";
        }

        s = I18n.translateToLocal("entity." + s + ".name").toUpperCase();
        StringBuilder spacedName = new StringBuilder();
        for (char c : s.toCharArray()) {
            spacedName.append(c).append(" ");
        }
        entityTypeName = spacedName.toString().trim();
    }

    @Override
    public void updateScreen() {
        nameScreen.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        buttonList.add(0, new GuiButton(0, width / 2 - 100, height / 4 + 42, "Save"));
        buttonList.add(1, new GuiButton(1, width / 2 - 100, height / 4 + 72, I18n.translateToLocal("gui.cancel")));
        nameScreen = new GuiTextField(1, fontRenderer, width / 2 - 100, height / 4 + 10, 200, 20);
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

        if (button.id == 0) {
            CreepsPacketHandler.INSTANCE.sendToServer(new MessageChangeTamedEntityName(entity.getEntityId(), nameScreen.getText()));
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
        drawDefaultBackground();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        mc.renderEngine.bindTexture(backgroundTexture);

        drawTexturedModalRect((width - xSize) / 2, (height - (ySize + 16)) / 2, 0, 0, xSize, ySize);

        drawCenteredString(fontRenderer, "N A M E   Y O U R   " + entityTypeName, width / 2, (height / 4 - 40) + 30, 0xffffff);

        nameScreen.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
