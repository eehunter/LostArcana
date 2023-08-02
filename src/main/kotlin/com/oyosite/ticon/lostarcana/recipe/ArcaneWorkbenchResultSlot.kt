package com.oyosite.ticon.lostarcana.recipe

import com.oyosite.ticon.lostarcana.block.entity.ArcaneWorkbenchInventory
import com.oyosite.ticon.lostarcana.component.LostArcanaComponentEntrypoint
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.screen.slot.CraftingResultSlot

class ArcaneWorkbenchResultSlot(val player: PlayerEntity, val input: ArcaneWorkbenchInventory.Wrapper, inventory: Inventory, index: Int, x: Int, y: Int) : CraftingResultSlot(player, input.recipeInput, inventory, index, x, y) {

    override fun onTakeItem(player: PlayerEntity, stack: ItemStack) {
        val opt = player.world.recipeManager.getFirstMatch(ArcaneWorkbenchRecipe.Type, input, player.world)
        onCrafted(stack)
        var remainder = player.world.recipeManager.getRemainingStacks(RecipeType.CRAFTING, this.input.recipeInput, player.world)
        if(opt.isPresent){
            val awRecipe = opt.get()
            //var b = false
            awRecipe.visCost.forEachIndexed { i, amt -> input.getStack(9+i).decrement(amt) }//; if(amt>0)b=true }
            input.inventory.blockEntity?.also{be->
                if(be.world == null)return@also
                val chunk = be.world!!.getChunk(be.pos)
                val comp = LostArcanaComponentEntrypoint.CHUNK_VIS[chunk]
                comp.vis-=awRecipe.auraCost
                LostArcanaComponentEntrypoint.CHUNK_VIS.sync(chunk)
            }
            remainder = awRecipe.getRemainder(input)
            //if(b)input.markDirty()
        }
        for (i in remainder.indices) {
            var itemStack = this.input.getStack(i)
            val itemStack2: ItemStack = remainder.get(i)
            if (!itemStack.isEmpty) {
                this.input.removeStack(i, 1)
                itemStack = this.input.getStack(i)
            }
            if (itemStack2.isEmpty) continue
            if (itemStack.isEmpty) {
                this.input.setStack(i, itemStack2)
                continue
            }
            if (ItemStack.canCombine(itemStack, itemStack2)) {
                itemStack2.increment(itemStack.count)
                this.input.setStack(i, itemStack2)
                continue
            }
            if (this.player.inventory.insertStack(itemStack2)) continue
            this.player.dropItem(itemStack2, false)
        }
        input.markDirty()
    }


}