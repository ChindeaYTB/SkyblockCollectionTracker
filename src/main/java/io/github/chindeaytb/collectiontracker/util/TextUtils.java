package io.github.chindeaytb.collectiontracker.util;

import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.config.ModConfig;
import io.github.chindeaytb.collectiontracker.config.categories.Overlay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.chindeaytb.collectiontracker.commands.StartTracker.collection;
import static io.github.chindeaytb.collectiontracker.tracker.TrackCollection.*;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.*;

public class TextUtils {

    static List<String> overlayLines = new ArrayList<>();

    public static void updateStats() {
        ModConfig config = Objects.requireNonNull(ModInitialization.configManager.getConfig());
        Overlay overlay = config.overlay;

        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }

        if (!isTracking) {
            overlayLines.clear();
            return;
        }

        overlayLines.clear();
        for (int id : overlay.statsText) {
            switch (id) {
                case 0:
                    if (collection != null && collectionAmount >= 0) {
                        overlayLines.add(formatCollectionName(collection) + " collection: " + formatNumber(collectionAmount));
                    }
                    break;
                case 1:
                    if (collection != null) {
                        if (collectionMade > 0) {
                            overlayLines.add(formatCollectionName(collection) + " collection (session): " + formatNumber(collectionMade));
                        } else {
                            overlayLines.add(formatCollectionName(collection) + " collection (session): Calculating...");
                        }
                    }
                    break;
                case 2:
                    if (collectionPerHour > 0) {
                        overlayLines.add("Coll/h: " + formatNumber(collectionPerHour));
                    } else {
                        overlayLines.add("Coll/h: Calculating...");
                    }
                    break;
                case 3:
                    if (moneyPerHour > 0) {
                        overlayLines.add("$/h (NPC): " + formatNumber(moneyPerHour));
                    } else {
                        overlayLines.add("$/h (NPC): Calculating...");
                    }
                    break;
            }
        }
    }

    public static @NotNull List<String> getStrings() {
        updateStats();
        return overlayLines;
    }

    public static String uptimeString() {
        return ("Uptime: " + getUptime());
    }

    public static String formatCollectionName(String collection) {
        String[] words = collection.split("\\s+");
        StringBuilder formattedName = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                formattedName.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
            } else {
                formattedName.append(" ").append(word.toLowerCase());
            }
        }
        return formattedName.toString();
    }

    private static String formatNumber(float number) {
        number = (float) Math.floor(number);

        if (number < 1_000) {
            return String.valueOf((int) number);
        } else if (number < 1_000_000) {
            return String.format("%.2fk", number / 1_000.0);
        } else if (number < 1_000_000_000) {
            return String.format("%.2fM", number / 1_000_000.0);
        } else {
            return String.format("%.2fB", number / 1_000_000_000.0);
        }
    }
}
