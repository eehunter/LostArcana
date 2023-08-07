plugins {
    kotlin("jvm")
    id("fabric-loom")
    `maven-publish`
    java
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    maven { setUrl("https://maven.ladysnake.org/releases");name = "Ladysnake Mods" }
    maven { setUrl("https://maven.jamieswhiteshirt.com/libs-release"); content { includeGroup("com.jamieswhiteshirt") }}
    maven { setUrl("https://jitpack.io") }
    maven { setUrl("https://maven.shedaniel.me/") }
    maven { setUrl("https://maven.terraformersmc.com/releases/") }
    maven { setUrl("https://maven.blamejared.com") }
    maven { setUrl("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/");name = "GeckoLib" }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    listOf("base", "entity", "chunk").forEach{
        modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-$it:${property("cardinal_components_version")}")
        include("dev.onyxstudios.cardinal-components-api:cardinal-components-$it:${property("cardinal_components_version")}")
    }

    modImplementation("com.github.apace100:calio:v${property("calio_version")}")

    modImplementation("com.jamieswhiteshirt:reach-entity-attributes:${property("reach_version")}")

    modApi("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_config_version")}") {
        exclude("net.fabricmc.fabric-api")
    }

    modApi("com.terraformersmc:modmenu:${property("modmenu_version")}")

    modImplementation("vazkii.patchouli:Patchouli:${property("patchouli_version")}")

    modImplementation("software.bernie.geckolib:geckolib-fabric-1.20:4.2")

    //modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${property("cardinal_components_version")}")
    //modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-chunk:${property("cardinal_components_version")}")

    //include("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${property("cardinal_components_version")}")
    //include("dev.onyxstudios.cardinal-components-api:cardinal-components-chunk:${property("cardinal_components_version")}")
}

tasks {

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(remapJar) {
                    builtBy(remapJar)
                }
                artifact(kotlinSourcesJar) {
                    builtBy(remapSourcesJar)
                }
            }
        }

        // select the repositories you want to publish to
        repositories {
            // uncomment to publish to the local maven
            // mavenLocal()
        }
    }

    compileJava {
        targetCompatibility = "18"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "18"
    }

}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

loom{

    runs {

        create("Data Generation"){
            client()
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.strict_validation")

            ideConfigGenerated(true)
            runDir = "build/datagen"


        }
    }
}

sourceSets{
    main{
        resources{
            srcDirs("src/main/generated", "src/main/resources")
        }
    }
}

// configure the maven publication
