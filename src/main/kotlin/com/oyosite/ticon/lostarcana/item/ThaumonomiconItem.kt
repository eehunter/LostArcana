package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.LostArcana
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import vazkii.patchouli.api.PatchouliAPI

class ThaumonomiconItem: Item(FabricItemSettings().maxCount(1)) {


    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return if(!world.isClient && user is ServerPlayerEntity){

            println("Player $user opening guidebook")

            if(user.isNewPlayer) user.openGuidebookIntro()
            else user.openGuidebook()

            TypedActionResult.success(user.getStackInHand(hand))
        } else {
            TypedActionResult.consume(user.getStackInHand(hand))
        }
    }

    private val ServerPlayerEntity.isNewPlayer get() = statHandler.getStat(Stats.USED, this@ThaumonomiconItem) == 0

    private fun ServerPlayerEntity.openGuidebook() = PatchouliAPI.get().openBookGUI(this, GUIDEBOOK_ID)
    private fun ServerPlayerEntity.openGuidebookIntro() = PatchouliAPI.get().openBookEntry(this, GUIDEBOOK_ID, LostArcana.id("fundamentals/intro"), 0)

    companion object{
        val GUIDEBOOK_ID = LostArcana.id("thaumonomicon")
    }
}