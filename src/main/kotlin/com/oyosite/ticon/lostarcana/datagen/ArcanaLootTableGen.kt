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
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.function.LootFunctionConsumingBuilder
import net.minecraft.loot.function.SetNbtLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.nbt.NbtCompound
import net.minecraft.predicate.StatePredicate
import net.minecraft.util.DyeColor

class ArcanaLootTableGen(dataOutput: FabricDataOutput): FabricBlockLootTableProvider(dataOutput) {
    override fun generate() = BlockRegistry.run{
        val nitorEntryBuilder = ItemEntry.builder(NITOR).apply(DyeColor.values().map(DyeColor::getId)){SetNbtLootFunction.builder(
            NbtCompound().apply { put("nitor", NbtCompound().apply { putString("color", DyeColor.byId(it).name.lowercase()) }) }
        ).conditionally(BlockStatePropertyLootCondition.builder(NITOR).properties(StatePredicate.Builder.create().exactMatch(NitorBlock.COLOR, it)))}

        /*val neb = BlockRegistry.NITOR.entry {
            apply(DyeColor.values().map(DyeColor::getId)){SetNbtLootFunction.builder(
                NbtCompound().apply { put("nitor", NbtCompound().apply { putString("color", DyeColor.byId(it).name.lowercase()) }) }
            ).conditionally(BlockStatePropertyLootCondition.builder(BlockRegistry.NITOR).properties(StatePredicate.Builder.create().exactMatch(NitorBlock.COLOR, it)))}
        }*/

        //addDrop(BlockRegistry.NITOR, LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(applyExplosionDecay(BlockRegistry.NITOR, nitorEntryBuilder))))
        NITOR + {
            pool { rolls(ConstantLootNumberProvider.create(1.0f)).with(applyExplosionDecay(NITOR, nitorEntryBuilder)) }
        }

        +ARCANE_WORKBENCH
        +CRUCIBLE
    }

    operator fun Block.unaryPlus() = addDrop(this)
    operator fun Block.plus(body: LootTable.Builder.()->Unit) = addDrop(this, LootTable.builder().apply(body))
    fun LootTable.Builder.pool(body: LootPool.Builder.()->Unit) = pool(LootPool.builder().apply(body))
    //fun Block.constantNumberLootProviderWithExplosionDecay(count: Number, entryBuilder: LeafEntry.Builder<*>) = ConstantLootNumberProvider.create(count.toFloat()).with(applyExplosionDecay(this, entryBuilder))
    infix fun Block.entry(body: LootFunctionConsumingBuilder<*>.()->Unit) = ItemEntry.builder(this).apply(body)
}