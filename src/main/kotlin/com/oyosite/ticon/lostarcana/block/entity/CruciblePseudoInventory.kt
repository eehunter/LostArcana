package com.oyosite.ticon.lostarcana.block.entity

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class CruciblePseudoInventory(val be: CrucibleBlockEntity): Inventory {
    var lastUser: PlayerEntity? = null
    var activeStack: ItemStack = ItemStack.EMPTY

    override fun clear() {}

    override fun size(): Int = 0

    override fun isEmpty(): Boolean = true

    override fun getStack(slot: Int): ItemStack = ItemStack.EMPTY

    override fun removeStack(slot: Int, amount: Int): ItemStack = ItemStack.EMPTY

    override fun removeStack(slot: Int): ItemStack = ItemStack.EMPTY

    override fun setStack(slot: Int, stack: ItemStack?) {}

    override fun markDirty() {}

    override fun canPlayerUse(player: PlayerEntity?): Boolean = true
}