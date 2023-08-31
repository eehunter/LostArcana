package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.advancement.criterion.ImpossibleCriterion
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.item.ItemConvertible
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.function.Consumer
import kotlin.reflect.KProperty0

class ArcanaAdvancementProvider(output: FabricDataOutput) : FabricAdvancementProvider(output) {
    override fun generateAdvancement(consumer: Consumer<Advancement>) {
        val rootAdvancement = Advancement.Builder.create().display(
            ItemRegistry.VIS_CRYSTAL,
            Text.translatable("advancement.lostarcana.fundamentals.root"),
            Text.translatable("advancement.lostarcana.fundamentals.root.desc"),
            Identifier("textures/gui/advancements/backgrounds/adventure.png"),
            AdvancementFrame.TASK,
            true, true, false
            )
            .criterion("got_vis_crystal", InventoryChangedCriterion.Conditions.items(ItemRegistry.VIS_CRYSTAL))
            .build(consumer, LostArcana.id("fundamentals/root"))
        val salisMundisAdvancement = Advancement.Builder.create().display(
            ItemRegistry.VIS_CRYSTAL,
            Text.translatable("advancement.lostarcana.fundamentals.salis_mundis"),
            Text.translatable("advancement.lostarcana.fundamentals.salis_mundis.desc"),
            Identifier("textures/gui/advancements/backgrounds/adventure.png"),
            AdvancementFrame.TASK,
            true, true, false
            ).criterion("got_salis_mundis", InventoryChangedCriterion.Conditions.items(ItemRegistry.SALIS_MUNDIS))
            .parent(rootAdvancement).build(consumer, LostArcana.id("fundamentals/salis_mundis"))
        val crucibleUnlockAdvancement = Advancement.Builder.create().display(
            BlockRegistry.CRUCIBLE,
            Text.translatable("advancement.lostarcana.fundamentals.crucible"),
            Text.translatable("advancement.lostarcana.fundamentals.crucible.desc"),
            Identifier("textures/gui/advancements/backgrounds/adventure.png"),
            AdvancementFrame.TASK,
            true, true, false
        ).criterion("researched_crucible", ImpossibleCriterion.Conditions())
            .parent(salisMundisAdvancement).build(consumer, LostArcana.id("fundamentals/crucible"))
        val arcaneWorkbenchUnlockAdvancement = Advancement.Builder.create().display(
            BlockRegistry.ARCANE_WORKBENCH,
            Text.translatable("advancement.lostarcana.fundamentals.arcane_workbench"),
            Text.translatable("advancement.lostarcana.fundamentals.arcane_workbench.desc"),
            Identifier("textures/gui/advancements/backgrounds/adventure.png"),
            AdvancementFrame.TASK,
            true, true, false
        ).criterion("researched_arcane_workbench", ImpossibleCriterion.Conditions())
            .parent(salisMundisAdvancement).build(consumer, LostArcana.id("fundamentals/arcane_workbench"))

        

    }




    fun Advancement.Builder.build(exporter: Consumer<Advancement>, id: Identifier) = build(exporter, id.toString())
}