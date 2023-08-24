package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.InfusionDataProvider
import com.oyosite.ticon.lostarcana.block.InfusionPillar
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class RunicMatrixBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(LostArcana.RUNIC_MATRIX_BLOCK_ENTITY, pos, state) {

    val infusionAltarHorizontalRadius: Int = 8
    val infusionAltarVerticalRadius: Int = 5

    var active = false; private set
    var crafting = false; private set
    var angle: Float = 0f
    var tilt: Float = 0f
    var lastRenderTime = 0L

    val infusionDataProviders: Array<Array<Array<List<InfusionDataProvider>?>>> = Array(2*infusionAltarHorizontalRadius+1){ Array(2*infusionAltarVerticalRadius+1){Array(2*infusionAltarHorizontalRadius+1){null} } }

    fun refreshStructure(){
        active = checkCoreStructure()

        if(active && world!=null){
            val world = this.world!!
            for(x in infusionDataProviders.indices) for(y in infusionDataProviders[x].indices) for(z in infusionDataProviders[x][y].indices){
                val p = BlockPos(pos.x+x-infusionAltarHorizontalRadius, pos.y+y-infusionAltarVerticalRadius-2, pos.z+z-infusionAltarHorizontalRadius)
                if(p == pos.down(2))continue
                infusionDataProviders[x][y][z] = world.getInfusionDataProviders(p)
            }
        }
    }

    private fun checkCoreStructure(): Boolean{
        if(world==null)return false
        val world = this.world!!
        if(world.getBlockState(pos.down(2)).block != BlockRegistry.ARCANE_PEDESTAL) return false
        for(dir in InfusionPillar.DIRECTION.values){
            if(dir == Direction.DOWN)continue
            val pillarPos = pos.down(2).add(dir.vector).add(dir.vector.z, 0, -dir.vector.x)
            val pillar = world.getBlockState(pillarPos)
            val pillarTop = world.getBlockState(pillarPos.up())
            if(pillar.block != BlockRegistry.INFUSION_PILLAR || pillarTop.block != BlockRegistry.INFUSION_PILLAR)return false
            if(pillar[InfusionPillar.DIRECTION] != dir || pillarTop[InfusionPillar.DIRECTION] != Direction.DOWN) return false
        }


        return true
    }

    fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        refreshStructure()
        if(active) return ActionResult.PASS
        if(player.getStackInHand(hand).item == ItemRegistry.SALIS_MUNDIS) return attemptConstruction(player).also { if(it == ActionResult.SUCCESS && !player.isCreative)player.getStackInHand(hand).decrement(1) }


        return ActionResult.PASS
    }

    private fun attemptConstruction(player: PlayerEntity?): ActionResult{
        val world = this.world?: return ActionResult.FAIL
        if(checkCoreStructure()) return ActionResult.PASS
        for(x in listOf(-1, 1)) for(y in -2..-1) for(z in listOf(-1, 1)){
            if(!testPillarBlock(x, y, z, player))return ActionResult.FAIL
        }
        if(world.getBlockState(pos.down(2)).block != BlockRegistry.ARCANE_PEDESTAL)return ActionResult.FAIL

        for(dir in InfusionPillar.DIRECTION.values){
            if(dir == Direction.DOWN)continue
            val pillarPos = pos.down(2).add(dir.vector).add(dir.vector.z, 0, -dir.vector.x)
            world.setBlockState(pillarPos, BlockRegistry.INFUSION_PILLAR.defaultState.with(InfusionPillar.DIRECTION, dir))
            world.setBlockState(pillarPos.up(), BlockRegistry.INFUSION_PILLAR.defaultState.with(InfusionPillar.DIRECTION, Direction.DOWN))
        }

        return ActionResult.SUCCESS
    }

    private fun testPillarBlock(x: Int, y: Int, z: Int, player: PlayerEntity?): Boolean{
        val p = BlockPos(pos.x+x, pos.y+y, pos.z+z)
        val pillarValid = world?.let{it.getBlockState(p).let{state-> state.block==BlockRegistry.INFUSION_PILLAR && ((state[InfusionPillar.DIRECTION]==Direction.DOWN) xor (y==-2))}}?:false
        return player?.canModifyAt(world, p) != false && (world?.getBlockState(p)?.block == BlockRegistry.ARCANE_STONE || pillarValid)
    }

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putBoolean("active", active)
    }

    override fun readNbt(nbt: NbtCompound) {
        active = nbt.getBoolean("active")
    }

}