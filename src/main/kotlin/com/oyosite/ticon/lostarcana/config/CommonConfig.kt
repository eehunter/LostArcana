package com.oyosite.ticon.lostarcana.config

import com.oyosite.ticon.lostarcana.LostArcana
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config

@Config(name = "${LostArcana.MODID}_common")
class CommonConfig : ConfigData {
    var maxVisInChunk: Int = 100
}