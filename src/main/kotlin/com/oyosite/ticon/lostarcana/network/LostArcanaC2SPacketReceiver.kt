package com.oyosite.ticon.lostarcana.network

import com.oyosite.ticon.lostarcana.item.removeFromInventoryWithRemainders
import com.oyosite.ticon.lostarcana.item.smartAddToInventory
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

object LostArcanaC2SPacketReceiver {
    operator fun invoke(){
        UNLOCK_THAUMONOMICON_PAGE{ server: MinecraftServer, player: ServerPlayerEntity, handler: ServerPlayNetworkHandler, buf: PacketByteBuf, packetSender: PacketSender ->
            val ingredientCount = buf.readByte()
            val ingredients = mutableListOf<Ingredient>()
            for(i in 0 until ingredientCount)ingredients+= Ingredient.fromPacket(buf)
            for (remainder in removeFromInventoryWithRemainders(ingredients, player.inventory))
                smartAddToInventory(remainder, player.inventory, null)

            if(buf.readBoolean()){
                val advancementId = buf.readIdentifier()
                val criterion = buf.readString()
                grantAdvancementCriterion(player, advancementId, criterion)
            }

            if(ingredientCount>0)player.world.playSound(player, player.x, player.y, player.z, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F)
        }

        //{ server: MinecraftServer, player: ServerPlayerEntity, handler: ServerPlayNetworkHandler, buf: PacketByteBuf, packetSender: PacketSender -> }
    }


    operator fun Identifier.invoke(handler: ServerPlayNetworking.PlayChannelHandler) =
        ServerPlayNetworking.registerGlobalReceiver(this, handler)




    fun grantAdvancementCriterion(player: ServerPlayerEntity, id: Identifier, criterion: String){
        val advancementLoader = player.server.advancementLoader
        val tracker = player.advancementTracker

        advancementLoader[id]?.let{advancement ->
            if(!tracker.getProgress(advancement).isDone)tracker.grantCriterion(advancement, criterion)
        }?: println("Cannot grant criterion $criterion for advancement $id, which does not exist.")
    }
}