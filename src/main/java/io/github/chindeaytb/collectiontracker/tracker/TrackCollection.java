package io.github.chindeaytb.collectiontracker.tracker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.github.chindeaytb.collectiontracker.collections.CollectionsManager;
import io.github.chindeaytb.collectiontracker.collections.prices.NPCPrice;
import io.github.chindeaytb.collectiontracker.gui.overlays.CollectionOverlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringReader;

import static io.github.chindeaytb.collectiontracker.commands.SetCollection.collection;

public class TrackCollection {

    private static final Logger logger = LogManager.getLogger(TrackCollection.class);

    public static float previousCollection = -1;
    public static float sessionStartCollection = 0;

    public static void displayCollection(String jsonResponse) {
        try (JsonReader reader = new JsonReader(new StringReader(jsonResponse))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                String HypixelCollection = CollectionsManager.toHypixelCollection(collection);
                if (jsonObject.has(HypixelCollection)) {
                    float currentCollection = jsonObject.get(HypixelCollection).getAsLong();
                    String formattedCollection = formatCollectionName(collection);

                    String collectionPerHour;
                    String collectionMade;
                    String npcMoneyPerHour;
                    float npcMoney = 0;

                    if (previousCollection > 0) {
                        if (currentCollection == previousCollection) {
                            collectionPerHour = "Paused";
                            collectionMade = "Paused";
                            npcMoneyPerHour = "Paused";
                            TrackingHandlerClass.pauseTracking();
                        } else {
                            float collectedSinceStart = currentCollection - sessionStartCollection;

                            if (NPCPrice.notRiftCollection(collection)) {
                                npcMoney = collectedSinceStart * NPCPrice.getNpcPrice(collection);
                            }

                            long elapsedSeconds = (System.currentTimeMillis() - CollectionOverlay.startTime) / 1000;

                            if (elapsedSeconds > 0) {
                                double averagePerSecond = collectedSinceStart / (double) elapsedSeconds;
                                double projectedPerHour = averagePerSecond * 3600;

                                collectionPerHour = formatNumber((float) projectedPerHour);
                                collectionMade = formatNumber(collectedSinceStart);

                                npcMoneyPerHour = formatNumber((float) (npcMoney / (elapsedSeconds / 3600.0)));

                            } else {
                                collectionPerHour = "Calculating...";
                                collectionMade = "Calculating...";
                                npcMoneyPerHour = "Calculating...";
                            }
                        }
                    } else {
                        sessionStartCollection = currentCollection;
                        collectionPerHour = "Calculating...";
                        collectionMade = "Calculating...";
                        npcMoneyPerHour = "Calculating...";

                    }

                    if(!NPCPrice.notRiftCollection(collection)) {
                        npcMoneyPerHour = null;
                    }

                    logger.info("New collection value: {}", currentCollection);
                    logger.info("Previous collection value: {}", previousCollection);

                    CollectionOverlay.updateCollectionData(formattedCollection, formatNumber(currentCollection), collectionPerHour, collectionMade, npcMoneyPerHour);

                    previousCollection = currentCollection;
                } else {
                    logger.warn("Collection '{}' not found in the response.", collection);
                }
            } else {
                logger.error("Invalid JSON response: {}", jsonResponse);
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing the collection data", e);
        }
    }

    private static String formatCollectionName(String collection) {
        return collection.substring(0, 1).toUpperCase() + collection.substring(1).toLowerCase();
    }

    private static String formatNumber(float number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1_000_000) {
            return String.format("%.3fk", number / 1000.0);
        } else if (number < 1_000_000_000) {
            return String.format("%.3fM", number / 1_000_000.0);
        } else {
            return String.format("%.3fB", number / 1_000_000_000.0);
        }
    }
}

