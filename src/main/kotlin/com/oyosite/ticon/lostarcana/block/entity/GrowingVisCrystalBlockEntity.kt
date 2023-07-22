package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.Aspect
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.GrowingVisCrystalBlock
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class GrowingVisCrystalBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(LostArcana.VIS_CRYSTAL_BLOCK_ENTITY, pos, state) {

    private val offsets: Array<Vec3d?> = arrayOfNulls(6)

    val color: Int get() = aspect.color

    var aspect: Aspect = AspectRegistry.PRIMAL_ASPECTS.values.random(); set(value){
        field = value
        if(world?.isClient == false)return

    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        return NbtCompound().apply{putString("aspect", aspect.id.toString())}
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? = BlockEntityUpdateS2CPacket.create(this)


    override fun writeNbt(nbt: NbtCompound) {
        nbt.putString("aspect", aspect.id.toString())
    }

    override fun readNbt(nbt: NbtCompound) {
        println(LostArcana.id(nbt.getString("aspect")))
        println(AspectRegistry[LostArcana.id(nbt.getString("aspect"))])
        aspect = AspectRegistry[LostArcana.id(nbt.getString("aspect"))]?:aspect
    }



    fun getOffset(i: Int): Vec3d = (offsets[i-1]?:(world?.getBlockState(pos)?.with(GrowingVisCrystalBlock.CRYSTALS, i)?.getModelOffset(world, pos)?.also { offsets[i-1] = it }))?: Vec3d.ZERO




    var crystals = state[GrowingVisCrystalBlock.CRYSTALS]; set(value) {
        field = value
        world?.setBlockState(pos, BlockRegistry.GROWING_VIS_CRYSTAL.defaultState.with(GrowingVisCrystalBlock.CRYSTALS, value))
    }



}