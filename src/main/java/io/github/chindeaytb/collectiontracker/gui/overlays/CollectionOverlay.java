package io.github.chindeaytb.collectiontracker.gui.overlays;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isTracking;
import static io.github.chindeaytb.collectiontracker.util.TextUtils.updateStats;

public class CollectionOverlay {

    private static boolean visible = true;

    public static boolean isVisible() {
        return visible;
    }

    public static void setVisible(boolean visibility) {
        visible = visibility;
    }

    public static void stopTracking() {
        updateStats();
        setVisible(false);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (!isTracking || !visible || ModInitialization.configManager.getConfig() == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRendererObj;

        RenderUtils.INSTANCE.drawRect(fontRenderer);
    }
}
