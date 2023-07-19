package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.component.LostArcanaComponentEntrypoint
import com.oyosite.ticon.lostarcana.fluid.EssentiaFluid
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.TypeFilter
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.time.times

@Suppress("UnstableApiUsage")
class CrucibleBlockEntity(pos: BlockPos, state: BlockState): BlockEntity(LostArcana.CRUCIBLE_BLOCK_ENTITY, pos, state), SidedStorageBlockEntity {

    val fluidContent: SingleFluidStorage = insertOnlyWithFixedCapacity(81000L){}
    val essentiaContent: MutableMap<FluidVariant, Long> = mutableMapOf()

    val pseudoInventory = CruciblePseudoInventory(this)

    val isHeated: Boolean get() = heat>=8
    var heat: Int = 0

    var dissolveBubbleTime = 0

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidContent

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        val fluidData = NbtCompound()
        fluidContent.writeNbt(fluidData)
        nbt.put("fluidData", fluidData)
        nbt.putInt("heat", heat)
        val essentia = NbtCompound()
        essentiaContent.forEach { variant, amt -> if(variant.nbt!=null)
            essentia.putLong(variant.nbt!!.getString("aspect"), amt)
        }
        nbt.put("essentia", essentia)
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        fluidContent.readNbt(nbt.getCompound("fluidData"))
        heat = nbt.getInt("heat")
        val essentia = nbt.getCompound("essentia")
        essentia.keys.forEach {
            val id = LostArcana.id(it)
            EssentiaFluid.VARIANTS[id.toString()]?.also{ v ->
                essentiaContent[v] = essentia.getLong(it)
            }
        }
    }
    val box = Box(pos.x + 1/8.0, pos.y + 1/8.0, pos.z + 7/8.0, pos.x + 7/8.0, pos.y + 7/8.0, pos.z + 1/8.0)

    companion object{
        val HEAT_BLOCKS = mutableMapOf<Block, Int>(Blocks.TORCH to 2, Blocks.SOUL_TORCH to 4, Blocks.CAMPFIRE to 3, Blocks.SOUL_CAMPFIRE to 6, Blocks.LAVA to 5, BlockRegistry.NITOR to 8)
        fun getHeat(block: BlockState): Int = HEAT_BLOCKS[block.block]?:0


        fun tick(world: World, pos: BlockPos, state: BlockState, be: CrucibleBlockEntity){
            if(world.time%5==0L)be.heat=(be.heat * 0.9).toInt() + getHeat(world.getBlockState(pos.down()))
            if(be.isHeated)world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS)
            if(be.dissolveBubbleTime>5)world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, SoundCategory.BLOCKS)
            if(be.dissolveBubbleTime>0) {

                be.dissolveBubbleTime--
            }
            if(world.time%20==0L){
                world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity::class.java), be.box){LostArcanaComponentEntrypoint.CRAFTING_RESULT_MARKER.getNullable(it)?.flag!=true}
            }
        }


        fun insertOnlyWithFixedCapacity(capacity: Long, onChange: ()->Unit) = object : SingleFluidStorage() {
            override fun getCapacity(variant: FluidVariant?): Long = if(variant?.fluid==Fluids.WATER)capacity else 0L
            override fun onFinalCommit() = onChange()
            override fun extract(extractedVariant: FluidVariant?, maxAmount: Long, transaction: TransactionContext?): Long = 0L
            override fun supportsExtraction(): Boolean = false
        }
    }

}