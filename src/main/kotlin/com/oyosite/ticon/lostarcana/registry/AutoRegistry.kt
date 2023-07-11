package com.oyosite.ticon.lostarcana.registry

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.lang.reflect.Field
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers

open class AutoRegistry<T>(val registryOf: Class<T>, private val registerAllFunction: AutoRegistry<T>.()->Unit) {
    constructor(registryOf: Class<T>, registry: Registry<T>, handleSpecial: (Pair<Identifier, T>)->Unit = {}): this(registryOf, { retrieveObjects(this).forEach { registerSingleToRegistry(registry, it); handleSpecial(it) } })


    var namespace: String = LostArcana.MODID

    protected val clazz get() = this::class.java

    fun registerAll() = this.registerAllFunction()

    companion object{
        private fun <T> registerSingleToRegistry(registry: Registry<T>, single: Pair<Identifier, T>){ Registry.register(registry, single.first, single.second); println(single.first) }
        @Suppress("unchecked_cast")
        private fun <T> retrieveObjects(autoRegistry: AutoRegistry<T>): List<Pair<Identifier,T>>{
            val fields = autoRegistry.clazz.kotlin.declaredMemberProperties.filter {
                try {
                    autoRegistry.registryOf.isInstance((it as KProperty1<AutoRegistry<T>, T>).get(autoRegistry))
                } catch (e: Exception) { e.printStackTrace();false }
            }.map { it as KProperty1<AutoRegistry<T>, T> }
            /*val fields: List<Field> = autoRegistry.clazz.declaredFields.filter {
                try{ autoRegistry.registryOf.isInstance(it.get(autoRegistry)) } catch (e: Exception) {e.printStackTrace();false}
            }*/
            val otpt: MutableList<Pair<Identifier, T>> = mutableListOf()
            for(field in fields){
                try {
                    val obj: T = field.get(autoRegistry)
                    if(obj is RegisterableObject) otpt.add(obj.registryId to obj)
                    else otpt.add(LostArcana.id(field.name.lowercase(Locale.getDefault())) to obj)

                } catch (e: Exception) {e.printStackTrace();continue}
            }
            return otpt
        }
    }
}