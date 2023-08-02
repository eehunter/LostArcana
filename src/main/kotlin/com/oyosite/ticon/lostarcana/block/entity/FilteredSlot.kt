package com.oyosite.ticon.lostarcana.block.entity

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

open class FilteredSlot(inventory: Inventory, index: Int, x: Int, y: Int, val predicate: (ItemStack)->Boolean, val markDirtyCallback: ()->Unit = {}): Slot(inventory, index, x, y) {

    override fun canInsert(stack: ItemStack): Boolean = predicate(stack)



    class Delegated(inventory: Inventory, index: Int, x: Int, y: Int, markDirtyCallback: ()->Unit = {}): FilteredSlot(inventory, index, x, y, {inventory.isValid(index, it)}, markDirtyCallback)


}