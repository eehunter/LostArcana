package com.oyosite.ticon.lostarcana.config

import com.oyosite.ticon.lostarcana.config.UIAnchor.*
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment

object ThaumometerUIConfig {
    @Comment("Which part of the screen should the Thaumometer Guage be anchored to.")
    var thaumometerGuageAnchor: UIAnchor = TOP_LEFT
    var thaumometerGuageX: Int = 10
    var thaumometerGuageY: Int = 10
}