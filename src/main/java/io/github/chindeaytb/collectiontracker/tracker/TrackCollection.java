package io.github.chindeaytb.collectiontracker.tracker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.github.chindeaytb.collectiontracker.collections.prices.NPCPrice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringReader;

import static io.github.chindeaytb.collectiontracker.commands.StartTracker.collection;
import static io.github.chindeaytb.collectiontracker.util.TextUtils.updateStats;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.getUptimeInSeconds;

public class TrackCollection {

    private static final Logger logger = LogManager.getLogger(TrackCollection.class);

    public static float previousCollection = -1;
    public static float sessionStartCollection = 0;
    public static boolean afk = false;

    public static float collectionAmount;
    public static float collectionPerHour;
    public static float collectionMade;
    public static float moneyPerHour;

    public static void displayCollection(String jsonResponse) {
        try (JsonReader reader = new JsonReader(new StringReader(jsonResponse))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(reader);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (!jsonObject.entrySet().isEmpty()) {
                    String key = jsonObject.entrySet().iterator().next().getKey();
                    float currentCollection = jsonObject.get(key).getAsFloat();

                    if (previousCollection > 0) {
                        if (currentCollection == previousCollection) {
                            afk = true;
                            if (TrackingHandlerClass.isTracking) {
                                TrackingHandlerClass.stopTracking();
                            }
                            return;
                        }
                    } else {
                        sessionStartCollection = currentCollection;
                    }

                    long uptime = getUptimeInSeconds();
                    float collectedSinceStart = currentCollection - sessionStartCollection;
                    float npcMoney = NPCPrice.notRiftCollection(collection) ? collectedSinceStart * NPCPrice.getNpcPrice(collection) : 0;

                    collectionAmount = (float) Math.floor(currentCollection);
                    collectionPerHour = uptime > 0 ? (float) Math.floor((collectedSinceStart / uptime) * 3600) : 0;
                    collectionMade = (float) Math.floor(collectedSinceStart);
                    moneyPerHour = uptime > 0 ? (float) Math.floor(npcMoney / (uptime / 3600.0f)) : 0;
                    updateStats();

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
}

