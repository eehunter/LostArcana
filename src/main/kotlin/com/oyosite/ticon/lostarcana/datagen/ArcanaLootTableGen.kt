package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.NitorBlock
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.BlockStatePropertyLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.SetNbtLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.nbt.NbtCompound
import net.minecraft.predicate.StatePredicate
import net.minecraft.util.DyeColor

class ArcanaLootTableGen(val dataOutput: FabricDataOutput): FabricBlockLootTableProvider(dataOutput) {
    override fun generate() {
        val nitorEntryBuilder = ItemEntry.builder(BlockRegistry.NITOR).apply(DyeColor.values().map(DyeColor::getId)){SetNbtLootFunction.builder(
            NbtCompound().apply { put("nitor", NbtCompound().apply { putString("color", DyeColor.byId(it).name.lowercase()) }) }
        ).conditionally(BlockStatePropertyLootCondition.builder(BlockRegistry.NITOR).properties(StatePredicate.Builder.create().exactMatch(NitorBlock.COLOR, it)))}

        addDrop(BlockRegistry.NITOR, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(applyExplosionDecay(BlockRegistry.NITOR, nitorEntryBuilder))))

    }

    operator fun Block.plus(body: LootTable.Builder.()->Unit) = addDrop(this, LootTable.builder().apply(body))
    fun LootTable.Builder.pool(body: LootPool.Builder.()->Unit) = pool(LootPool.builder().apply(body))

}