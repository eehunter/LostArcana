package com.oyosite.ticon.lostarcana.config

import com.oyosite.ticon.lostarcana.LostArcana
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject

@Config(name = "${LostArcana.MODID}_client")
class ClientConfig : ConfigData{
    @CollapsibleObject(startExpanded = true)
    val thaumometer = ThaumometerUIConfig

}