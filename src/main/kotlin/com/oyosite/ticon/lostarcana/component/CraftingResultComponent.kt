package com.oyosite.ticon.lostarcana.component

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.nbt.NbtCompound

class CraftingResultComponent(override var flag: Boolean) : BooleanComponent, AutoSyncedComponent {

    override fun readFromNbt(tag: NbtCompound) {flag = tag.getBoolean("flag")}

    override fun writeToNbt(tag: NbtCompound) {tag.putBoolean("flag", flag)}
}