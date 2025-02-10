package io.github.chindeaytb.collectiontracker.collections;

import java.util.HashMap;
import java.util.Map;

public class ValidCollectionsManager {

    private static final Map<String, Boolean> validCollections = new HashMap<>();

    static {
        // Farming
        validCollections.put("cocoa beans", true);
        validCollections.put("carrot", true);
        validCollections.put("cactus", true);
        validCollections.put("raw chicken", true);
        validCollections.put("sugar cane", true);
        validCollections.put("pumpkin", true);
        validCollections.put("wheat", true);
        validCollections.put("seeds", true);
        validCollections.put("mushroom", true);
        validCollections.put("raw rabbit", true);
        validCollections.put("nether wart", true);
        validCollections.put("mutton", true);
        validCollections.put("melon", true);
        validCollections.put("potato", true);
        validCollections.put("leather", true);
        validCollections.put("porkchop", true);
        validCollections.put("feather", true);

        // Mining
        validCollections.put("lapis lazuli", true);
        validCollections.put("redstone", true);
        validCollections.put("umber", true);
        validCollections.put("coal", true);
        validCollections.put("mycelium", true);
        validCollections.put("end stone", true);
        validCollections.put("quartz", true);
        validCollections.put("sand", true);
        validCollections.put("iron", true);
        validCollections.put("amber", true);
        validCollections.put("topaz", true);
        validCollections.put("sapphire", true);
        validCollections.put("amethyst", true);
        validCollections.put("jasper", true);
        validCollections.put("ruby", true);
        validCollections.put("jade", true);
        validCollections.put("opal", true);
        validCollections.put("aquamarine", true);
        validCollections.put("citrine", true);
        validCollections.put("onyx", true);
        validCollections.put("peridot", true);
        validCollections.put("tungsten", true);
        validCollections.put("obsidian", true);
        validCollections.put("diamond", true);
        validCollections.put("cobblestone", true);
        validCollections.put("glowstone", true);
        validCollections.put("gold", true);
        validCollections.put("gravel", true);
        validCollections.put("hard stone", true);
        validCollections.put("mithril", true);
        validCollections.put("emerald", true);
        validCollections.put("red sand", true);
        validCollections.put("ice", true);
        validCollections.put("glacite", true);
        validCollections.put("sulphur", true);
        validCollections.put("netherrack", true);

        // Combat
        validCollections.put("ender pearl", true);
        validCollections.put("chili pepper", true);
        validCollections.put("slimeball", true);
        validCollections.put("magma cream", true);
        validCollections.put("ghast tear", true);
        validCollections.put("gunpowder", true);
        validCollections.put("rotten flesh", true);
        validCollections.put("spider eye", true);
        validCollections.put("bone", true);
        validCollections.put("blaze rod", true);
        validCollections.put("string", true);

        // Foraging
        validCollections.put("acacia", true);
        validCollections.put("spruce", true);
        validCollections.put("jungle", true);
        validCollections.put("birch", true);
        validCollections.put("oak", true);
        validCollections.put("dark oak", true);

        // Fishing
        validCollections.put("lily pad", true);
        validCollections.put("prismarine shard", true);
        validCollections.put("ink sac", true);
        validCollections.put("raw fish", true);
        validCollections.put("pufferfish", true);
        validCollections.put("clownfish", true);
        validCollections.put("raw salmon", true);
        validCollections.put("magmafish", true);
        validCollections.put("prismarine crystals", true);
        validCollections.put("clay", true);
        validCollections.put("sponge", true);

        // Rift
        validCollections.put("wilted berberis", true);
        validCollections.put("living metal heart", true);
        validCollections.put("caducous stem", true);
        validCollections.put("agaricus cap", true);
        validCollections.put("hemovibe", true);
        validCollections.put("half-eaten carrot", true);
        validCollections.put("timite", true);
    }

    public static boolean isValidCollection(String collectionName) {
        return validCollections.getOrDefault(collectionName, false);
    }
}