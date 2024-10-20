package io.github.chindeaytb.collectiontracker.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.chindeaytb.collectiontracker.init.ModInitialization.Companion.logger
import io.github.chindeaytb.collectiontracker.config.error.ConfigError
import io.github.chindeaytb.collectiontracker.config.misc.version.VersionManager
import io.github.moulberry.moulconfig.gui.MoulConfigEditor
import io.github.moulberry.moulconfig.gui.GuiScreenElementWrapper
import io.github.moulberry.moulconfig.observer.PropertyTypeAdapterFactory
import io.github.moulberry.moulconfig.processor.BuiltinMoulConfigGuis
import io.github.moulberry.moulconfig.processor.ConfigProcessorDriver
import io.github.moulberry.moulconfig.processor.MoulConfigProcessor
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*

class ConfigManager {

    companion object {

        val gson: Gson = GsonBuilder().setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeSpecialFloatingPointValues()
            .registerTypeAdapterFactory(PropertyTypeAdapterFactory())
            .registerTypeAdapter(UUID::class.java, object : TypeAdapter<UUID>() {
                override fun write(out: JsonWriter, value: UUID) {
                    out.value(value.toString())
                }

                override fun read(reader: JsonReader): UUID {
                    return UUID.fromString(reader.nextString())
                }
            }.nullSafe())
            .enableComplexMapKeySerialization()
            .create()
    }

    private var config: ModConfig? = null

    private var configDirectory = File("config/sct")
    private var configFile: File
    private var lastSaveTime = 0L

    private lateinit var processor: MoulConfigProcessor<ModConfig>
    private val editor by lazy { MoulConfigEditor(processor) }

    init {

        logger.info("Loading config...")

        configDirectory.mkdirs()

        configFile = File(configDirectory, "config.json")

        if (configFile.isFile) {
            println("Trying to load the config")
            tryReadConfig()
        }

        if (config == null) {
            println("Creating a clean config.")
            config = ModConfig()
        }

        val config = config!!
        processor = MoulConfigProcessor(config)
        BuiltinMoulConfigGuis.addProcessors(processor)
        VersionManager.injectConfigProcessor(processor)
        ConfigProcessorDriver.processConfig(
            config.javaClass,
            config,
            processor
        )

        Runtime.getRuntime().addShutdownHook(Thread {
            save()
        })
    }

    private fun tryReadConfig() {
        try {
            val inputStreamReader = InputStreamReader(FileInputStream(configFile), StandardCharsets.UTF_8)
            val bufferedReader = BufferedReader(inputStreamReader)

            val builder = StringBuilder()
            for (line in bufferedReader.lines()) {
                builder.append(line)
                builder.append("\n")
            }
            config = gson.fromJson(builder.toString(), ModConfig::class.java)
        } catch (e: Exception) {
            throw ConfigError("Could not load config", e)
        }
    }

    fun save() {
        lastSaveTime = System.currentTimeMillis()
        val config = config ?: error("Can not save null config.")

        try {
            configDirectory.mkdirs()
            val unit = configDirectory.resolve("config.json.write")
            unit.createNewFile()
            BufferedWriter(OutputStreamWriter(FileOutputStream(unit), StandardCharsets.UTF_8)).use { writer ->
                writer.write(gson.toJson(config))
            }
            // Perform move — which is atomic, unlike writing — after writing is done.
            Files.move(
                unit.toPath(),
                configFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            )
        } catch (e: IOException) {
            throw ConfigError("Could not save config", e)
        }
    }


    fun openConfigGui() {
        screenToOpen = GuiScreenElementWrapper(editor)
    }

    private var screenToOpen: GuiScreen? = null

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        Minecraft.getMinecraft().thePlayer ?: return
        if (screenToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(screenToOpen)
            screenToOpen = null
        }
        if (System.currentTimeMillis() > lastSaveTime + 60_000) {
            save()
        }
    }
}