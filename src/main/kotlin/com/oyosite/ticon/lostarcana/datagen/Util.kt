package com.oyosite.ticon.lostarcana.datagen

import com.oyosite.ticon.lostarcana.aspect.Aspect
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

fun FabricLanguageProvider.TranslationBuilder.add(aspect: Aspect) = add("aspect.${aspect.id.namespace}.${aspect.id.path}", aspect.id.path.capitalize())