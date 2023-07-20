package com.oyosite.ticon.lostarcana.client

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.component.LostArcanaComponentEntrypoint
import com.oyosite.ticon.lostarcana.config.UIAnchor
import com.oyosite.ticon.lostarcana.config.UIAnchor.*
import com.oyosite.ticon.lostarcana.vis.MAX_CHUNK_VIS
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext


val THAUMOMETER_UI_TEXTURE = LostArcana.id("textures/gui/hud/thaumometer.png")

val anchor get() = LostArcana.CLIENT_CONFIG.thaumometer.thaumometerGuageAnchor
val relX get() = LostArcana.CLIENT_CONFIG.thaumometer.thaumometerGuageX
val relY get() = LostArcana.CLIENT_CONFIG.thaumometer.thaumometerGuageY
val wid get() = LostArcana.CLIENT_CONFIG.thaumometer.technical.thaumometerGuageWidth
val hei get() = LostArcana.CLIENT_CONFIG.thaumometer.technical.thaumometerGuageHeight

fun onHudRender(dc: DrawContext, tickDelta: Float) = dc.run{
    val client: MinecraftClient = MinecraftClient.getInstance()
    val player = client.player?:return
    if(player.attributes.getValue(LostArcana.AURA_VISION)==0.0)return
    val chunk = player.world.getChunk(player.chunkPos.x, player.chunkPos.z)
    val vis = LostArcanaComponentEntrypoint.CHUNK_VIS.getNullable(chunk)?:return
    drawTexture(THAUMOMETER_UI_TEXTURE, thaumometerGuageX+2, thaumometerGuageY+4 + ((hei-8) * (1 - vis.vis/MAX_CHUNK_VIS.toFloat())).toInt(), wid-4, ((hei-8) * (vis.vis/MAX_CHUNK_VIS.toDouble())).toInt(), 30f, 7f + ((hei-8) * (1 - vis.vis/MAX_CHUNK_VIS.toFloat())), wid-4, ((hei-8) * (vis.vis/MAX_CHUNK_VIS.toDouble())).toInt(), 128, 128)
    if(vis.flux>0)drawTexture(THAUMOMETER_UI_TEXTURE, thaumometerGuageX+2, thaumometerGuageY+4 + ((hei-8) * (1 - (vis.vis-vis.flux)/MAX_CHUNK_VIS.toFloat())).toInt(), wid-4, ((hei-8) * (vis.flux/MAX_CHUNK_VIS.toDouble())).toInt(), 50f, 7f + ((hei-8) * (1 - (vis.vis-vis.flux)/MAX_CHUNK_VIS.toFloat())), wid-4, ((hei-8) * (vis.flux/MAX_CHUNK_VIS.toDouble())).toInt(), 128, 128)

    drawTexture(THAUMOMETER_UI_TEXTURE, thaumometerGuageX, thaumometerGuageY, wid, hei, 5f, 5f, wid, hei, 128, 128)

    drawTexture(THAUMOMETER_UI_TEXTURE, thaumometerGuageX, thaumometerGuageY+3+((hei-8) * (1 - vis.visCap/MAX_CHUNK_VIS.toDouble())).toInt(), wid, 3, 5f, 72f, wid, 3, 128, 128)

}

val DrawContext.thaumometerGuageX: Int get() = when(anchor){
    TOP_RIGHT, RIGHT, BOTTOM_RIGHT -> this.scaledWindowWidth-relX-wid
    TOP_MIDDLE, CENTER, BOTTOM_MIDDLE -> (this.scaledWindowWidth-relX-wid)/2
    TOP_LEFT, LEFT, BOTTOM_LEFT -> relX
}//if(anchor in listOf(TOP_RIGHT, RIGHT, BOTTOM_RIGHT))this.scaledWindowWidth-relX-wid else relX


val DrawContext.thaumometerGuageY: Int get() = when(anchor){
    BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT -> this.scaledWindowHeight-relY-hei
    LEFT, CENTER, RIGHT -> (this.scaledWindowHeight-relY-hei)/2
    TOP_LEFT, TOP_MIDDLE, TOP_RIGHT -> relY
}//if(anchor in listOf(BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT))this.scaledWindowHeight-relY-hei else relY
