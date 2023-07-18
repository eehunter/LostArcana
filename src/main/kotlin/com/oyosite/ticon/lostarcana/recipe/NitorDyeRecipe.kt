package com.oyosite.ticon.lostarcana.recipe

import com.google.gson.JsonObject
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.world.World

class NitorDyeRecipe(val identifier: Identifier): CraftingRecipe {
    override fun matches(inventory: RecipeInputInventory, world: World): Boolean {
        var nitor = 0
        var dye = 0
        for(i in 0 until inventory.size()) when (inventory.getStack(i).item) {
            BlockRegistry.NITOR.asItem() -> nitor++
            is DyeItem -> dye++
            else -> if(!inventory.getStack(i).isEmpty)return false
        }
        return nitor==1 && dye==1
    }

    override fun craft(inventory: RecipeInputInventory, registryManager: DynamicRegistryManager): ItemStack {
        var nitor = ItemStack.EMPTY
        var dyeColor = DyeColor.YELLOW
        for(i in 0 until inventory.size()) if(inventory.getStack(i).item==BlockRegistry.NITOR.asItem()) nitor = inventory.getStack(i).copyWithCount(1)
        for(i in 0 until inventory.size()) if(inventory.getStack(i).item is DyeItem) dyeColor = (inventory.getStack(i).item as DyeItem).color
        nitor.getOrCreateSubNbt("nitor").putString("color", dyeColor.name.lowercase())
        return nitor
    }

    override fun fits(width: Int, height: Int): Boolean = width>=2||height>=2

    override fun getOutput(registryManager: DynamicRegistryManager?): ItemStack = ItemStack(BlockRegistry.NITOR)

    override fun getId(): Identifier = identifier

    override fun getSerializer(): RecipeSerializer<*> = Serializer

    //override fun getType(): RecipeType<*> = Type
    override fun getCategory(): CraftingRecipeCategory = CraftingRecipeCategory.MISC

    //object Type: RecipeType<NitorDyeRecipe>

    object Serializer: RecipeSerializer<NitorDyeRecipe>{
        override fun read(id: Identifier, json: JsonObject?): NitorDyeRecipe = NitorDyeRecipe(id)

        override fun read(id: Identifier, buf: PacketByteBuf?): NitorDyeRecipe = NitorDyeRecipe(id)

        override fun write(buf: PacketByteBuf?, recipe: NitorDyeRecipe?) {}

    }

}