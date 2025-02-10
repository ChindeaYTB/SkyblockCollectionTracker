package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.api.serverapi.ServerStatus;
import io.github.chindeaytb.collectiontracker.collections.ValidCollectionsManager;
import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass;
import io.github.chindeaytb.collectiontracker.util.ChatUtils;
import io.github.chindeaytb.collectiontracker.util.HypixelUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isPaused;
import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isTracking;
import static io.github.chindeaytb.collectiontracker.util.TextUtils.formatCollectionName;

public class StartTracker extends CommandBase {

    public static String collection = "";

    @Override
    public String getCommandName() {
        return "sct";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sct <command> [arguments]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (!HypixelUtils.isOnSkyblock()) {
                ChatUtils.INSTANCE.sendMessage("§cYou must be on Hypixel Skyblock to use this command!");
                return;
            }
            try {
                if (!ServerStatus.checkServer()) {
                    ChatUtils.INSTANCE.sendMessage("§cYou can't use any commands for this mod at the moment.");
                    return;
                }

                if (args[0].equalsIgnoreCase("track")) {
                    if (args.length < 2) {
                        ChatUtils.INSTANCE.sendMessage("Use: /sct track <collection>");
                        return;
                    }

                    StringBuilder keyBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        keyBuilder.append(args[i]);
                        if (i < args.length - 1) {
                            keyBuilder.append(" ");
                        }
                    }

                    if (!isTracking && !isPaused) {
                        collection = keyBuilder.toString().trim().toLowerCase();
                        if (!ValidCollectionsManager.isValidCollection(collection)) {
                            ChatUtils.INSTANCE.sendMessage("§4" + formatCollectionName(collection) + " collection is not supported!");
                        } else {
                            TrackingHandlerClass.startTracking(sender);
                        }
                    } else {
                        ChatUtils.INSTANCE.sendMessage("§cAlready tracking a collection.");
                    }
                }
            } catch (Exception e) {
                ChatUtils.INSTANCE.sendMessage("§cAn error occurred while processing the command.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}

