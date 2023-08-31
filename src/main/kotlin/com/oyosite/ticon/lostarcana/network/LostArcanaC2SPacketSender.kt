package com.oyosite.ticon.lostarcana.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier


object LostArcanaC2SPacketSender {

    fun sendThaumonomiconPageUnlockPacket(ingredients: List<Ingredient>, advancementId: Identifier? = null, criterion: String? = null){
        val buf = PacketByteBufs.create()
        buf.writeByte(ingredients.size)
        for(ingredient in ingredients)ingredient.write(buf)
        buf.writeBoolean(advancementId!=null)
        if(advancementId!=null){
            if(criterion==null)throw IllegalArgumentException("Parameters advancementId and criterion must either both be null or both be non-null")
            buf.writeIdentifier(advancementId)
            buf.writeString(criterion)
        }
        ClientPlayNetworking.send(UNLOCK_THAUMONOMICON_PAGE, buf)
    }

}