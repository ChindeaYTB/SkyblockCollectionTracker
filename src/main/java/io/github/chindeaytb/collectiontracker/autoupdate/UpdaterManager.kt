package io.github.chindeaytb.collectiontracker.autoupdate

import io.github.chindeaytb.UpdateContext
import io.github.chindeaytb.UpdateSetup
import io.github.chindeaytb.UpdateTarget
import io.github.chindeaytb.collectiontracker.ModInitialization
import java.util.concurrent.CompletableFuture

object UpdaterManager {

    private var activePromise: CompletableFuture<*>? = null
    private var potentialUpdate: UpdateSetup? = null

    private val context = UpdateContext(
        "sct",
        ModInitialization.version,
        setUpdateStream(),
        ModInitialization.MODID,
        UpdateTarget.deleteAndSaveInTheSameFolder(UpdaterManager::class.java)
    )

    init{
        context.cleanup()
    }

    private fun setUpdateStream(): String{
        val currentStream = ModInitialization.configManager.config?.about?.update
        return when(currentStream){
            1 -> "release"
            2 -> "beta"
            else -> "none"
        }
    }

    fun update() {
        activePromise = context.checkUpdate().thenAcceptAsync {
            potentialUpdate = it
            queueUpdate()
        }
    }

    private fun queueUpdate(){
        activePromise = CompletableFuture.supplyAsync{
            potentialUpdate!!.prepareUpdate()
        }.thenAcceptAsync {
            potentialUpdate!!.executeUpdate()
        }
    }
}