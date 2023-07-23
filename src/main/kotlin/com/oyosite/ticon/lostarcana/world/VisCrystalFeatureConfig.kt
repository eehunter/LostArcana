package com.oyosite.ticon.lostarcana.world

import com.mojang.serialization.Codec
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Encoder
import com.mojang.serialization.RecordBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.gen.feature.FeatureConfig

class VisCrystalFeatureConfig(var number: Int = 0): FeatureConfig {



    companion object{

        val CODEC = RecordCodecBuilder.create<VisCrystalFeatureConfig> { it.group(Codecs.POSITIVE_INT.fieldOf("number").forGetter(VisCrystalFeatureConfig::number)).apply(it, ::VisCrystalFeatureConfig) }
            // Codec.of(Encoder<VisCrystalFeatureConfig>{ _: VisCrystalFeatureConfig, _: DynamicOps<*>, p: RecordBuilder<*> -> p }, ::VisCrystalFeatureConfig)
            // RecordCodecBuilder.create { it.group(Codecs.POSITIVE_INT.fieldOf("")).apply(it, ::VisCrystalFeatureConfig) }
    }
}