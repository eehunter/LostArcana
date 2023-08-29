package com.oyosite.ticon.lostarcana.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor
    BlockEntityRenderDispatcher getBlockEntityRenderDispatcher();

    @Accessor
    ItemColors getItemColors();
}
