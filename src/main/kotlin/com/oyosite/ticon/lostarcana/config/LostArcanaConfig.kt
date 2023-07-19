package com.oyosite.ticon.lostarcana.config

import com.oyosite.ticon.lostarcana.LostArcana
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject
import me.shedaniel.autoconfig.serializer.PartitioningSerializer

@Config(name = LostArcana.MODID)
class LostArcanaConfig : PartitioningSerializer.GlobalData() {
    @Category("${LostArcana.MODID}_client")
    @TransitiveObject
    var clientConfig = ClientConfig()

    @Category("${LostArcana.MODID}_common")
    @TransitiveObject
    var commonConfig = CommonConfig()
}