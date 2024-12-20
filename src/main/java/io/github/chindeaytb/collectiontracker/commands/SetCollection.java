package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.api.serverapi.ServerStatus;
import io.github.chindeaytb.collectiontracker.collections.ValidCollectionsManager;
import io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass;
import io.github.chindeaytb.collectiontracker.util.HypixelUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.chindeaytb.collectiontracker.tracker.TrackingHandlerClass.isTracking;

public class SetCollection extends CommandBase {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
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
                sender.addChatMessage(new ChatComponentText("§cYou must be on Hypixel Skyblock to use this command!"));
                return;
            }

            executor.submit(() -> {
                try {
                    if (!ServerStatus.checkServer()) {
                        sender.addChatMessage(new ChatComponentText("§cYou can't use any commands for this mod at the moment."));
                        return;
                    }

                    if (args[0].equalsIgnoreCase("track")) {
                        if (args.length < 2) {
                            sender.addChatMessage(new ChatComponentText("Use: /sct track <collection>"));
                            return;
                        }

                        StringBuilder keyBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            keyBuilder.append(args[i]);
                            if (i < args.length - 1) {
                                keyBuilder.append(" ");
                            }
                        }

                        if (!isTracking) {
                            collection = keyBuilder.toString().trim().toLowerCase();
                            if (!ValidCollectionsManager.isValidCollection(collection)) {
                                sender.addChatMessage(new ChatComponentText("§4Invalid collection!"));
                            } else {
                                TrackingHandlerClass.startTracking(sender);
                            }
                        } else {
                            sender.addChatMessage(new ChatComponentText("§cAlready tracking a collection."));
                        }
                    }
                } catch (Exception e) {
                    sender.addChatMessage(new ChatComponentText("§cAn error occurred while processing the command."));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}

