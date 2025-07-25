package io.github.chindeaytb.collectiontracker.config;

import com.google.gson.annotations.Expose;
import io.github.chindeaytb.collectiontracker.ModInitialization;
import io.github.chindeaytb.collectiontracker.config.categories.*;
import io.github.moulberry.moulconfig.Config;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.Category;

@SuppressWarnings("unused")
public class ModConfig extends Config {

    @Expose
    @Category(name = "About", desc = "")
    public About about = new About();
    @Expose
    @Category(name = "GUI", desc = "Change the location of GUI")
    public GUIConfig gui = new GUIConfig();
    @Expose
    @Category(name = "Overlay", desc = "Overlay settings")
    public Overlay overlay = new Overlay();
    @Expose
    @Category(name = "Bazaar", desc = "Toggle bazaar prices")
    public Bazaar bazaar = new Bazaar();
    @Expose
    @Category(name = "Mining", desc = "Mining related settings")
    @Accordion
    public Mining mining = new Mining();

    @Override
    public String getTitle() {
        String modName = "SkyblockCollectionTracker";
        return modName + " by §3Chindea_YTB§r, config by §5Moulberry §rand §5nea89";
    }

    @Override
    public void saveNow() {
        ModInitialization.configManager.save();
    }
}
