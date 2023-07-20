package com.oyosite.ticon.lostarcana.config

import com.oyosite.ticon.lostarcana.config.UIAnchor.*
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment

object ThaumometerUIConfig {
    @Comment("Which part of the screen should the Thaumometer Guage be anchored to?")
    var thaumometerGuageAnchor: UIAnchor = TOP_LEFT
    @Comment("Distance from the left or right edge of the screen to render the thaumometer gauge. If the anchor is horizontally centered, it is recommended to set this to 0.")
    var thaumometerGuageX: Int = 10
    @Comment("Distance from the top or bottom edge of the screen to render the thaumometer gauge. If the anchor is vertically centered, it is recommended to set this to 0.")
    var thaumometerGuageY: Int = 10


    @CollapsibleObject
    @Comment("These options are intended for custom resource packs. Do not mess with them unless you know what you're doing. Otherwise the graphics will probably break.")
    val technical = Technical()


    class Technical {
        var thaumometerGuageWidth: Int = 10
        var thaumometerGuageHeight: Int = 60
        var thaumometerGuageU: Int = 5
        var thaumometerGuageV: Int = 5
    }
}