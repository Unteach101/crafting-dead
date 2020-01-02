package com.craftingdead.mod.client.gui.screen;

import org.lwjgl.glfw.GLFW;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.gui.transition.ITransition;
import com.craftingdead.mod.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

public class StartScreen extends ModScreen {

  private float fadeStartTime = 0L;

  public StartScreen() {
    // TODO add narrator.screen.start translation
    super(new TranslationTextComponent("narrator.screen.start"));
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);

    if (this.fadeStartTime == 0L) {
      this.fadeStartTime = Util.milliTime();
    }

    float fadePct = MathHelper.clamp((Util.milliTime() - this.fadeStartTime) / 1000.0F, 0.0F, 1.0F);

    double logoWidth = 1920;
    double logoHeight = 1080;
    double logoScale = RenderUtil.getScale(1920, 1080);
    logoWidth *= logoScale;
    logoHeight *= logoHeight;

    RenderSystem.enableBlend();
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, fadePct);
    RenderUtil.bind(new ResourceLocation(CraftingDead.ID, "textures/gui/craftingdead.png"));
    RenderUtil
        .drawTexturedRectangle(this.width / 2 - logoWidth / 2, this.height / 2 - logoHeight / 2,
            logoWidth, logoHeight);

    this.font
        .drawString(I18n.format("menu.start"),
            this.width / 2 - this.font.getStringWidth(I18n.format("menu.start")) / 2,
            this.height / 2 + this.height / 4, 0xFFFFFF | MathHelper.ceil(fadePct * 255.0F) << 24);

    RenderSystem.disableBlend();
  }

  @Override
  public boolean keyPressed(int key, int scanCode, int modifiers) {
    if (key == GLFW.GLFW_KEY_SPACE) {
      this.minecraft.displayGuiScreen(new ModMainMenuScreen());
    }
    return super.keyPressed(key, scanCode, modifiers);
  }

  @Override
  public ITransition getTransition() {
    return null;
  }
}
