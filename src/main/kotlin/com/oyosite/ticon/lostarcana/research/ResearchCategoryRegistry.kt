package com.oyosite.ticon.lostarcana.research

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.item.ResearchNotesType
import com.oyosite.ticon.lostarcana.item.ResearchNotesType.*
import net.minecraft.util.Identifier

object ResearchCategoryRegistry {
    val CATEGORIES = mutableMapOf<Identifier, ResearchCategory>()

    fun refreshCategories(){
        CATEGORIES.clear()
        "fundamentals"[THEORY, OBSERVATION]
        "artifice"[THEORY, OBSERVATION]
        "infusion"[THEORY, OBSERVATION]
        "alchemy"[THEORY, OBSERVATION]
        "golemancy"[THEORY, OBSERVATION]
        "auramancy"[THEORY, OBSERVATION]
        "solar"[CELESTIAL]
        listOf("first", "second", "third", "fourth").forEach { "${it}_quadrant"[CELESTIAL] }
        listOf("full", "waning_gibbous", "third_quarter", "waning_crescent", "new", "waxing_crescent", "first_quarter", "waxing_gibbous").forEach { "lunar_$it"[CELESTIAL] }


    }

    //I continue to misuse kotlin syntax for my own amusement.
    operator fun String.get(vararg type: ResearchNotesType) = CATEGORIES.put(LostArcana.id(this), ResearchCategory(LostArcana.id(this), type.asList()))
}