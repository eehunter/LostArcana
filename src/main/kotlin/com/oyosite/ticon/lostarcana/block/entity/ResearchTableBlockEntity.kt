package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem.Companion.researchCategory
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem.Companion.researchComplete
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem.Companion.researchProgress
import com.oyosite.ticon.lostarcana.item.ResearchNotesItem.Companion.maxResearchProgress
import com.oyosite.ticon.lostarcana.item.ResearchNotesType
import com.oyosite.ticon.lostarcana.research.ResearchCategory
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
import kotlin.math.min

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

    fun grantResearch(inventory: Inventory, amount: Int, researchType: ResearchNotesType, researchCategory: ResearchCategory){
        var amt = amount
        for(i in 0 until inventory.size()){
            val stack = inventory.getStack(i)
            if(stack.item !is ResearchNotesItem)continue
            if(stack.researchCategory != researchCategory)continue
            if(stack.researchComplete)continue
            val remainingSpace = stack.maxResearchProgress-stack.researchProgress
            if(remainingSpace>amt){
                stack.researchProgress+=amt
                return
            }
            stack.researchProgress = stack.maxResearchProgress
            amt -= remainingSpace
            if(amt == 0)return
        }
        while (amt > 0){
            val stack = ItemStack(researchType.item)
            // Maybe randomize research progress here?
            stack.researchCategory = researchCategory
            stack.researchProgress = min(amt, stack.maxResearchProgress)
            amt-=stack.researchProgress
            if(inventory is PlayerInventory){
                val player = inventory.player
                player.giveItemStack(stack)
            } else for(i in 0 until inventory.size()){
                if(!inventory.getStack(i).isEmpty)continue
                inventory.setStack(i, stack)
                break
            }
        }
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