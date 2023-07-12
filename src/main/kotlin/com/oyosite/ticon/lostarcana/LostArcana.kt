package com.oyosite.ticon.lostarcana

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.entity.ArcaneWorkbenchBlockEntity
import com.oyosite.ticon.lostarcana.block.entity.ArcaneWorkbenchScreenHandler
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import com.oyosite.ticon.lostarcana.recipe.StructureTransformationRecipe
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier

object LostArcana : ModInitializer {
    const val MODID = "lostarcana"

    val ARCANE_WORKBENCH_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("arcane_workbench"), FabricBlockEntityTypeBuilder.create(::ArcaneWorkbenchBlockEntity).build())
    val ARCANE_WORKBENCH_SCREEN_HANDLER: ScreenHandlerType<ArcaneWorkbenchScreenHandler> = Registry.register(Registries.SCREEN_HANDLER, id("arcane_workbench"), ScreenHandlerType<ArcaneWorkbenchScreenHandler>(::ArcaneWorkbenchScreenHandler, FeatureFlags.VANILLA_FEATURES))


    override fun onInitialize(){
        //println("ItemRegistry class: ${ItemRegistry.clazz.name}")
        BlockRegistry.registerAll()
        ItemRegistry.registerAll()
        //Registry.register(Registries.RECIPE_TYPE, id("structure_transformation"), StructureTransformationRecipe.Type)
    }

    fun id(id: String) = Identifier(if(id.contains(":")) id else "$MODID:$id")
}