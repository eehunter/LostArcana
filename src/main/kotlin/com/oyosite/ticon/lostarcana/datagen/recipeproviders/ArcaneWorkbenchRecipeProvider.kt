package com.oyosite.ticon.lostarcana.datagen.recipeproviders

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.oyosite.ticon.lostarcana.recipe.ArcaneWorkbenchRecipe
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier

class ArcaneWorkbenchRecipeProvider(val identifier: Identifier, val base: RecipeJsonProvider, val visCost: Array<Int>, val auraCost: Int): RecipeJsonProvider {
    override fun serialize(json: JsonObject) {
        json.add("base", base.toJson())
        val visCostJson = JsonArray()
        visCost.forEach(visCostJson::add)
        json.add("visCost", visCostJson)
        json.addProperty("auraCost", auraCost)
    }

    override fun getRecipeId(): Identifier = identifier

    override fun getSerializer(): RecipeSerializer<*> = ArcaneWorkbenchRecipe.Serializer

    override fun toAdvancementJson(): JsonObject? = null

    override fun getAdvancementId(): Identifier? = null
}