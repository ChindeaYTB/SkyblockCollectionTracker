package io.github.chindeaytb.collectiontracker.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RepoUtils {
    public static String MODRINTH_URL = "https://modrinth.com/mod/sct/version/";
    private static final String API_URL = "https://api.github.com/repos/ChindeaYTB/SkyblockCollectionTracker/releases";
    private static final Logger logger = LogManager.getLogger(RepoUtils.class);
    public static String latestVersion;
    public static String latestStableVersion;
    public static String latestBetaVersion;

    public static void checkForUpdates(int update) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            if (connection.getResponseCode() == 200) {
                JsonArray releases = getJsonArray(connection);

                latestStableVersion = null;
                latestBetaVersion = null;

                for (JsonElement element : releases) {
                    JsonObject release = element.getAsJsonObject();
                    boolean isPreRelease = release.get("prerelease").getAsBoolean();
                    String versionTag = release.get("tag_name").getAsString();

                    if (!isPreRelease && (latestStableVersion == null || isNewerVersion(versionTag, latestStableVersion))) {
                        latestStableVersion = versionTag;
                    }
                    if (isPreRelease && (latestBetaVersion == null || isNewerVersion(versionTag, latestBetaVersion))) {
                        latestBetaVersion = versionTag;
                    }
                }

                if (update == 1 && latestStableVersion != null) {
                    latestVersion = latestStableVersion;
                } else if (update == 2) {
                    if (latestStableVersion != null && latestBetaVersion != null) {

                        latestVersion = isNewerVersion(latestStableVersion, latestBetaVersion) ? latestStableVersion : latestBetaVersion;
                    } else if (latestStableVersion != null) {
                        latestVersion = latestStableVersion;
                    } else if (latestBetaVersion != null) {
                        latestVersion = latestBetaVersion;
                    } else {
                        latestVersion = null;
                    }
                } else {
                    latestVersion = null;
                }

                if (latestVersion != null) {
                    MODRINTH_URL += latestVersion;
                }
            } else {
                logger.error("Failed to check for updates. HTTP Response Code: {}", connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("An error occurred while checking for updates", e);
        }
    }

    private static JsonArray getJsonArray(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response.toString());
        return jsonElement.getAsJsonArray();
    }

    public static boolean isNewerVersion(String candidateVersion, String currentVersion) {
        String candidateClean = candidateVersion.replace("v", "");
        String currentClean = currentVersion.replace("v", "");

        String[] candidateParts = candidateClean.split("-beta");
        String[] currentParts = currentClean.split("-beta");

        String candidateMain = candidateParts[0];
        String currentMain = currentParts[0];

        Integer candidateBeta = candidateParts.length > 1 ? Integer.parseInt(candidateParts[1]) : null;
        Integer currentBeta = currentParts.length > 1 ? Integer.parseInt(currentParts[1]) : null;

        String[] candidateNums = candidateMain.split("\\.");
        String[] currentNums = currentMain.split("\\.");

        int length = Math.max(candidateNums.length, currentNums.length);
        for (int i = 0; i < length; i++) {
            int candidateNum = i < candidateNums.length ? Integer.parseInt(candidateNums[i]) : 0;
            int currentNum = i < currentNums.length ? Integer.parseInt(currentNums[i]) : 0;
            if (candidateNum > currentNum) {
                return true;
            } else if (candidateNum < currentNum) {
                return false;
            }
        }

        if (candidateBeta == null && currentBeta != null) {
            return true;
        } else if (candidateBeta != null && currentBeta == null) {
            return false;
        } else if (candidateBeta != null && currentBeta != null) {
            return candidateBeta > currentBeta;
        }

        return false;
    }

}
