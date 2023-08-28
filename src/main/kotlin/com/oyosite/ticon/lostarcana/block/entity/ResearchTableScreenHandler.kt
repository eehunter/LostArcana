package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.item.ScribingToolsItem
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot

class ResearchTableScreenHandler(syncId: Int, playerInventory: PlayerInventory, val inv: ResearchTableBlockEntity.Inv = ResearchTableBlockEntity.Inv(playerInventory.player)) : ScreenHandler(LostArcana.RESEARCH_TABLE_SCREEN_HANDLER, syncId, ) {

    private val player = playerInventory.player

    init {
        inv.onOpen(player)
        addSlot(FilteredSlot(inv, 0, 6, 6, {it.item is ScribingToolsItem}))
        addSlot(FilteredSlot(inv, 1, 154, 6, {it.item == Items.PAPER}))

        //Inventory slots
        for(row in 0..2) for(col in 0..8)
            addSlot(Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18))
        //Hotbar slots
        for(slot in 0..8)
            addSlot(Slot(playerInventory, slot, 8 + slot * 18, 142))

    }

    override fun quickMove(player: PlayerEntity, invSlot: Int): ItemStack {
        var newStack = ItemStack.EMPTY
        val slot: Slot? = slots.getOrNull(invSlot)

        if (slot != null && slot.hasStack()) {
            val originalStack: ItemStack = slot.stack
            newStack = originalStack.copy()
            if (invSlot < inv.size()) {
                if (!insertItem(originalStack, inv.size(), slots.size, true)) return ItemStack.EMPTY
            } else if (!insertItem(originalStack, 0, inv.size(), false)) return ItemStack.EMPTY

            if (originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return newStack
    }

    override fun canUse(player: PlayerEntity?): Boolean = true
}