package com.oyosite.ticon.lostarcana.datagen

import com.google.common.collect.Lists
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.recipe.UniqueVisCrystalRecipe
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.CriterionMerger
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.function.Consumer

class UniqueVisCrystalRecipeJsonBuilder(val category: RecipeCategory, val output: ItemConvertible, val count: Int = 1) : ShapelessRecipeJsonBuilder(category, output, count) {
    val doNotConsume = mutableListOf<Ingredient>()
    private val advancementBuilder = Advancement.Builder.createUntelemetered()
    private var group: String? = null
    private val inputs: MutableList<Ingredient> = mutableListOf()
    private var advancementId: Identifier? = null

    override fun input(tag: TagKey<Item>): UniqueVisCrystalRecipeJsonBuilder {
        return this.input(Ingredient.fromTag(tag))
    }

    override fun input(itemProvider: ItemConvertible?): UniqueVisCrystalRecipeJsonBuilder {
        return this.input(itemProvider, 1)
    }

    override fun input(itemProvider: ItemConvertible?, size: Int): UniqueVisCrystalRecipeJsonBuilder {
        for (i in 0 until size) this.input(Ingredient.ofItems(itemProvider))
        return this
    }

    override fun input(ingredient: Ingredient): UniqueVisCrystalRecipeJsonBuilder {
        return this.input(ingredient, 1)
    }

    override fun input(ingredient: Ingredient, size: Int): UniqueVisCrystalRecipeJsonBuilder {
        for (i in 0 until size) inputs.add(ingredient)
        return this
    }
    fun catalyst(ingredient: Ingredient): UniqueVisCrystalRecipeJsonBuilder{
        input(ingredient)
        doNotConsume.add(ingredient)
        return this
    }
    fun catalyst(item: ItemConvertible) = catalyst(Ingredient.ofItems(item))

    override fun criterion(string: String?, criterionConditions: CriterionConditions?): UniqueVisCrystalRecipeJsonBuilder {
        this.advancementBuilder.criterion(string, criterionConditions)
        return this
    }

    override fun group(string: String?): UniqueVisCrystalRecipeJsonBuilder {
        this.group = string
        return this
    }

    override fun offerTo(exporter: Consumer<RecipeJsonProvider>, recipeId: Identifier) {
        validate(recipeId)
        advancementBuilder.parent(ROOT).criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(CriterionMerger.OR)
        exporter.accept(UniqueVisCrystalRecipeJsonProvider(recipeId, outputItem, count, group?:"", getCraftingCategory(category), inputs, doNotConsume, advancementBuilder, recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/")))
    }
    private fun validate(recipeId: Identifier) {
        check(!this.advancementBuilder.criteria.isEmpty()) { "No way of obtaining recipe $recipeId" }
    }


    class UniqueVisCrystalRecipeJsonProvider(recipeId: Identifier, output: Item, outputCount: Int, group: String?,
                                             craftingCategory: CraftingRecipeCategory?, inputs: MutableList<Ingredient>, val doNotConsume: MutableList<Ingredient>,
                                             advancementBuilder: Advancement.Builder?,
                                             advancementId: Identifier?
    ): ShapelessRecipeJsonProvider(recipeId, output, outputCount, group, craftingCategory, inputs, advancementBuilder, advancementId){
        override fun serialize(json: JsonObject) {
            super.serialize(json)
            val jsonArray = JsonArray()
            for (ingredient in doNotConsume) { jsonArray.add(ingredient.toJson()) }
            json.add("doNotConsume", jsonArray)
        }

        override fun getSerializer(): RecipeSerializer<*> = UniqueVisCrystalRecipe.Serializer

        override fun getAdvancementId(): Identifier = super.getAdvancementId()?:LostArcana.id("fundamentals/root")


    }


}