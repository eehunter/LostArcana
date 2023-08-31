package com.oyosite.ticon.lostarcana.item

import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.util.math.Direction
import java.util.function.Consumer
import kotlin.math.min


/**
 * Adds a single itemstack to an inventory. Automatically converted to kotlin from [InventoryHelper#smartAddToInventory](https://github.com/DaFuqs/Spectrum/blob/22d72ca413577d75d2da0f8e2d8066c651259143/src/main/java/de/dafuqs/spectrum/helpers/InventoryHelper.java#L106)
 * This code is used here in accordance with the [GNU General Public License](https://github.com/DaFuqs/Spectrum/blob/1.19-deeper-down/LICENSE.md)
 *
 * @param itemStack the itemstack to add. The stack can have a size > maxStackSize and will be split accordingly
 * @param inventory the inventory to add to
 * @return The remaining stack that could not be added
 */
fun smartAddToInventory(itemStack: ItemStack, inventory: Inventory, side: Direction?): ItemStack {
    var itemStack = itemStack
    if (inventory is SidedInventory && side != null) {
        val acceptableSlots = inventory.getAvailableSlots(side)
        for (acceptableSlot in acceptableSlots) {
            if (inventory.canInsert(acceptableSlot, itemStack, side)) {
                itemStack = setOrCombineStack(inventory, acceptableSlot, itemStack)
                if (itemStack.isEmpty) {
                    break
                }
            }
        }
    } else {
        for (i in 0 until inventory.size()) {
            itemStack = setOrCombineStack(inventory, i, itemStack)
            if (itemStack.isEmpty) {
                break
            }
        }
    }
    return itemStack
}

/**Automatically converted to kotlin from [InventoryHelper#smartAddToInventory](https://github.com/DaFuqs/Spectrum/blob/22d72ca413577d75d2da0f8e2d8066c651259143/src/main/java/de/dafuqs/spectrum/helpers/InventoryHelper.java#L128)
 * This code is used here in accordance with the [GNU General Public License](https://github.com/DaFuqs/Spectrum/blob/1.19-deeper-down/LICENSE.md)
 * */
fun setOrCombineStack(inventory: Inventory, slot: Int, addingStack: ItemStack): ItemStack {
    val existingStack = inventory.getStack(slot)
    if (existingStack.isEmpty) {
        if (addingStack.count > addingStack.maxCount) {
            var amount =
                min(addingStack.maxCount.toDouble(), addingStack.count.toDouble()).toInt()
            amount = min(amount.toDouble(), inventory.maxCountPerStack.toDouble()).toInt()
            val newStack = addingStack.copy()
            newStack.count = amount
            addingStack.decrement(amount)
            inventory.setStack(slot, newStack)
        } else {
            inventory.setStack(slot, addingStack)
            return ItemStack.EMPTY
        }
    } else {
        combineStacks(existingStack, addingStack)
    }
    return addingStack
}

/**Automatically converted to kotlin from [InventoryHelper#smartAddToInventory](https://github.com/DaFuqs/Spectrum/blob/22d72ca413577d75d2da0f8e2d8066c651259143/src/main/java/de/dafuqs/spectrum/helpers/InventoryHelper.java#L148)
 * This code is used here in accordance with the [GNU General Public License](https://github.com/DaFuqs/Spectrum/blob/1.19-deeper-down/LICENSE.md)
 * */
fun combineStacks(originalStack: ItemStack, addingStack: ItemStack) {
    if (ItemStack.canCombine(originalStack, addingStack)) {
        val leftOverAmountInExistingStack = originalStack.maxCount - originalStack.count
        if (leftOverAmountInExistingStack > 0) {
            val addAmount =
                min(leftOverAmountInExistingStack.toDouble(), addingStack.count.toDouble()).toInt()
            originalStack.increment(addAmount)
            addingStack.decrement(addAmount)
        }
    }
}

fun removeFromInventoryWithRemainders(ingredients: List<Ingredient>, inv: Inventory):List<ItemStack>{
    val remainders = mutableListOf<ItemStack>()

    val requiredIngredients = mutableListOf<Ingredient>()
    val requiredIngredientAmounts = mutableListOf<Int>()
    for (ingredient in ingredients){
        if(ingredient.isEmpty) continue
        requiredIngredients.add(ingredient)
        if(ingredient.matchingStacks.isNotEmpty()) requiredIngredientAmounts.add(ingredient.matchingStacks[0].count)
        else requiredIngredientAmounts.add(1)
    }

    for(i in 0 until inv.size()){
        if(requiredIngredients.size == 0) break

        val currentStack = inv.getStack(i)
        if(currentStack.isEmpty)continue
        var j = -1
        while(++j<requiredIngredients.size){
            val currentStackCount = currentStack.count
            if(requiredIngredients[j].test(currentStack)){
                val ingredientCount = requiredIngredientAmounts[j]
                val remainder = currentStack.recipeRemainder
                if(currentStackCount >= ingredientCount){
                    if(!remainder.isEmpty){
                        remainder.count = requiredIngredientAmounts[j]
                        remainders += remainder
                    }
                    requiredIngredients.removeAt(j)
                    requiredIngredientAmounts.removeAt(j--)
                } else {
                    if(!remainder.isEmpty){
                        remainder.count = requiredIngredientAmounts[j]
                        remainders += remainder
                    }
                    requiredIngredientAmounts[j] = requiredIngredientAmounts[j] - currentStackCount
                }
                currentStack.count = currentStackCount - ingredientCount
            }
        }
    }
    return remainders
}

fun hasInInventory(ingredients: Iterable<Ingredient>, inventory: Inventory): Boolean{
    val ingredientsToFind = mutableListOf<Ingredient>()
    val requiredIngredientAmounts = mutableListOf<Int>()
    for(ingredient in ingredients){
        if(ingredient.isEmpty)continue

        ingredientsToFind.add(ingredient)
        requiredIngredientAmounts.add(if(ingredient.matchingStacks.isNotEmpty())ingredient.matchingStacks[0].count else 1)
    }

    for(i in 0 until inventory.size()){
        if(ingredientsToFind.isEmpty())break
        val currentStack = inventory.getStack(i)
        if(!currentStack.isEmpty){
            var amt = currentStack.count
            var j = 0
            while (j in ingredientsToFind.indices){
                if(!ingredientsToFind[j].test(currentStack)) {
                    j++
                    continue
                }
                val ingredientCount = requiredIngredientAmounts[j]
                if(amt > ingredientCount){
                    ingredientsToFind.removeAt(j)
                    requiredIngredientAmounts.removeAt(j)
                } else requiredIngredientAmounts[j] = requiredIngredientAmounts[j++] - amt

                amt -= ingredientCount
                if(amt<1)break
            }
        }
    }
    return ingredientsToFind.isEmpty()
}

fun scribingToolsDamageHandler(stack: ItemStack, amt: Int, entity: LivingEntity, breakCallback: Consumer<LivingEntity>): Int = stack.run{ min(amt, maxDamage-1-damage) }