package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot

class ArcaneWorkbenchScreenHandler(syncId: Int, playerInventory: PlayerInventory, val inventory: Inventory): ScreenHandler(LostArcana.ARCANE_WORKBENCH_SCREEN_HANDLER, syncId) {
    constructor(syncId: Int, playerInventory: PlayerInventory): this(syncId, playerInventory, SimpleInventory(15))

    init {
        checkSize(inventory, 15)
        inventory.onOpen(playerInventory.player)
        //addSlot(CraftingResultSlot(playerInventory.player, this.input, this.result, 0, 124, 35))

        //Vis crystal slots
        listOf(10 to 10, 86 to 10, 89 to 35, 86 to 60, 10 to 60, 7 to 35).forEachIndexed{ i, coords ->
            addSlot(FilteredSlot(inventory, 9+i, coords.first, coords.second, i::testCrystalInSlot))
        }
        //Crafting slots
        for(row in 0..2) for(col in 0..2)
            addSlot(Slot(inventory, col+row*3, 30 + col * 18, 17 + row * 18))
        //Inventory slots
        for(row in 0..2) for(col in 0..8)
            addSlot(Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18))
        //Hotbar slots
        for(slot in 0..8)
            addSlot(Slot(playerInventory, slot, 8 + slot * 18, 142))
    }




    override fun quickMove(player: PlayerEntity?, invSlot: Int): ItemStack {
        var newStack = ItemStack.EMPTY
        val slot: Slot? = slots.getOrNull(invSlot)
        if (slot != null && slot.hasStack()) {
            val originalStack: ItemStack = slot.stack
            newStack = originalStack.copy()
            if (invSlot < inventory.size()) {
                if (!insertItem(originalStack, inventory.size(), slots.size, true)) return ItemStack.EMPTY
            } else if (!insertItem(originalStack, 0, inventory.size(), false)) return ItemStack.EMPTY

            if (originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return newStack!!
    }

    override fun canUse(player: PlayerEntity): Boolean = true

}