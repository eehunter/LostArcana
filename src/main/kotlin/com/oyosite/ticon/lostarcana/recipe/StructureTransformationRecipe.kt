package com.oyosite.ticon.lostarcana.recipe

import com.google.gson.JsonObject
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes
import com.oyosite.ticon.lostarcana.math.Symmetry.*
import com.oyosite.ticon.lostarcana.math.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class StructureTransformationRecipe(
    val identifier: Identifier,
    val structureData: Array<Array<Array<Char>>>,
    val predicateMap: MutableMap<Char, (BlockPos,World)->Boolean>,
    val starterPos: List<Vec3d>,
    val symmetry: Symmetry,
    val resultStructure: Identifier,
    val input: Ingredient,
    val researchPredicate: (PlayerEntity)->Boolean
): Recipe<PlayerInventory> {

    override fun matches(inventory: PlayerInventory, world: World): Boolean {
        val player = inventory.player
        if(!input.test(player.mainHandStack))return false
        if(!researchPredicate(player))return false
        return when(symmetry){
            RADIAL -> matchExact(inventory, world, structureData, starterPos)
            QUAD -> matchQuad(inventory, world, structureData, starterPos)
            BILATERAL -> false
            NONE -> false
        }
    }

    fun matchExact(inventory: PlayerInventory, world: World, strData: Array<Array<Array<Char>>>, refPos: List<Vec3d>): Boolean{
        val player = inventory.player
        val hitResult = player.raycast(player.getAttributeValue(ReachEntityAttributes.REACH), 1.0f, false)
        if(hitResult.type!=HitResult.Type.BLOCK)return false
        val origin = calculateOrigin(strData)
        for(startPos in refPos){
            val minCoords = (hitResult.pos!!-origin-startPos).vec3i
            strData.forEachIndexed{ y, plane ->
                plane.forEachIndexed { x, row ->
                    row.forEachIndexed { z, char ->
                        if(predicateMap[char]?.invoke(BlockPos(minCoords.x+x, minCoords.y+y, minCoords.z+z), world)!=true)return false
                    }
                }
            }
        }
        return true
    }
    fun matchQuad(inventory: PlayerInventory, world: World, strData: Array<Array<Array<Char>>>, refPos: List<Vec3d>): Boolean{
        if(matchExact(inventory, world, strData, refPos)) return true
        val rotatedStrData = rotate90(strData)
        val rotatedRefPos = refPos.map{Vec3d(it.z, it.y, -it.x)}
        return matchExact(inventory, world, rotatedStrData, rotatedRefPos)
    }

    fun rotate90(strData: Array<Array<Array<Char>>>): Array<Array<Array<Char>>>{
        return strData.map{ plane ->
            val otpt = Array(plane[0].size){ Array(plane.size){' '} }
            plane.reversed().forEachIndexed{ r, row ->
                row.forEachIndexed { c, char ->
                    otpt[c][r] = char
                }
            }
            otpt
        }.toTypedArray()
    }

    fun flip(strData: Array<Array<Array<Char>>>): Array<Array<Array<Char>>>{
        return strData.map(Array<Array<Char>>::reversed).map(List<Array<Char>>::toTypedArray).toTypedArray()
    }

    fun calculateOrigin(strData: Array<Array<Array<Char>>>): Vec3d{
        return Vec3d(strData[0][0].size/2.0, strData.size/2.0, strData[0].size/2.0)
    }

    override fun craft(inventory: PlayerInventory?, registryManager: DynamicRegistryManager?): ItemStack {
        return ItemStack.EMPTY
    }

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getOutput(registryManager: DynamicRegistryManager?): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getId(): Identifier = identifier

    override fun getSerializer(): RecipeSerializer<StructureTransformationRecipe> = serializer

    override fun getType(): RecipeType<StructureTransformationRecipe> = Type

    object Type: RecipeType<StructureTransformationRecipe>

    object Serializer: RecipeSerializer<StructureTransformationRecipe>{

        override fun read(id: Identifier, json: JsonObject): StructureTransformationRecipe {
            //return StructureTransformationRecipe(id, json.getAsJsonArray("structureData"))
            TODO("Not yet implemented")
        }

        override fun read(id: Identifier?, buf: PacketByteBuf?): StructureTransformationRecipe {
            TODO("Not yet implemented")
        }

        override fun write(buf: PacketByteBuf?, recipe: StructureTransformationRecipe?) {
            TODO("Not yet implemented")
        }

    }
}