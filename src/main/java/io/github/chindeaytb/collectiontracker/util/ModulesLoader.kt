/*
 * This kotlin object is derived from the NotEnoughUpdates mod.
 */

package io.github.chindeaytb.collectiontracker.util

import io.github.chindeaytb.collectiontracker.autoupdate.UpdaterManager
import io.github.chindeaytb.collectiontracker.gui.overlays.CollectionOverlay

object ModulesLoader {
    val modules: List<Any> = buildList {
        add(CollectionOverlay())
        add(Hypixel)
        add(ServerUtils)
        add(UpdaterManager)
    }
}