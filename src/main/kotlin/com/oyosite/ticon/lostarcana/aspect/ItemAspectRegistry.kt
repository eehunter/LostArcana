package com.oyosite.ticon.lostarcana.aspect

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.util.Identifier

object ItemAspectRegistry: Map<Identifier, List<Pair<Aspect,Int>>> {
    private val ASPECTS = mutableMapOf<String, List<Pair<Aspect,Int>>>()
    override fun get(key: Identifier): List<Pair<Aspect,Int>>? = ASPECTS[key.toString()]
    override val entries: Set<Map.Entry<Identifier, List<Pair<Aspect,Int>>>>
        get() = ASPECTS.mapKeys{LostArcana.id(it.key)}.entries
    override val keys: Set<Identifier>
        get() = ASPECTS.keys.map(LostArcana::id).toSet()
    override val size: Int
        get() = ASPECTS.size
    override val values: Collection<List<Pair<Aspect,Int>>>
        get() = ASPECTS.values

    override fun isEmpty(): Boolean = ASPECTS.isEmpty()

    override fun containsValue(value: List<Pair<Aspect,Int>>): Boolean = ASPECTS.containsValue(value)

    override fun containsKey(key: Identifier): Boolean = ASPECTS.containsKey(key.toString())


}