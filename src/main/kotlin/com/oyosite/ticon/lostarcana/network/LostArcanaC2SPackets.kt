package com.oyosite.ticon.lostarcana.network

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.util.Identifier
import kotlin.reflect.KProperty

val UNLOCK_THAUMONOMICON_PAGE by Auto



private object Auto{
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): Identifier = LostArcana.id(prop.name.lowercase())
}