package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

@Suppress("UnstableApiUsage")
class CrucibleBlockEntity(pos: BlockPos, state: BlockState): BlockEntity(LostArcana.CRUCIBLE_BLOCK_ENTITY, pos, state), SidedStorageBlockEntity {

    val fluidContent: SingleFluidStorage = insertOnlyWithFixedCapacity(81000L){}
    
    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidContent

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        val fluidData = NbtCompound()
        fluidContent.writeNbt(fluidData)
        nbt.put("fluidData", fluidData)
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        fluidContent.readNbt(nbt.getCompound("fluidData"))
    }

    companion object{
        val HEAT_BLOCKS = mutableMapOf<Block, Int>()
        fun getHeat(block: BlockState){

        }


        fun insertOnlyWithFixedCapacity(capacity: Long, onChange: ()->Unit) = object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant?): Long = if(variant?.fluid==Fluids.WATER)capacity else 0L
            override fun onFinalCommit() = onChange()
            override fun extract(extractedVariant: FluidVariant?, maxAmount: Long, transaction: TransactionContext?): Long = 0L
            override fun supportsExtraction(): Boolean = false
        }
    }

}