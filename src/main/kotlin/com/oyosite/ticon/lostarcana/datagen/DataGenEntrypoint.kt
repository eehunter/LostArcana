package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.LostArcana
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object DataGenEntrypoint: DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(dataGen: FabricDataGenerator) {
        val pack = dataGen.createPack()//createBuiltinResourcePack(LostArcana.id("lostarcana"))
        pack.addProvider(::ArcanaModelGenerator)
        pack.addProvider(::ArcanaEnglishLangProvider)
        pack.addProvider(::ArcanaRecipeGen)
        pack.addProvider(::ArcanaLootTableGen)
    }

}