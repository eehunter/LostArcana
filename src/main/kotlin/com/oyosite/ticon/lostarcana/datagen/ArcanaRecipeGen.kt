package com.oyosite.ticon.lostarcana.datagen

import com.google.gson.JsonObject
import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.data.LostArcanaDataTypes
import com.oyosite.ticon.lostarcana.fluid.EssentiaFluid
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry.toNbt
import com.oyosite.ticon.lostarcana.recipe.AlchemyRecipe
import com.oyosite.ticon.lostarcana.recipe.NitorDyeRecipe
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import java.util.function.Consumer

class ArcanaRecipeGen(generator: FabricDataOutput): FabricRecipeProvider(generator) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>) = G(exporter).gen{
        +AlchemyRecipe(LostArcana.id("nitor"), Ingredient.ofItems(Items.GLOWSTONE_DUST), mapOf(EssentiaFluid["lux"]!! to 5*810L), {true}, ItemStack(BlockRegistry.NITOR).apply{setSubNbt("nitor", DyeColor.YELLOW.toNbt)})
        +NitorDyeRecipeProvider()



    }

    class G(val exporter: Consumer<RecipeJsonProvider>){
        fun gen(generate: G.()->Unit): Unit = generate()
        operator fun AlchemyRecipe.unaryPlus(){
            exporter.accept(AlchemyRecipeProvider(this))
        }
        operator fun RecipeJsonProvider.unaryPlus() = exporter.accept(this)


    }

    class NitorDyeRecipeProvider: RecipeJsonProvider{
        override fun serialize(json: JsonObject) {}

        override fun getRecipeId(): Identifier = LostArcana.id("nitor_dye")

        override fun getSerializer(): RecipeSerializer<*> = NitorDyeRecipe.Serializer

        override fun toAdvancementJson(): JsonObject? = null

        override fun getAdvancementId(): Identifier? = null

    }

    class AlchemyRecipeProvider(val alchemyRecipe: AlchemyRecipe): RecipeJsonProvider{

        val Consumer<RecipeJsonProvider>.export get() = accept(this@AlchemyRecipeProvider)

        override fun serialize(json: JsonObject) {
            //var itemId = Identifier("air")
            //try{itemId = Registries.ITEM.getId(stack.item); itemId.toString() } catch (e: Exception) {e.printStackTrace()}
            json.add("catalyst", alchemyRecipe.catalyst.toJson())
            val essentia = JsonObject()
            alchemyRecipe.requiredEssentia.entries.forEach{essentia.addProperty(it.key.nbt!!.getString("aspect"), it.value)}
            json.add("essentia", essentia)
            val item = JsonObject()
            val stack = alchemyRecipe.dummyResult
            item.addProperty("item", Registries.ITEM.getId(stack.item).toString())
            if(stack.count != 1)item.addProperty("count", stack.count)
            stack.nbt?.also{item.addProperty("tag", it.toString())}
            json.add("result", item)
        }

        override fun getRecipeId(): Identifier = alchemyRecipe.id

        override fun getSerializer(): RecipeSerializer<*> = AlchemyRecipe.Serializer

        override fun toAdvancementJson(): JsonObject? = null

        override fun getAdvancementId(): Identifier? = null

    }

}