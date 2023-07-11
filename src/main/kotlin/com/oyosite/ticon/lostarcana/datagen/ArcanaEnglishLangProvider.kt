package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class ArcanaEnglishLangProvider(generator: FabricDataOutput): FabricLanguageProvider(generator, "en_us") {
    override fun generateTranslations(tb: TranslationBuilder) {
        tb.add(BlockRegistry.ARCANE_WORKBENCH, "Arcane Workbench")
        tb.add(ItemRegistry.SALIS_MUNDIS, "Salis Mundis")
        tb.add(ItemRegistry.VIS_CRYSTAL, "Vis Crystal")
    }
}