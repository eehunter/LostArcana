package com.oyosite.ticon.bookbuilder

import com.google.gson.JsonObject
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class Book(val name: String, val landingText: String, val customBook: ItemStack? = null, val version: Int = 0) {

    var texture: Identifier? = null //= Identifier("patchouli", "textures/gui/book_brown.png")



    val asJson: JsonObject get(){
        val json = JsonObject()
        json.addProperty("name", name)
        json.addProperty("landing_text", landingText)
        json.addProperty("use_resource_pack", true)
        json.addProperty("version", version)
        customBook?.let{
            json.addProperty("dont_generate_book", true)
            json.addProperty("custom_book_item", "${Registries.ITEM.getId(it.item)}${it.nbt?:""}")
        }
        texture?.let{json.addProperty("texture", it.toString())}
        return json
    }
}