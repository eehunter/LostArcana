package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos


class ArcaneWorkbenchBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(TODO(), pos, state), Inventory {
    private val items = DefaultedList.ofSize(15, ItemStack.EMPTY)
    override fun clear() = items.clear()

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean {
        for (i in 0 until size()) {
            val stack = getStack(i)
            if (!stack.isEmpty) return false
        }
        return true
    }

    override fun getStack(slot: Int): ItemStack = items[slot]

    override fun removeStack(slot: Int, count: Int): ItemStack {
        val result = Inventories.splitStack(items, slot, count)
        if (!result.isEmpty) markDirty()
        return result
    }

    override fun removeStack(slot: Int): ItemStack {
        return Inventories.removeStack(items, slot)
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        items.set(slot, stack)
        if (stack.count > stack.maxCount) {
            stack.count = stack.maxCount
        }
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean = true

    private val primalAspects = listOf("aer", "ignis", "perditio", "terra", "aqua", "ordo")
    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        if(slot in 10..15){
            if(stack.item != ItemRegistry.VIS_CRYSTAL) return false
            if(stack.getSubNbt("vis") == null) return false
            if(stack.getSubNbt("vis")!!.getString("aspect") == primalAspects[slot-10]) return true
        }
        return true
    }

    override fun readNbt(nbt: NbtCompound) {
        Inventories.readNbt(nbt, items)
    }

    override fun writeNbt(nbt: NbtCompound) {
        Inventories.writeNbt(nbt, items)
    }
}