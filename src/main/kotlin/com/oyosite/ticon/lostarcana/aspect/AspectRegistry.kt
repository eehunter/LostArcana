package com.oyosite.ticon.lostarcana.aspect

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.util.Identifier

object AspectRegistry {
    val ASPECTS = mutableMapOf(*mutableListOf(
        "aer"      to 0xf6f775,
        "ignis"    to 0xff6608,
        "perditio" to 0x4d5049,
        "terra"    to 0x66ee08,
        "aqua"     to 0x4ce6ff,
        "ordo"     to 0xd8d6fb,
    ).map{LostArcana.id(it.first) to Aspect(LostArcana.id(it.first), it.second)}.toTypedArray())


    init{
        A("motus", 0xc8befa, "aer", "ordo")

    }

    fun A(name: String, color: Int, component1: String, component2: String){
        val c1 = ASPECTS[LostArcana.id(component1)]!!
        val c2 = ASPECTS[LostArcana.id(component2)]!!
        ASPECTS[LostArcana.id(name)] = Aspect(LostArcana.id(name), color, c1 to c2)
    }

    fun registerAspect(aspect: Aspect) = ASPECTS.put(aspect.id, aspect)
}