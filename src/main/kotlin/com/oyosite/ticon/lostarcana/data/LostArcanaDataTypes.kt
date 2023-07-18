package com.oyosite.ticon.lostarcana.data

import com.google.gson.JsonElement
import com.google.gson.JsonIOException
import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.fluid.EssentiaFluid
import com.oyosite.ticon.lostarcana.recipe.AlchemyRecipe
import io.github.apace100.calio.ClassUtil
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataType
import io.github.apace100.calio.data.SerializableDataTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.network.PacketByteBuf
import javax.naming.directory.SchemaViolationException

object LostArcanaDataTypes {
    /*val ESSENTIA_QUANTITY_LIST = SerializableDataType.compound<List<Pair<FluidVariant,Long>>(ClassUtil.castClass(Pair::class.java),
        SDKotlin("data", SerializableDataTypes.) ,
    )*/

    val ESSENTIA_QUANTITY_TYPE: SerializableDataType<List<Pair<FluidVariant,Long>>> = SerializableDataType(ClassUtil.castClass(List::class.java),
        ::writeEssentiaToPacket,
        ::readEssentiaFromPacket,
        ::readEssentiaFromJson
    )

    val ALCHEMY_RECIPE = SerializableDataType.compound(AlchemyRecipe::class.java,
        SDKotlin("identifier", SerializableDataTypes.IDENTIFIER, LostArcana.id("invalid"))
            ("catalyst", SerializableDataTypes.VANILLA_INGREDIENT)
            ("essentia", ESSENTIA_QUANTITY_TYPE, listOf())
            ("result", SerializableDataTypes.ITEM_STACK),
        ::readAlchemyRecipeFromData,
        ::writeAlchemyRecipeToData
    )

    fun readAlchemyRecipeFromData(d: SerializableData.Instance) =
        AlchemyRecipe(d.getId("identifier"), d["catalyst"], mapOf(*(d.get<List<Pair<FluidVariant, Long>>>("essentia")).toTypedArray()), {true}, d["result"])

    fun writeAlchemyRecipeToData(data: SerializableData, recipe: AlchemyRecipe): SerializableData.Instance{
        val d = data.Instance()
        d["identifier"] = recipe.identifier
        d["catalyst"] = recipe.catalyst
        d["essentia"] = recipe.requiredEssentia.entries.map { it.key to it.value }
        d["result"] = recipe.dummyResult
        return d
    }

    private fun writeEssentiaToPacket(buf: PacketByteBuf, essentia: List<Pair<FluidVariant,Long>>){
        buf.writeInt(essentia.size)
        for(e in essentia){
            buf.writeIdentifier(LostArcana.id(e.first.nbt!!.getString("aspect")))
            buf.writeLong(e.second)
        }
    }

    private fun readEssentiaFromPacket(buf: PacketByteBuf): List<Pair<FluidVariant,Long>>{
        val num = buf.readInt()
        val otpt: MutableList<Pair<FluidVariant,Long>> = mutableListOf()
        for(i in 0 until num){
            val id = buf.readIdentifier()
            val essentia = EssentiaFluid.VARIANTS[id.toString()]!!
            val amt = buf.readLong()
            otpt.add(essentia to amt)
        }
        return otpt
    }

    private fun readEssentiaFromJson(jsonElement: JsonElement): List<Pair<FluidVariant,Long>>{
        val json = jsonElement.asJsonObject
        val otpt: MutableList<Pair<FluidVariant,Long>> = mutableListOf()
        for(id in json.keySet()){
            println(id)
            val essentia = EssentiaFluid[id]?:throw JsonIOException("Essentia type not found.")
            val amt = json.getAsJsonPrimitive(id).asLong
            otpt.add(essentia to amt)
        }
        return otpt
    }
}