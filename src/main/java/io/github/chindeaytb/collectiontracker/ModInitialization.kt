package io.github.chindeaytb.collectiontracker

import io.github.chindeaytb.collectiontracker.commands.*
import io.github.chindeaytb.collectiontracker.config.ConfigManager
import io.github.chindeaytb.collectiontracker.util.ModulesLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(modid = ModInitialization.MODID, clientSideOnly = true, useMetadata = true, version = "sctVersion")
class ModInitialization {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {

        loadModule(this)
        ModulesLoader.modules.forEach { loadModule(it) }

        logger.info("Skyblock Collection Tracker pre-initialization complete.")
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        // Register the event bus
        configManager = ConfigManager()

        MinecraftForge.EVENT_BUS.register(configManager)
        logger.info("ConfigManager initialized.")

        // Register commands
        val commandHelper = CommandHelper()
        val startTracker = StartTracker()
        val stopTracker = StopTracker()
        val pauseTracker = PauseTracker()
        val resumeTracker = ResumeTracker()
        val guiMenu = GuiMenu()
        ClientCommandHandler.instance.registerCommand(SCT_Commands(commandHelper, startTracker, stopTracker, pauseTracker, resumeTracker, guiMenu))

        logger.info("SkyblockCollectionTracker initialized.")

        loadedClasses.clear()
    }

    private val loadedClasses = mutableSetOf<String>()

    private fun loadModule(obj: Any) {
        if (!loadedClasses.add(obj.javaClass.name)) throw IllegalStateException("Module ${obj.javaClass.name} is already loaded")
        modules.add(obj)
        MinecraftForge.EVENT_BUS.register(obj)
    }

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (screenToOpen != null) {
            screenTicks++

            // Delay the screen opening by 5 ticks
            if (screenTicks == 5) {
                // Close any open screen
                Minecraft.getMinecraft().thePlayer?.closeScreen()

                // Open the specified screen
                Minecraft.getMinecraft().displayGuiScreen(screenToOpen)

                // Reset the counter and screen to open
                screenTicks = 0
                screenToOpen = null
            }
        }
    }

    companion object {
        lateinit var configManager: ConfigManager
        const val MODID = "skyblockcollectiontracker"

        val logger: Logger = LogManager.getLogger(ModInitialization::class.java)

        @JvmStatic
        val version: String
            get() = Loader.instance().indexedModList[MODID]!!.version

        var screenToOpen: GuiScreen? = null
        val modules: MutableList<Any> = ArrayList()
        private var screenTicks = 0
    }
}
