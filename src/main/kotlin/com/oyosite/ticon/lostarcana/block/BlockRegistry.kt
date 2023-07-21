package com.oyosite.ticon.lostarcana.block

import com.oyosite.ticon.lostarcana.block.BlockRegistry.BLOCK_ITEMS
import com.oyosite.ticon.lostarcana.block.BlockRegistry.itemSettings
import com.oyosite.ticon.lostarcana.registry.AutoRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object BlockRegistry: AutoRegistry<Block>(Block::class.java, Registries.BLOCK, { Registry.register(Registries.ITEM, it.first, BlockItem(it.second, it.second.itemSettings).apply{ BLOCK_ITEMS[it.second] = this })}) {
    val BLOCK_ITEMS = mutableMapOf<Block, Item>()

    val ARCANE_WORKBENCH = ArcaneWorkbenchBlock()
    val CRUCIBLE = CrucibleBlock()

    val NITOR = NitorBlock()

    val GROWING_VIS_CRYSTAL = GrowingVisCrystalBlock()



    val Block.asItem get() = BLOCK_ITEMS[this]!!

    fun Block(settings: FabricBlockSettings.()->Unit) = Block(FabricBlockSettings.create().apply(settings))
    val Block.itemSettings get() = (if(this is BlockWithItemSettings)settings else FabricItemSettings())
}