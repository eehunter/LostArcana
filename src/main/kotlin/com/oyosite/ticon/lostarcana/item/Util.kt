package com.oyosite.ticon.lostarcana.item

import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import java.util.function.Consumer
import kotlin.math.min

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