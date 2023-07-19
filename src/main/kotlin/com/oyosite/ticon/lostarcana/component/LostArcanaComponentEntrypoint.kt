package com.oyosite.ticon.lostarcana.component

import com.oyosite.ticon.lostarcana.LostArcana
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import net.minecraft.entity.ItemEntity

object LostArcanaComponentEntrypoint: ChunkComponentInitializer, EntityComponentInitializer {

    val CRAFTING_RESULT_MARKER = ComponentRegistryV3.INSTANCE.getOrCreate(LostArcana.id("crafting_result_marker"), BooleanComponent::class.java)
    val CHUNK_VIS = ComponentRegistryV3.INSTANCE.getOrCreate(LostArcana.id("chunk_vis"), VisAreaComponent::class.java)

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerFor(ItemEntity::class.java, CRAFTING_RESULT_MARKER) { CraftingResultComponent(false) }
    }

    override fun registerChunkComponentFactories(registry: ChunkComponentFactoryRegistry) {
        registry.register(CHUNK_VIS, ::VisChunkImpl)
    }
}