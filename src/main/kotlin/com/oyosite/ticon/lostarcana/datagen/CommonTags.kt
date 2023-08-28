package com.oyosite.ticon.lostarcana.datagen

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


val GOLD_INGOTS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("c:gold_ingots"))
val GLASS_PANES: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("c:glass_panes"))

val FEATHERS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("c:feathers"))

val WOODEN_SLABS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("minecraft:wooden_slabs"))
val WOODEN_PLANKS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("minecraft:planks"))

val VANILLA_GLASS_PANES: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("glass_panes"))
