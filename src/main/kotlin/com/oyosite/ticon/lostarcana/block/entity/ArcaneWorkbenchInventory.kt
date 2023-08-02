package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.CraftingResultInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.RecipeMatcher
import net.minecraft.util.collection.DefaultedList

class ArcaneWorkbenchInventory(val blockEntity: ArcaneWorkbenchBlockEntity? = null) : Inventory {

    private val items = DefaultedList.ofSize(15, ItemStack.EMPTY)
    val recipeInput = RecipeInput(this)

    override fun clear() = items.clear()

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.all(ItemStack::isEmpty)

    override fun getStack(slot: Int): ItemStack = items[slot]

    override fun removeStack(slot: Int, count: Int): ItemStack {
        val result = Inventories.splitStack(items, slot, count)
        if (!result.isEmpty) markDirty()
        return result
    }

    override fun removeStack(slot: Int): ItemStack {
        markDirty()
        return Inventories.removeStack(items, slot)
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        items.set(slot, stack)
        if (stack.count > stack.maxCount) {
            stack.count = stack.maxCount
        }
        markDirty()
    }

    override fun markDirty() {
        blockEntity?.markDirty()
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean = true

    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        if(slot in 9..14){
            if(stack.item != ItemRegistry.VIS_CRYSTAL) return false
            if(stack.getSubNbt("vis") == null) return false
            if(LostArcana.id(stack.getSubNbt("vis")!!.getString("aspect")) == AspectRegistry.ASPECTS.keys.toList()[slot-9]) return true
        }
        return true
    }

    fun wrap(player: PlayerEntity?) = Wrapper(this, player)

    fun readNbt(nbt: NbtCompound) = Inventories.readNbt(nbt, items)
    fun writeNbt(nbt: NbtCompound) = Inventories.writeNbt(nbt, items)

    class Wrapper(val inventory: ArcaneWorkbenchInventory, val player: PlayerEntity?): Inventory by inventory{
        val recipeInput by inventory::recipeInput
        val result = CraftingResultInventory()

        var markDirtyCallback: ()->Unit = {}


        override fun clear() = inventory.clear().also { markDirty() }
        override fun removeStack(slot: Int): ItemStack = inventory.removeStack(slot).also{markDirty()}
        override fun removeStack(slot: Int, amount: Int): ItemStack = inventory.removeStack(slot, amount).also{markDirty()}
        override fun setStack(slot: Int, stack: ItemStack) = inventory.setStack(slot, stack).also{markDirty()}

        override fun markDirty() {
            //println("marked dirty")
            markDirtyCallback()
        }
    }

    class RecipeInput(val arcaneWorkbenchInventory: ArcaneWorkbenchInventory) : RecipeInputInventory{
        override fun clear() = (0..8).forEach { arcaneWorkbenchInventory.setStack(it, ItemStack.EMPTY) }

        override fun size(): Int = 9

        override fun isEmpty(): Boolean = (0..8).all{arcaneWorkbenchInventory.getStack(it).isEmpty}

        override fun getStack(slot: Int): ItemStack {
            if(slot !in 0..8)throw ArrayIndexOutOfBoundsException("Invalid slot id")
            return arcaneWorkbenchInventory.getStack(slot)
        }

        override fun removeStack(slot: Int, amount: Int): ItemStack {
            if(slot !in 0..8)throw ArrayIndexOutOfBoundsException("Invalid slot id")
            return arcaneWorkbenchInventory.removeStack(slot, amount)
        }

        override fun removeStack(slot: Int): ItemStack {
            if(slot !in 0..8)throw ArrayIndexOutOfBoundsException("Invalid slot id")
            return arcaneWorkbenchInventory.removeStack(slot)
        }

        override fun setStack(slot: Int, stack: ItemStack) {
            if(slot !in 0..8)throw ArrayIndexOutOfBoundsException("Invalid slot id")
            arcaneWorkbenchInventory.setStack(slot, stack)
        }

        override fun markDirty() = arcaneWorkbenchInventory.markDirty()

        override fun canPlayerUse(player: PlayerEntity?): Boolean = true

        override fun provideRecipeInputs(finder: RecipeMatcher) = (0..8).forEach { finder.addInput(arcaneWorkbenchInventory.getStack(it)) }

        override fun getWidth(): Int = 3

        override fun getHeight(): Int = 3

        override fun getInputStacks(): MutableList<ItemStack> = (0..8).map(arcaneWorkbenchInventory::getStack).toMutableList()

    }
}