package com.oyosite.ticon.lostarcana.client.blockentity

import net.minecraft.client.util.math.MatrixStack

operator fun <T> MatrixStack.invoke(toDo: MatrixStack.()->T): T { push(); return toDo().also{pop()} }