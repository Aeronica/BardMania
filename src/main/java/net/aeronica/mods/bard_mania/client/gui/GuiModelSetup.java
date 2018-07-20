/*
 * Copyright 2018 Paul Boese a.k.a Aeronica
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.aeronica.mods.bard_mania.client.gui;

import net.aeronica.mods.bard_mania.Reference;
import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiModelSetup extends GuiScreen
{
    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_player_background.png");
    private String TITLE = I18n.format("gui.bard_mania.gui_model_setup.title");

    GuiSliderMX sliderPlayerRotYaw;
    GuiSliderMX translateX;
    GuiSliderMX translateY;
    GuiSliderMX translateZ;
    GuiSliderMX rotateX;
    GuiSliderMX rotateY;
    GuiSliderMX rotateZ;
    GuiSliderMX scale;

    GuiButton equip;
    GuiButton remove;
    GuiButton print;

    Instrument inst;
    float playerYaw;

    public GuiModelSetup() {/* NOP */}

    @Override
    public void initGui()
    {
        super.initGui();
        playerYaw = 1f;
        inst = ((ItemInstrument) mc.player.getHeldItemMainhand().getItem()).getInstrument();
        int y = 20;
        int x = 10;
        int w = 120;
        sliderPlayerRotYaw = new GuiSliderMX(0, x, y, w, 20, "Player Yaw", playerYaw, -180f, 180f, 1f);
        translateX = new GuiSliderMX(1, x, y += 22, w, 20, "Trans X", inst.general.translationXYZ[0], -0.5f, 0.5f, 0.01f);
        translateY = new GuiSliderMX(2, x, y += 22, w, 20, "Trans Y", inst.general.translationXYZ[1], -0.5f, 0.5f, 0.01f);
        translateZ = new GuiSliderMX(3, x, y += 22, w, 20, "Trans Z", inst.general.translationXYZ[2], -0.5f, 0.5f, 0.01f);
        rotateX = new GuiSliderMX(4, x, y += 22, w, 20, "Rotate X", inst.general.rotationXYZ[0], -180f, 180f, 1f);
        rotateY = new GuiSliderMX(5, x, y += 22, w, 20, "Rotate Y", inst.general.rotationXYZ[1], -180f, 180f, 1f);
        rotateZ = new GuiSliderMX(6, x, y += 22, w, 20, "Rotate Z", inst.general.rotationXYZ[2], -180f, 180f, 1f);
        scale = new GuiSliderMX(7, x, y += 22, w, 20, "Scale", inst.general.scaleXYZ[0], 0f, 1.5f, 0.01f);

        equip = new GuiButton(8, x, y += 22, w, 20, "Equip");
        remove = new GuiButton(9, x += 102, y, w, 20, "Remove");
        print = new GuiButton(10, x += 102, y, w, 20, "Print");

        buttonList.add(sliderPlayerRotYaw);
        buttonList.add(translateX);
        buttonList.add(translateY);
        buttonList.add(translateZ);
        buttonList.add(rotateX);
        buttonList.add(rotateY);
        buttonList.add(rotateZ);
        buttonList.add(scale);
        buttonList.add(equip);
        buttonList.add(remove);
        buttonList.add(print);
    }

    @Override
    public void onGuiClosed() {super.onGuiClosed();}

    @Override
    public void updateScreen()
    {
        inst.general.translationXYZ[0] = translateX.getValue();
        inst.general.translationXYZ[1] = translateY.getValue();
        inst.general.translationXYZ[2] = translateZ.getValue();
        inst.general.rotationXYZ[0] = rotateX.getValue();
        inst.general.rotationXYZ[1] = rotateY.getValue();
        inst.general.rotationXYZ[2] = rotateZ.getValue();
        inst.general.scaleXYZ[0] = scale.getValue();
        inst.general.scaleXYZ[1] = scale.getValue();
        inst.general.scaleXYZ[2] = scale.getValue();
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        //drawDefaultBackground();
        drawGuiPlayerBackgroundLayer(partialTicks, mouseX, mouseY, (width + 26 * 8 - 100) / 2, 20);
        int posX = (this.width - getFontRenderer().getStringWidth(TITLE)) / 2;
        int posY = 10;
        getFontRenderer().drawStringWithShadow(TITLE, posX, posY, 0xD3D3D3);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void drawGuiPlayerBackgroundLayer(float partialTicks, int mouseX, int mouseY, int xIn, int yIn)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(GUI_BACKGROUND);
        int i = xIn + 100 / 2;
        int j = 120 + 8;
        //drawBox(xIn, yIn, 100, 120);
        drawTexturedModalRect(xIn, yIn, 0, 0, 100, 120);
        drawEntityOnScreen(i, j, 50, (float) xIn - xIn + 10, (float) yIn - yIn - 10, this.mc.player, sliderPlayerRotYaw.getValue());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch (button.id)
        {
            case 8:
                BardActionHelper.setInstrumentEquipped(mc.player);
                ActionManager.equipAction(mc.player);
                break;
            case 9:
                BardActionHelper.setInstrumentRemoved(mc.player);
                ActionManager.removeAction(mc.player);
                break;
            case 10:
                System.out.printf("\"translationXYZ\": [%1.2f, %1.2f, %1.2f],\n", inst.general.translationXYZ[0], inst.general.translationXYZ[1], inst.general.translationXYZ[2]);
                System.out.printf("\"rotationXYZ\": [%1.2f, %1.2f, %1.2f],\n", inst.general.rotationXYZ[0], inst.general.rotationXYZ[1], inst.general.rotationXYZ[2]);
                System.out.printf("\"scaleXYZ\": [%1.2f, %1.2f, %1.2f]\n", inst.general.scaleXYZ[0], inst.general.scaleXYZ[1], inst.general.scaleXYZ[2]);
                break;
            default:
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
    }

    @Override
    public boolean doesGuiPauseGame() {return false;}

    private FontRenderer getFontRenderer() {return mc.fontRenderer;}

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent, float playerYaw)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = playerYaw;
        ent.rotationYaw = playerYaw;
        ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

}
