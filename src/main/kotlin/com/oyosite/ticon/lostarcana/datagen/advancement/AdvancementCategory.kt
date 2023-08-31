package com.oyosite.ticon.lostarcana.datagen.advancement

import com.oyosite.ticon.lostarcana.LostArcana
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.ImpossibleCriterion
import net.minecraft.item.ItemConvertible
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.function.Consumer
import kotlin.reflect.KProperty0

interface AdvancementCategory {

    val id: Identifier

    operator fun invoke(
        prop: KProperty0<ItemConvertible>,
        name: String = prop.name.lowercase(),
        researchCriterion: String? = "research_$name",
        title: MutableText = Text.translatable("advancement.${id.namespace}.${id.path}.$name"),
        desc: MutableText = Text.translatable("advancement.${id.namespace}.${id.path}.$name.desc"),
        background: Identifier = Identifier("textures/gui/advancements/backgrounds/adventure.png"),
        frame: AdvancementFrame = AdvancementFrame.TASK,
        showToast: Boolean = true,
        announce: Boolean = true,
        hidden: Boolean = false,
        advancementId: Identifier = Identifier(id.namespace, "${id.path}/$name"),
        preBuild: Advancement.Builder.()->Unit = {},
        build: Advancement.Builder.()->Advancement = {build(consumer, advancementId.toString())},
    ) = Advancement.Builder.create().display(
        prop.get(),
        title,
        desc,
        background,
        frame,
        showToast,
        announce,
        hidden,
    ).apply{researchCriterion?.also { criterion(it, ImpossibleCriterion.Conditions()) }}.apply(preBuild).run(build)


    companion object{

        var consumer: Consumer<Advancement>? = null

    }
}