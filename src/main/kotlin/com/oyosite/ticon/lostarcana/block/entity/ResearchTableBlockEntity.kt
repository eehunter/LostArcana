package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

class ResearchTableBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(LostArcana.RESEARCH_TABLE_BLOCK_ENTITY, pos, state), NamedScreenHandlerFactory {

    val inv = Inv(null, this)

    override fun toInitialChunkDataNbt(): NbtCompound = NbtCompound().apply(this::writeNbt)

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)

    override fun readNbt(nbt: NbtCompound) {
        Inventories.readNbt(nbt, inv.items)
    }

    override fun writeNbt(nbt: NbtCompound) {
        Inventories.writeNbt(nbt, inv.items)
    }





    /**
     * Slot 0 = Scribing Tools
     * Slot 1 = Paper
     * */
    class Inv(val player: PlayerEntity?, val blockEntity: ResearchTableBlockEntity? = null): Inventory{
        val items = DefaultedList.ofSize(2, ItemStack.EMPTY)

        override fun clear() = items.clear()

        override fun size(): Int = 2

        override fun isEmpty(): Boolean = items.all(ItemStack::isEmpty)

        override fun getStack(slot: Int): ItemStack = items[slot]

        override fun removeStack(slot: Int, amount: Int): ItemStack {
            val result = Inventories.splitStack(items, slot, amount)
            if (!result.isEmpty) markDirty()
            return result
        }

        override fun removeStack(slot: Int): ItemStack {
            markDirty()
            return Inventories.removeStack(items, slot)
        }

        override fun setStack(slot: Int, stack: ItemStack) {
            items[slot] = stack
        }

        override fun markDirty() {
            blockEntity?.markDirty()
        }

        override fun canPlayerUse(player: PlayerEntity?): Boolean = true

    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler = ResearchTableScreenHandler(syncId, playerInventory, this.inv)

    override fun getDisplayName(): Text = Text.empty()
}