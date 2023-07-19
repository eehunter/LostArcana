package com.oyosite.ticon.lostarcana.component

import com.oyosite.ticon.lostarcana.vis.MAX_CHUNK_VIS
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkStatus
import net.minecraft.world.chunk.WorldChunk
import kotlin.random.Random

class VisChunkImpl(val chunk: Chunk): VisAreaComponent, AutoSyncedComponent, ServerTickingComponent {
    private var baseVisCap: Int = Random.nextInt(MAX_CHUNK_VIS/5, MAX_CHUNK_VIS/2)

    override val visCap: Int
        get() = baseVisCap
    private var _vis: Int = baseVisCap
    private var _flux: Int = baseVisCap
    override var vis: Int
        get() = _vis
        set(value) {_vis=value; updateFlux()}
    override var flux: Int
        get() = _flux
        set(value) {_flux=value; updateFlux()}

    private fun updateFlux(){
        if(flux+vis<100 && flux>0 && vis>0)return
        _flux = _flux.coerceIn(0..MAX_CHUNK_VIS)
        _vis = _vis.coerceIn(0..MAX_CHUNK_VIS-_flux)
    }
    override fun readFromNbt(tag: NbtCompound) {
        if(tag.contains("baseVisCap"))baseVisCap = tag.getInt("baseVisCap")
        vis = if(tag.contains("vis"))tag.getInt("vis") else baseVisCap
        flux = if(tag.contains("flux"))tag.getInt("flux") else 0
    }

    override fun writeToNbt(tag: NbtCompound) {
        tag.putInt("baseVisCap", baseVisCap)
        tag.putInt("vis", vis)
        tag.putInt("flux", flux)
    }

    override fun serverTick() {
        naturalVisFlow()


    }
    enum class Dir(val x: Int, val z: Int){
        NW(-1, -1),
        N(0, -1),
        NE(1, -1),
        W(-1, 0),
        E(1, 0),
        SW(-1, 1),
        S(0, 1),
        SE(1, 1)
    }
    private fun naturalVisFlow(){
        if(chunk !is WorldChunk)return
        val world = chunk.world
        if(world.time%20 != 0L)return
        if(Random.nextFloat()<0.8)return
        val dirs = Dir.values().copyOf().apply(Array<Dir>::shuffle)
        for(dir in dirs){
            val other = world.getChunk(chunk.pos.x+dir.x, chunk.pos.z+dir.z, ChunkStatus.FULL, false)?:continue
            val otherComp = LostArcanaComponentEntrypoint.CHUNK_VIS.getNullable(other)?:continue
            if(visPressure<=otherComp.visPressure)continue
            if(flux>0) {
                flux-=1
                otherComp.flux+=1
            }
            if(flux==0 || Random.nextFloat()>0.5){
                vis-=1
                otherComp.vis+=1
            }
        }

    }


}