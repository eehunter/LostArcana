package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.CraftingResultInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.RecipeMatcher
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos


class ArcaneWorkbenchBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(LostArcana.ARCANE_WORKBENCH_BLOCK_ENTITY, pos, state), RecipeInputInventory, NamedScreenHandlerFactory {
    private val items = DefaultedList.ofSize(15, ItemStack.EMPTY)
    var craftingInventory: CraftingInventory? = null
    var craftingResultInventory: CraftingResultInventory? = null


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
        if(slot in 0..8)craftingInventory?.setStack(slot, getStack(slot))
        return result
    }

    override fun removeStack(slot: Int): ItemStack {
        return Inventories.removeStack(items, slot).also { if(slot in 0..8) craftingInventory?.setStack(slot, getStack(slot)) }
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        items.set(slot, stack)
        if (stack.count > stack.maxCount) {
            stack.count = stack.maxCount
        }
        if(slot in 0..8) craftingInventory?.setStack(slot, stack)
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean = true

    private val primalAspects = listOf("aer", "ignis", "perditio", "terra", "aqua", "ordo")
    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        if(slot in 9..14){
            if(stack.item != ItemRegistry.VIS_CRYSTAL) return false
            if(stack.getSubNbt("vis") == null) return false
            if(LostArcana.id(stack.getSubNbt("vis")!!.getString("aspect")) == AspectRegistry.ASPECTS.keys.toList()[slot-9]) return true
        }
        return true
    }

    override fun provideRecipeInputs(finder: RecipeMatcher) = items.forEach(finder::addInput)


    override fun getWidth(): Int = 3

    override fun getHeight(): Int = 3

    override fun getInputStacks(): MutableList<ItemStack> = items.toMutableList()

    fun syncCraftingInventory(){
        for(i in 0..8)craftingInventory?.setStack(i, getStack(i))
    }

    override fun readNbt(nbt: NbtCompound) {
        Inventories.readNbt(nbt, items)
    }

    override fun writeNbt(nbt: NbtCompound) {
        Inventories.writeNbt(nbt, items)
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler = ArcaneWorkbenchScreenHandler(syncId, playerInventory, this)

    override fun getDisplayName(): Text = Text.translatable(cachedState.block.translationKey)
}