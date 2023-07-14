package com.oyosite.ticon.lostarcana.aspect

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.util.Identifier

object AspectRegistry: Map<Identifier, Aspect> {
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
        val c1 = ASPECTS[LostArcana.id(component1)]?: return
        val c2 = ASPECTS[LostArcana.id(component2)]?: return
        ASPECTS[LostArcana.id(name)] = Aspect(LostArcana.id(name), color, c1 to c2)
    }

    fun registerAspect(aspect: Aspect) = ASPECTS.put(aspect.id, aspect)
    override val entries: Set<Map.Entry<Identifier, Aspect>>
        get() = ASPECTS.entries
    override val keys: Set<Identifier>
        get() = ASPECTS.keys
    override val size: Int
        get() = ASPECTS.size
    override val values: Collection<Aspect>
        get() = ASPECTS.values

    override fun isEmpty(): Boolean = ASPECTS.isEmpty()

    override operator fun get(key: Identifier): Aspect? = ASPECTS[key]

    override fun containsValue(value: Aspect): Boolean = ASPECTS.containsValue(value)

    override fun containsKey(key: Identifier): Boolean = ASPECTS.containsKey(key)
}