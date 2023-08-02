package com.oyosite.ticon.lostarcana.recipe

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.oyosite.ticon.lostarcana.block.entity.ArcaneWorkbenchInventory
import com.oyosite.ticon.lostarcana.component.LostArcanaComponentEntrypoint
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.*
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

class ArcaneWorkbenchRecipe(val identifier: Identifier, val base: CraftingRecipe, val researchPredicate: (PlayerEntity)->Boolean, val visCost: Array<Int> = Array(6){0}, val auraCost: Int = 0): Recipe<ArcaneWorkbenchInventory.Wrapper> {
    override fun matches(inventory: ArcaneWorkbenchInventory.Wrapper, world: World): Boolean {
        //println("checking match")
        if(!base.matches(inventory.recipeInput, world))return false
        if(!world.isClient && LostArcanaComponentEntrypoint.CHUNK_VIS[world.getChunk(inventory.inventory.blockEntity!!.pos)].vis < auraCost)return false
        for(i in 0 until 6)if(visCost[i]>inventory.getStack(9+i).count)return false
        return inventory.player?.let(researchPredicate) != false
    }

    override fun craft(inventory: ArcaneWorkbenchInventory.Wrapper, registryManager: DynamicRegistryManager): ItemStack = base.craft(inventory.recipeInput, registryManager)

    override fun fits(width: Int, height: Int): Boolean = base.fits(width, height)

    override fun getOutput(registryManager: DynamicRegistryManager?): ItemStack = base.getOutput(registryManager)

    override fun getId(): Identifier = identifier

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    override fun getType(): RecipeType<*> = Type

    override fun getRemainder(inventory: ArcaneWorkbenchInventory.Wrapper): DefaultedList<ItemStack> {
        val defaultedList = DefaultedList.ofSize(9, ItemStack.EMPTY)
        for (i in defaultedList.indices) {
            val item = inventory.getStack(i).item
            if (!item.hasRecipeRemainder()) continue
            defaultedList[i] = ItemStack(item.recipeRemainder)
        }
        return defaultedList
    }

    object Type: RecipeType<ArcaneWorkbenchRecipe>

    object Serializer: RecipeSerializer<ArcaneWorkbenchRecipe>{
        override fun read(id: Identifier, json: JsonObject): ArcaneWorkbenchRecipe {
            val baseRecipe: CraftingRecipe = RecipeManager.deserialize(id.withSuffixedPath("_base"),json.get("base").asJsonObject).let{if(it is CraftingRecipe) it else throw JsonParseException("Invalid base recipe.")}
            val visCost = json.getAsJsonArray("visCost").map(JsonElement::getAsInt).toTypedArray()
            val auraCost = json["auraCost"].asInt
            return ArcaneWorkbenchRecipe(id, baseRecipe, {true}, visCost, auraCost)
        }

        override fun read(id: Identifier, buf: PacketByteBuf): ArcaneWorkbenchRecipe {
            val sid = buf.readIdentifier()
            val serializer = Registries.RECIPE_SERIALIZER.get(sid)?: throw RuntimeException("RecipeSerializer with identifier $sid not found")
            val baseRecipe: CraftingRecipe = serializer.read(id.withSuffixedPath("_base"), buf).let{if(it is CraftingRecipe)it else throw RuntimeException("baseRecipe is not CraftingRecipe")}
            val visCost = buf.readIntArray().toTypedArray()
            val auraCost = buf.readInt()
            return ArcaneWorkbenchRecipe(id, baseRecipe, {true}, visCost, auraCost)
        }

        override fun write(buf: PacketByteBuf, recipe: ArcaneWorkbenchRecipe) {
            buf.writeIdentifier(Registries.RECIPE_SERIALIZER.getId(recipe.base.serializer))
            (recipe.base.serializer as RecipeSerializer<CraftingRecipe>).write(buf, recipe.base)
            buf.writeIntArray(recipe.visCost.toIntArray())
            buf.writeInt(recipe.auraCost)
        }

    }
}