package com.oyosite.ticon.lostarcana.client

import com.oyosite.ticon.lostarcana.config.LostArcanaConfig
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig

object ModMenuEntrypoint: ModMenuApi{
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = ConfigScreenFactory { AutoConfig.getConfigScreen(LostArcanaConfig::class.java, it).get() }
}