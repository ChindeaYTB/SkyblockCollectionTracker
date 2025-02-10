package io.github.chindeaytb.collectiontracker.config.categories;

import com.google.gson.annotations.Expose;
import io.github.chindeaytb.collectiontracker.config.core.ConfigLink;
import io.github.chindeaytb.collectiontracker.config.core.Position;
import io.github.moulberry.moulconfig.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Overlay {

    @ConfigOption(
            name = "Stats Overlay",
            desc = ""
    )
    @ConfigEditorAccordion(id = 0)
    public boolean statsOverlay = true;

    @Expose
    @ConfigOption(
            name = "Overlay Text",
            desc = "Drag text to change the appearance of the Overlay."
    )
    @ConfigEditorDraggableList(
            exampleText = {
                    "§aGold collection §f> 200.000M",
                    "§aGold collection (session) §f> 10.000M",
                    "§aColl/h §f> Calculating...",
                    "§a$/h (NPC/Bazaar) §f> 100k/h",
            }
    )
    @ConfigAccordionId(id = 0)
    public List<Integer> statsText = new ArrayList<>(Arrays.asList(0, 1, 2, 3));

    @Expose
    @ConfigLink(owner = Overlay.class, field = "statsOverlay")
    public Position overlayPosition = new Position(4, 150);

    @Expose
    @ConfigOption(
            name = "Overlay Text Color",
            desc = "Toggle this to enable color-coded text."
    )
    @ConfigEditorBoolean
    public boolean overlayTextColor = false;
}
