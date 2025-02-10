package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.util.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;


public class CommandHelper extends CommandBase {
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
        if (args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(sender);
        } else {
            ChatUtils.INSTANCE.sendMessage("Unknown command. Use /sct help.");
        }
    }

    private void sendHelpMessage(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("                        §f<§eInfo§f>"));
        sender.addChatMessage(new ChatComponentText("§a◆/sct => Opens the gui."));
        sender.addChatMessage(new ChatComponentText("§a◆/sct track => Tracks your Skyblock collection."));
        sender.addChatMessage(new ChatComponentText("§a◆/sct stop => Stops tracking."));
        sender.addChatMessage(new ChatComponentText("§a◆/sct pause => Pauses tracking."));
        sender.addChatMessage(new ChatComponentText("§a◆/sct resume => Resumes tracking."));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
