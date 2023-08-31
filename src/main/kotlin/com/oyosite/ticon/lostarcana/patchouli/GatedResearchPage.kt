package com.oyosite.ticon.lostarcana.patchouli

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.item.hasInInventory
import com.oyosite.ticon.lostarcana.network.LostArcanaC2SPacketSender
import com.oyosite.ticon.lostarcana.patchouli.widgets.ResearchButtonWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.resource.language.I18n
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.world.World
import vazkii.patchouli.api.IVariable
import vazkii.patchouli.client.base.PersistentData
import vazkii.patchouli.client.book.BookContentsBuilder
import vazkii.patchouli.client.book.BookEntry
import vazkii.patchouli.client.book.BookPage
import vazkii.patchouli.client.book.gui.BookTextRenderer
import vazkii.patchouli.client.book.gui.GuiBook
import vazkii.patchouli.client.book.gui.GuiBookEntry
import vazkii.patchouli.common.book.Book
import kotlin.math.min

/**
 * This is heavily based upon the [PageHint](https://github.com/DaFuqs/Spectrum/blob/1.19-deeper-down/src/main/java/de/dafuqs/spectrum/compat/patchouli/pages/PageHint.java)
 * class from the Spectrum mod by DaFuqs
 * */
open class GatedResearchPage: BookPage() {

    var cost: IVariable? = null
    var text: IVariable? = null

    var advancementId: IVariable? = null
    var advancementCriterion: IVariable? = null

    @Transient var textRenderer: BookTextRenderer? = null
    @Transient var ingredients: List<Ingredient> = listOf()

    @Transient var lastRevealTick: Long = 0
    @Transient var revealProgress: Long = -1

    var rawText: Text = Text.empty()
    var displayedText: Text = Text.empty()

    var title: String? = null

    val textureHeight: Int = 22

    val refreshAdvancement: Unit by lazy {if(isQuestDone(book))LostArcanaC2SPacketSender.sendThaumonomiconPageUnlockPacket(
        listOf(), advancementId?.asString()?.let(LostArcana::id), advancementCriterion?.asString()
    )}

    override fun build(level: World, entry: BookEntry, builder: BookContentsBuilder, pageNum: Int) {
        super.build(level, entry, builder, pageNum)
        ingredients = cost!!.asList().map{it.`as`(Ingredient::class.java)}
    }

    fun isQuestDone(book: Book): Boolean = PersistentData.data.getBookData(book).completedManualQuests.contains(entryId)

    val entryId: Identifier by lazy { Identifier(entry.id.namespace, entry.id.path + "_" + pageNum) }

    override fun onDisplayed(parent: GuiBookEntry, left: Int, top: Int) {
        super.onDisplayed(parent, left, top)
        rawText = text!!.`as`(Text::class.java)

        val isDone = isQuestDone(parent.book)
        if(!isDone){
            revealProgress = -1
            displayedText = calculateTextToRender(rawText)

            val researchButton = ResearchButtonWidget(GuiBook.PAGE_WIDTH / 2 - 50, GuiBook.PAGE_HEIGHT - 35, 100, 20, Text.empty(), this, this::researchButtonClicked){Text.translatable("Purchase research")}
            addButton(researchButton)
        } else {
            displayedText = rawText
            revealProgress = 0
            refreshAdvancement
        }
        textRenderer = BookTextRenderer(parent, displayedText, 0, textureHeight)
    }

    private fun calculateTextToRender(text: Text): Text{
        if(revealProgress == 0L)return text
        if(revealProgress<0)return Text.literal("$(obf)"+text.string)

        val calculatedText = Text.literal(text.string.substring(0, revealProgress.toInt())+"$(obf)"+text.string.substring(revealProgress.toInt()))

        val currentTime = MinecraftClient.getInstance().world!!.time
        if(currentTime != lastRevealTick){
            lastRevealTick = currentTime
            revealProgress++
            revealProgress = min(text.string.length.toLong(), revealProgress)
            if(text.string.length < revealProgress){
                revealProgress = 0
                return text
            }
        }
        return calculatedText
    }

    @Environment(EnvType.CLIENT)
    protected fun researchButtonClicked(buttonWidget: ButtonWidget){
        if(revealProgress>-1)return
        val player = MinecraftClient.getInstance().player!!
        if(!player.isCreative && !hasInInventory(ingredients, player.inventory))return
        val data = PersistentData.data.getBookData(parent.book)
        data.completedManualQuests.add(entryId)
        PersistentData.save()
        entry.markReadStateDirty()

        //TODO: MinecraftClient.getInstance().soundManager.play(/*[sound]*/)

        var id = advancementId?.asString()?.let(LostArcana::id)
        val criterion = if(id!=null)advancementCriterion?.asString() else null
        if(criterion==null)id = null
        LostArcanaC2SPacketSender.sendThaumonomiconPageUnlockPacket(ingredients, id, criterion)

        revealProgress=1
        lastRevealTick = MinecraftClient.getInstance().world!!.time
        MinecraftClient.getInstance().player!!.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f)
    }


    override fun render(graphics: DrawContext, mouseX: Int, mouseY: Int, pticks: Float) {
        super.render(graphics, mouseX, mouseY, pticks)

        if(revealProgress >= 0)textRenderer = BookTextRenderer(parent, calculateTextToRender(rawText), 0, textureHeight)

        textRenderer!!.render(graphics, mouseX, mouseY)

        if(revealProgress == -1L) for (i in ingredients.indices.reversed()){
            val ingredient = ingredients[i]
            parent.renderIngredient(graphics, GuiBook.PAGE_WIDTH-24-16*i, GuiBook.PAGE_HEIGHT - 34, mouseX, mouseY, ingredient)
        }

        parent.drawCenteredStringNoShadow(graphics, I18n.translate(if(title.isNullOrEmpty())"patchouli.gui.lexicon.objective" else title), GuiBook.PAGE_WIDTH/2, 0, book.headerColor)
        GuiBook.drawSeparator(graphics, book, 0, 12)
    }
}