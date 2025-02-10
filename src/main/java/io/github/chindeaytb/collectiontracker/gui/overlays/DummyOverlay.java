package io.github.chindeaytb.collectiontracker.gui.overlays;

import io.github.chindeaytb.collectiontracker.mixins.AccessorGuiContainer;
import io.github.chindeaytb.collectiontracker.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class DummyOverlay extends GuiScreen {

    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;
    private GuiContainer oldScreen = null;

    public DummyOverlay(GuiContainer oldScreen) {
        this.oldScreen = oldScreen;
    }

    @Override
    public void initGui() {
        if (CollectionOverlay.isVisible()) {
            CollectionOverlay.setVisible(false);
        }
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (CollectionOverlay.isVisible()) {
            return;
        }

        drawDefaultBackground();
        if (oldScreen != null) {
            AccessorGuiContainer accessor = (AccessorGuiContainer) oldScreen;
            accessor.invokeDrawGuiContainerBackgroundLayer_sct(partialTicks, -1, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.disableLighting();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        RenderUtils.INSTANCE.drawRectDummy(fontRenderer);

        if (dragging) {
            RenderUtils.INSTANCE.getPosition().setPosition(mouseX - dragOffsetX, mouseY - dragOffsetY);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            float scaleChange = 0.1f;
            float currentScale = RenderUtils.INSTANCE.getPosition().getScale();

            if (scrollAmount > 0) {
                RenderUtils.INSTANCE.getPosition().setScaling(Math.min(10.0f, currentScale + scaleChange));
            } else {
                RenderUtils.INSTANCE.getPosition().setScaling(Math.max(0.1f, currentScale - scaleChange));
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isMouseOverOverlay(mouseX, mouseY)) {
                dragging = true;
                dragOffsetX = mouseX - RenderUtils.INSTANCE.getPosition().getX();
                dragOffsetY = mouseY - RenderUtils.INSTANCE.getPosition().getY();
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    private boolean isMouseOverOverlay(int mouseX, int mouseY) {
        return mouseX >= RenderUtils.INSTANCE.getPosition().getX() && mouseX <= RenderUtils.INSTANCE.getPosition().getX() + RenderUtils.INSTANCE.getMaxWidth() + 2 * RenderUtils.PADDING && mouseY >= RenderUtils.INSTANCE.getPosition().getY() && mouseY <= RenderUtils.INSTANCE.getPosition().getY() + RenderUtils.INSTANCE.getTextHeight() + 2 * RenderUtils.PADDING;
    }

    @Override
    public void onGuiClosed() {

        CollectionOverlay.setVisible(true);
    }

}