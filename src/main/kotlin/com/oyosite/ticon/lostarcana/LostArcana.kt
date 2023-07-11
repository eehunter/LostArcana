package com.oyosite.ticon.lostarcana

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import com.oyosite.ticon.lostarcana.recipe.StructureTransformationRecipe
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object LostArcana : ModInitializer {
    const val MODID = "lostarcana"
    override fun onInitialize(){
        //println("ItemRegistry class: ${ItemRegistry.clazz.name}")
        BlockRegistry.registerAll()
        ItemRegistry.registerAll()
        //Registry.register(Registries.RECIPE_TYPE, id("structure_transformation"), StructureTransformationRecipe.Type)
    }

    fun id(id: String) = Identifier(if(id.contains(":")) id else "$MODID:$id")
}