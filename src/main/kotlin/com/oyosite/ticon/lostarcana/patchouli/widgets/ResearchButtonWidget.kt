package com.oyosite.ticon.lostarcana.patchouli.widgets

import com.oyosite.ticon.lostarcana.patchouli.GatedResearchPage
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

class ResearchButtonWidget(x: Int, y: Int, width: Int, height: Int, message: Text?, val researchPage: GatedResearchPage, onPress: PressAction?, narrationSupplier: NarrationSupplier?) : ButtonWidget(x, y, width, height, message, onPress, narrationSupplier) {
    override fun renderButton(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        if(researchPage.revealProgress >= 0)return
        super.renderButton(context, mouseX, mouseY, delta)
    }
}