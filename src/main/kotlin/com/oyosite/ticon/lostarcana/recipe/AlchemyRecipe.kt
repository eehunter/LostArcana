package com.oyosite.ticon.lostarcana.recipe

import com.google.gson.JsonObject
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes
import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.data.LostArcanaDataTypes.ALCHEMY_RECIPE
import com.oyosite.ticon.lostarcana.block.entity.CrucibleBlockEntity
import com.oyosite.ticon.lostarcana.block.entity.CruciblePseudoInventory
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.world.World

class AlchemyRecipe(
    val identifier: Identifier,
    val catalyst: Ingredient,
    val requiredEssentia: Map<FluidVariant, Long>,
    val researchPredicate: (PlayerEntity)->Boolean,
    val dummyResult: ItemStack,
    val result: (ItemStack, CrucibleBlockEntity)->ItemStack = {_,_->dummyResult},
): Recipe<CruciblePseudoInventory> {

    override fun matches(inventory: CruciblePseudoInventory, world: World): Boolean {
        val stack = inventory.activeStack
        if(!catalyst.test(stack))return false//.also{ println("catalyst mismatch") }
        if(inventory.lastUser?.let(researchPredicate) == false)return false//.also{ println("missing research") }
        val be = inventory.be

        return requiredEssentia.all{ e -> be.essentiaContent.any { (e.key==it.key && e.value <= it.value) } }//.also(::println)
    }

    override fun craft(inventory: CruciblePseudoInventory, registryManager: DynamicRegistryManager): ItemStack = result(inventory.activeStack, inventory.be).also{
        if(inventory.lastUser?.isCreative != true) inventory.activeStack.decrement(1)
        val ec = inventory.be.essentiaContent
        for(e in requiredEssentia) ec[e.key] = ec[e.key]?.minus(e.value)?:0
    }

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getOutput(registryManager: DynamicRegistryManager?): ItemStack = dummyResult

    override fun getId(): Identifier = identifier

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    override fun getType(): RecipeType<*> = Type

    object Type: RecipeType<AlchemyRecipe>{
        //init{ Registry.register(Registries.RECIPE_TYPE, LostArcana.id("alchemy_recipe"), Type) }
    }

    object Serializer: RecipeSerializer<AlchemyRecipe>{
        override fun read(id: Identifier, json: JsonObject): AlchemyRecipe = ALCHEMY_RECIPE.read(json.deepCopy().also{it.addProperty("identifier", id.toString())})


        override fun read(id: Identifier, buf: PacketByteBuf): AlchemyRecipe = ALCHEMY_RECIPE.receive(buf)

        override fun write(buf: PacketByteBuf, recipe: AlchemyRecipe) = ALCHEMY_RECIPE.send(buf, recipe)


    }

}