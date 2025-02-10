package io.github.chindeaytb.collectiontracker.config.categories;

import com.google.gson.annotations.Expose;
import io.github.chindeaytb.collectiontracker.config.version.VersionDisplay;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigEditorInfoText;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

@SuppressWarnings("unused")
public class About {
    @ConfigOption(name = "Current Version", desc = "This is the SkyblockCollectionTracker version you are currently running")
    @VersionDisplay
    public transient Void currentVersion = null;

    @Expose
    @ConfigOption(name = "§aInfo", desc = "This mod is meant to track (almost) any collection that exists. Helps a lot if you are lazy to check your stats.")
    @ConfigEditorInfoText()
    public boolean info = true;

    @Expose
    @ConfigOption(
            name = "Update Stream",
            desc = "Choose between getting notification about latest or latest beta versions."
    )
    @ConfigEditorDropdown(
            values = {"None", "Full releases", "Beta releases"}
    )
    public int update = 0;

    @Expose
    public boolean hasCheckedUpdate = false;
}
