package io.github.chindeaytb.collectiontracker.commands;

import io.github.chindeaytb.collectiontracker.util.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SCT_Commands extends CommandBase {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CommandHelper commandHelper;
    private final StartTracker startTracker;
    private final StopTracker stopTracker;
    private final PauseTracker pauseTracker;
    private final ResumeTracker resumeTracker;
    private final GuiMenu guiMenu;

    public SCT_Commands(CommandHelper commandHelper, StartTracker startTracker, StopTracker stopTracker, PauseTracker pauseTracker, ResumeTracker resumeTracker, GuiMenu guiMenu) {
        this.commandHelper = commandHelper;
        this.startTracker = startTracker;
        this.stopTracker = stopTracker;
        this.pauseTracker = pauseTracker;
        this.resumeTracker = resumeTracker;
        this.guiMenu = guiMenu;
    }

    @Override
    public String getCommandName() {
        return "sct";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sct <command> [arguments]";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "help", "track", "stop", "pause", "resume");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("track")) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "cocoa beans", "carrot", "cactus", "raw chicken", "sugar cane", "pumpkin", "wheat", "seeds", "mushroom", "raw rabbit", "nether wart", "mutton", "melon", "potato", "leather", "porkchop", "feather", "lapis lazuli", "redstone", "umber", "coal", "mycelium", "end stone", "quartz", "sand", "iron", "amber", "topaz", "sapphire", "amethyst", "jasper", "ruby", "jade", "opal", "aquamarine", "citrine", "onyx", "peridot" , "tungsten", "obsidian", "diamond", "cobblestone", "glowstone", "gold", "gravel", "hard stone", "mithril", "emerald", "red sand", "ice", "glacite", "sulphur", "netherrack", "ender pearl", "chili pepper", "slimeball", "magma cream", "ghast tear", "gunpowder", "rotten flesh", "spider eye", "bone", "blaze rod", "string", "acacia", "spruce", "jungle", "birch", "oak", "dark oak", "lily pad", "prismarine shard", "ink sac", "raw fish", "pufferfish", "clownfish", "raw salmon", "magmafish", "prismarine crystals", "clay", "sponge", "wilted berberis", "living metal heart", "caducous stem", "agaricus cap", "hemovibe", "half-eaten carrot", "timite");
        }
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        executor.submit(() -> {
            if (args.length == 0) {
                guiMenu.processCommand(sender, args);
                return;
            }

            switch (args[0].toLowerCase()) {
                case "help":
                    commandHelper.processCommand(sender, args);
                    break;
                case "track":
                    startTracker.processCommand(sender, args);
                    break;
                case "stop":
                    stopTracker.processCommand(sender, args);
                    break;
                case "pause":
                    pauseTracker.processCommand(sender, args);
                    break;
                case "resume":
                    resumeTracker.processCommand(sender, args);
                    break;
                default:
                    ChatUtils.INSTANCE.sendMessage("Unknown command. Use /sct help.");
                    break;
            }
        });
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
