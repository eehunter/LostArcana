package com.oyosite.ticon.lostarcana.item

enum class ResearchNotesType(val itemGetter: ()->ResearchNotesItem) {
    THEORY({ ItemRegistry.THEORY_NOTES }), OBSERVATION({ ItemRegistry.OBSERVATION_NOTES }), CELESTIAL({ ItemRegistry.CELESTIAL_NOTES });

    val item get() = itemGetter()
}