pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://maven.architectury.dev/")
        maven("https://maven.fabricmc.net")
        maven("https://maven.minecraftforge.net/")
        maven("https://repo.spongepowered.org/maven/")
        maven("https://repo.sk1er.club/repository/maven-releases/")
        maven("https://maven.teamresourceful.com/repository/maven-private/")  // Blossom
        maven("https://jitpack.io") {
            content {
                includeGroupByRegex("(com|io)\\.github\\..*")
            }
        }
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "gg.essential.loom" -> useModule("gg.essential:architectury-loom:${requested.version}")
            }
        }
    }
}

// Plugins block for managing plugin versions and applying them
plugins {
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("net.kyori.blossom") version "2.1.0" apply false
    id("gg.essential.loom") version "1.9.29" apply false
    kotlin("jvm") version "2.0.0" apply false
}

// Set the root project name
rootProject.name = "SkyblockCollectionTracker"