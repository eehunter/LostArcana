package com.oyosite.ticon.lostarcana.item

import com.oyosite.ticon.lostarcana.block.BlockRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

class SalisMundisItem: Item(FabricItemSettings()) {
    val craftingTable = TagKey.of(RegistryKeys.BLOCK, Identifier("c:crafting_tables"))

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        if(context.player?.canModifyAt(world, pos)==true) {
            if (world.getBlockState(pos).isIn(craftingTable)||world.getBlockState(pos)==Blocks.CRAFTING_TABLE.defaultState) {
                world.setBlockState(pos, BlockRegistry.ARCANE_WORKBENCH.defaultState)
                if(!context.player!!.isCreative)context.stack.decrement(1)
                return ActionResult.SUCCESS
            }
        }
        return super.useOnBlock(context)
    }


}