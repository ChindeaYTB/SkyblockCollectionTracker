/*
 * This kotlin object is derived from the Skyhanni mod.
 */

package io.github.chindeaytb.collectiontracker.util

import io.github.chindeaytb.collectiontracker.ModInitialization
import io.github.chindeaytb.collectiontracker.api.serverapi.ServerStatus
import io.github.chindeaytb.collectiontracker.api.tokenapi.TokenManager
import io.github.chindeaytb.collectiontracker.autoupdate.UpdaterManager
import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass
import io.github.chindeaytb.collectiontracker.util.ServerUtils.serverStatus
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.CompletableFuture

object Hypixel {

    private val scoreboardTitlePattern = Regex("SK[YI]BLOCK(?: CO-OP| GUEST)?")

    private val formattingChars = "kmolnrKMOLNR".toSet()
    private val colorChars = "abcdefABCDEF0123456789".toSet()

    var server = false
    var skyblock = false
    private var playerLoaded = false

    private var logger: Logger = LogManager.getLogger(Hypixel::class.java)

    @SubscribeEvent
    fun onDisconnect(event: ClientDisconnectionFromServerEvent) {
        logger.info("Player has disconnected from the server.")
        server = false
        skyblock = false
        playerLoaded = false
        serverStatus = false
        TrackingHandlerClass.stopTracking()
    }

    private fun checkServer() {
        val mc = Minecraft.getMinecraft()
        val player = mc.thePlayer ?: return

        var hypixel = false

        player.clientBrand?.let {
            if (it.contains("hypixel", ignoreCase = true)) {
                hypixel = true
            }
        }

        server = hypixel
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!HypixelUtils.isInHypixel) {
            checkServer()
            if (HypixelUtils.isInHypixel && !playerLoaded) {
                loadPlayerData()
                if (playerLoaded) {
                    serverStatus = ServerStatus.checkServer()

                    setConfigForNewVersion()

                    if (!serverStatus) {
                        ChatUtils.sendMessage("§cThe API server is down at the moment. Sorry for the inconvenience.")

                    } else {
                        if (TokenManager.getToken() == null) {
                            TokenManager.fetchAndStoreToken()
                        }
                    }

                    logger.info("Update stream status: {}", ModInitialization.configManager.config!!.about.update)

                    if (ModInitialization.configManager.config!!.about.update != 0) {
                        CompletableFuture.runAsync {
                            RepoUtils.checkForUpdates(ModInitialization.configManager.config!!.about.update)
                        }

                        if (isUpdateAvailable()) {
                            logger.info("New version found: ${RepoUtils.latestVersion}")

                            ChatUtils.sendMessage(
                                ChatComponentText("§eA new version for SkyblockCollectionTracker found: §a${RepoUtils.latestVersion}§e. It will be downloaded after closing the game.")
                            )
                            logger.info("The new version will be downloaded after closing the client.")

                            UpdaterManager.update()
                            ModInitialization.configManager.config!!.about.hasCheckedUpdate = false

                        } else {
                            if(!ModInitialization.configManager.config!!.about.hasCheckedUpdate) {
                                ChatUtils.sendMessage("§aThe mod has been updated successfully.")
                                ModInitialization.configManager.config!!.about.hasCheckedUpdate = true
                            }

                            logger.info("No new version found.")
                        }
                    } else{
                        logger.info("Update stream is disabled.")
                    }
                }
            }
        }

        val inSkyblock = checkScoreboard()
        if (inSkyblock == skyblock) return
        skyblock = inSkyblock
    }

    private fun setConfigForNewVersion() {
        val scale = ModInitialization.configManager.config!!.overlay.overlayPosition.scale
        if(scale == 0.0f){
            ModInitialization.configManager.config!!.overlay.overlayPosition.setScaling(1.0f)
        }
    }

    private fun isUpdateAvailable(): Boolean {
        val currentVersion = ModInitialization.version
        val latestVersion = RepoUtils.latestVersion

        if (currentVersion == latestVersion) return false

        val currentParts = currentVersion.removePrefix("v").split("-", limit = 2)
        val latestParts = latestVersion.removePrefix("v").split("-", limit = 2)

        val currentNumericParts = currentParts[0].split(".").map { it.toIntOrNull() ?: 0 }
        val latestNumericParts = latestParts[0].split(".").map { it.toIntOrNull() ?: 0 }

        for (i in 0 until maxOf(currentNumericParts.size, latestNumericParts.size)) {
            val currentPart = currentNumericParts.getOrElse(i) { 0 }
            val latestPart = latestNumericParts.getOrElse(i) { 0 }
            if (currentPart < latestPart) return true
            if (currentPart > latestPart) return false
        }

        val currentPreRelease = currentParts.getOrNull(1)
        val latestPreRelease = latestParts.getOrNull(1)

        return when {
            currentPreRelease == null && latestPreRelease != null -> false
            currentPreRelease != null && latestPreRelease == null -> true
            currentPreRelease != null && latestPreRelease != null -> currentPreRelease < latestPreRelease
            else -> false
        }
    }

    private fun loadPlayerData() {
        val mc = Minecraft.getMinecraft()
        val player = mc.thePlayer
        if (player != null) {
            playerLoaded = true
            PlayerData.playerUUID
            PlayerData.playerName
        }
    }

    private fun checkScoreboard(): Boolean {
        val minecraft = Minecraft.getMinecraft()
        val world = minecraft.theWorld ?: return false

        val objective = world.scoreboard.getObjectiveInDisplaySlot(1) ?: return false
        val displayName = objective.displayName
        val scoreboardTitle = displayName.removeColor()
        return scoreboardTitlePattern.matches(scoreboardTitle)
    }

    // Method taken from Skyhanni mod
    private fun CharSequence.removeColor(keepFormatting: Boolean = false): String {
        // Glossary:
        // Formatting indicator: The '§' character indicating the beginning of a formatting sequence
        // Formatting code: The character following a formatting indicator which specifies what color or text style this sequence corresponds to
        // Formatting sequence: The combination of a formatting indicator and code that changes the color or format of a string

        // Flag for whether there is a text style (non-color and non-reset formatting code) currently being applied
        var isFormatted = false

        // Find the first formatting indicator
        var nextFormattingSequence = indexOf('§')

        // If this string does not contain any formatting indicators, just return this string directly
        if (nextFormattingSequence < 0) return this.toString()

        // Let's create a new string, and pre-allocate enough space to store this entire string
        val cleanedString = StringBuilder(this.length)

        // Read index stores the position in `this` which we have written up until now
        // a/k/a where we need to start reading from
        var readIndex = 0

        // As long as there still is a formatting indicator left in our string
        while (nextFormattingSequence >= 0) {

            // Write everything from the read index up to the next formatting indicator into our clean string
            cleanedString.append(this, readIndex, nextFormattingSequence)

            // Get the formatting code (note: this may not be a valid formatting code)
            val formattingCode = this.getOrNull(nextFormattingSequence + 1)

            // If the next formatting sequence's code indicates a non-color format and we should keep those
            if (keepFormatting && formattingCode in formattingChars) {
                // Update formatted flag based on whether this is a reset or a style format code
                isFormatted = formattingCode?.lowercaseChar() != 'r'

                // Set the readIndex to the formatting indicator, so that the next loop will start writing from that paragraph symbol
                readIndex = nextFormattingSequence
                // Find the next § symbol after the formatting sequence
                nextFormattingSequence = indexOf('§', startIndex = readIndex + 1)
            } else {
                // If this formatting sequence should be skipped (either a color code, or !keepFormatting or an incomplete formatting sequence without a code)

                // If being formatted and color code encountered, reset the current formatting code
                if (isFormatted && formattingCode in colorChars) {
                    cleanedString.append("§r")
                    isFormatted = false
                }

                // Set the readIndex to after this formatting sequence, so that the next loop will skip over it before writing the string
                readIndex = nextFormattingSequence + 2
                // Find the next § symbol after the formatting sequence
                nextFormattingSequence = indexOf('§', startIndex = readIndex)

                // If the next read would be out of bound, reset the readIndex to the very end of the string, resulting in a "" string to be appended
                readIndex = readIndex.coerceAtMost(this.length)
            }
        }
        // Finally, after the last formatting sequence was processed, copy over the last sequence of the string
        cleanedString.append(this, readIndex, this.length)

        // And turn the string builder into a string
        return cleanedString.toString()
    }
}