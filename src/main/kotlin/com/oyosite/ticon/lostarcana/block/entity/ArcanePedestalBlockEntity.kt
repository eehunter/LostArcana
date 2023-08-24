package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.InfusionDataProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import kotlin.math.min

class ArcanePedestalBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(LostArcana.ARCANE_PEDESTAL_BLOCK_ENTITY, pos, state), Inventory, InfusionDataProvider {

    var stack = ItemStack.EMPTY

    override fun clear() { stack = ItemStack.EMPTY }

    override fun size(): Int = 1

    override fun isEmpty(): Boolean = stack.isEmpty

    override fun getStack(slot: Int): ItemStack = if(slot==0)stack else ItemStack.EMPTY

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        if(slot!=0)return ItemStack.EMPTY
        return stack.copy().apply{ count = min(count, amount); stack.decrement(amount) }
    }

    override fun removeStack(slot: Int): ItemStack = if(slot==0)stack.also{stack = ItemStack.EMPTY} else ItemStack.EMPTY

    override fun setStack(slot: Int, stack: ItemStack?) {
        if(slot!=0)return
        this.stack = stack?: ItemStack.EMPTY
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean = true


    override fun writeNbt(nbt: NbtCompound) {
        Inventories.writeNbt(nbt, DefaultedList.ofSize(1, ItemStack.EMPTY).apply{set(0, stack)})
    }

    override fun readNbt(nbt: NbtCompound) {
        val dummyStacks = DefaultedList.ofSize(1, ItemStack.EMPTY)
        Inventories.readNbt(nbt, dummyStacks)
        stack = dummyStacks[0]
    }

    override val itemStacks: List<ItemStack>
        get() = listOf(stack)

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = NbtCompound().also(this::writeNbt)


}