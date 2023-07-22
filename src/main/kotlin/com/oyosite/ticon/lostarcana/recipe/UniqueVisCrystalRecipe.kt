package com.oyosite.ticon.lostarcana.recipe

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.recipe.ShapelessRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

class UniqueVisCrystalRecipe(id: Identifier, group: String, category: CraftingRecipeCategory?, val output: ItemStack, val input: DefaultedList<Ingredient>, val doNotConsume: DefaultedList<Ingredient>) : ShapelessRecipe(id, group, category, output, input) {



    override fun matches(recipeInputInventory: RecipeInputInventory, world: World?): Boolean {
        val aspects = mutableSetOf<String>()
        return super.matches(recipeInputInventory, world) && recipeInputInventory.inputStacks.all { it.getSubNbt("vis")?.getString("aspect")?.run{(!aspects.contains(this)).also{_->aspects.add(this)}}?:true }
    }

    override fun getRemainder(inventory: RecipeInputInventory): DefaultedList<ItemStack> {
        val defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY)
        for (i in defaultedList.indices) {
            val stack = inventory.getStack(i)
            if(stack.item.hasRecipeRemainder()) defaultedList[i] = ItemStack(stack.item.getRecipeRemainder())
            else stack.remainder?.also{ defaultedList[i] = it }
        }
        return defaultedList
    }

    val ItemStack.remainder: ItemStack? get(){
        if(doNotConsume.any{it.test(this)})return this.copyWithCount(1)
        return null
    }


    override fun getSerializer(): RecipeSerializer<*> = Serializer

    object Serializer: RecipeSerializer<UniqueVisCrystalRecipe> {

        private fun getIngredients(json: JsonArray): DefaultedList<Ingredient> {
            val defaultedList = DefaultedList.of<Ingredient>()
            for (i in 0 until json.size()) {
                val ingredient = Ingredient.fromJson(json[i], false)
                if (ingredient.isEmpty) continue
                defaultedList.add(ingredient)
            }
            return defaultedList
        }
        override fun read(id: Identifier, json: JsonObject): UniqueVisCrystalRecipe {
            val group = JsonHelper.getString(json, "group", "")!!
            val craftingRecipeCategory = CraftingRecipeCategory.CODEC.byId(
                JsonHelper.getString(json, "category", null),
                CraftingRecipeCategory.MISC
            )
            val ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"))
            if (ingredients.isEmpty()) {
                throw JsonParseException("No ingredients for unique vis crystal recipe")
            }
            val doNotConsume = getIngredients(JsonHelper.getArray(json, "doNotConsume"))
            val output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"))
            return UniqueVisCrystalRecipe(id, group, craftingRecipeCategory, output, ingredients, doNotConsume)
        }

        override fun read(id: Identifier, buf: PacketByteBuf): UniqueVisCrystalRecipe {
            val group: String = buf.readString()
            val craftingRecipeCategory: CraftingRecipeCategory = buf.readEnumConstant<CraftingRecipeCategory>(
                CraftingRecipeCategory::class.java
            )
            val i: Int = buf.readVarInt()
            val ingredients = DefaultedList.ofSize(i, Ingredient.EMPTY)
            for (j in ingredients.indices) {
                ingredients[j] = Ingredient.fromPacket(buf)
            }
            val j: Int = buf.readVarInt()
            val doNotConsume = DefaultedList.ofSize(j, Ingredient.EMPTY)
            for (k in doNotConsume.indices) {
                doNotConsume[k] = Ingredient.fromPacket(buf)
            }
            val output: ItemStack = buf.readItemStack()
            return UniqueVisCrystalRecipe(id, group, craftingRecipeCategory, output, ingredients, doNotConsume)
        }

        override fun write(buf: PacketByteBuf, recipe: UniqueVisCrystalRecipe) {
            buf.writeString(recipe.group)
            buf.writeEnumConstant(recipe.category)
            buf.writeVarInt(recipe.input.size)
            for (ingredient in recipe.input) {
                ingredient.write(buf)
            }
            buf.writeVarInt(recipe.doNotConsume.size)
            for (ingredient in recipe.doNotConsume) {
                ingredient.write(buf)
            }
            buf.writeItemStack(recipe.output)
        }

    }

}