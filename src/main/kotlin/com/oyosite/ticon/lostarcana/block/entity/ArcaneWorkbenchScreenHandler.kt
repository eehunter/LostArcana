package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.recipe.ArcaneWorkbenchRecipe
import com.oyosite.ticon.lostarcana.recipe.ArcaneWorkbenchResultSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.CraftingResultInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeMatcher
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.book.RecipeBookCategory
import net.minecraft.screen.AbstractRecipeScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.slot.CraftingResultSlot
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.World

open class ArcaneWorkbenchScreenHandler(syncId: Int, playerInventory: PlayerInventory, val inv: ArcaneWorkbenchInventory.Wrapper, val ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY): AbstractRecipeScreenHandler<Inventory>(LostArcana.ARCANE_WORKBENCH_SCREEN_HANDLER, syncId) {
    constructor(syncId: Int, playerInventory: PlayerInventory): this(syncId, playerInventory, ArcaneWorkbenchInventory().wrap(playerInventory.player))

    //val input: RecipeInputInventory = dInput?:CraftingInventory(this, 5, 3)
    private val result = CraftingResultInventory()
    private val player = playerInventory.player

    init {
        //checkSize(input, 15)
        inv.markDirtyCallback = {onContentChanged(inv)}
        inv.onOpen(playerInventory.player)
        addSlot(ArcaneWorkbenchResultSlot(playerInventory.player, this.inv, this.inv.result, 0, 124+23, 35))

        //Vis crystal slots
        listOf(10 to 10, 86 to 10, 89 to 35, 86 to 60, 10 to 60, 7 to 35).forEachIndexed{ i, coords ->
            addSlot(FilteredSlot(inv, 9+i, coords.first, coords.second, i::testCrystalInSlot){onContentChanged(inv)})
        }
        //Crafting slots
        for(row in 0..2) for(col in 0..2)
            addSlot(Slot(inv, col+row*3, 30 + col * 18, 17 + row * 18){onContentChanged(inv)})
        //Inventory slots
        for(row in 0..2) for(col in 0..8)
            addSlot(Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18))
        //Hotbar slots
        for(slot in 0..8)
            addSlot(Slot(playerInventory, slot, 8 + slot * 18, 142))

        onContentChanged(inv)
    }

    override fun onContentChanged(inventory: Inventory) {
        ctx.run{world, pos -> updateResult(this, world, player, inv, inv.result) }
    }



    override fun quickMove(player: PlayerEntity?, invSlot: Int): ItemStack {
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

        return newStack!!
    }

    override fun canInsertIntoSlot(index: Int): Boolean = index != craftingResultSlotIndex

    override fun canUse(player: PlayerEntity): Boolean = true
    override fun populateRecipeFinder(finder: RecipeMatcher) = inv.recipeInput.provideRecipeInputs(finder)

    override fun clearCraftingSlots() {
        inv.clear()
        inv.result.clear()
    }

    override fun getCraftingResultSlotIndex(): Int = 0

    override fun getCraftingWidth(): Int = 3

    override fun getCraftingHeight(): Int = 3

    override fun getCraftingSlotCount(): Int = 10

    override fun getCategory(): RecipeBookCategory = RecipeBookCategory.CRAFTING

    override fun matches(recipe: Recipe<in Inventory>): Boolean = when(recipe.type){
        ArcaneWorkbenchRecipe.Type -> recipe.matches(inv, player.world)
        RecipeType.CRAFTING -> recipe.matches(inv.recipeInput, player.world)
        else -> false
    }


    companion object{
        protected fun updateResult(
            handler: ScreenHandler,
            world: World,
            player: PlayerEntity,
            inv: ArcaneWorkbenchInventory.Wrapper,
            resultInventory: CraftingResultInventory
        ) {
            var itemStack2: ItemStack? = null
            val craftingRecipe: CraftingRecipe
            if (world.isClient) return
            val awRecipe: ArcaneWorkbenchRecipe
            val serverPlayerEntity = player as ServerPlayerEntity
            var itemStack = ItemStack.EMPTY
            var optional = world.server!!.recipeManager.getFirstMatch(ArcaneWorkbenchRecipe.Type, inv, world)
            if(optional.isPresent){
                awRecipe = optional.get()
                if(resultInventory.shouldCraftRecipe(world, serverPlayerEntity, awRecipe)){
                    itemStack2 = awRecipe.craft(inv, world.registryManager)
                    if (itemStack2.isItemEnabled(world.enabledFeatures)) itemStack = itemStack2
                }
            }
            val optional2 = world.server!!.recipeManager.getFirstMatch(RecipeType.CRAFTING, inv.recipeInput, world)
            //println("found recipe")
            if (optional2.isPresent && itemStack2==null) {
                craftingRecipe = optional2.get()
                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, optional2.get())) {
                    itemStack2 = craftingRecipe.craft(inv.recipeInput, world.registryManager)
                    if (itemStack2.isItemEnabled(world.enabledFeatures)) itemStack = itemStack2
                }
            }
            resultInventory.setStack(0, itemStack)
            handler.setPreviousTrackedSlot(0, itemStack)
            serverPlayerEntity.networkHandler.sendPacket(
                ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack)
            )
        }
    }

}