package com.oyosite.ticon.lostarcana.component

import com.oyosite.ticon.lostarcana.vis.MAX_CHUNK_VIS
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkStatus
import net.minecraft.world.chunk.WorldChunk
import kotlin.random.Random

class VisChunkImpl(val chunk: Chunk): VisAreaComponent, AutoSyncedComponent, ServerTickingComponent {
    private var baseVisCap: Int = Random(hashCode()).nextInt(MAX_CHUNK_VIS/5, MAX_CHUNK_VIS/2)
    //private var loaded = false

    override val visCap: Int
        get() = baseVisCap
    private var visImpl: Int = baseVisCap
    private var fluxImpl: Int = 0
    override var vis: Int
        get() = visImpl
        set(value) {visImpl=value; updateFlux(); println("Set vis to $value") }
    override var flux: Int
        get() = fluxImpl
        set(value) {fluxImpl=value; updateFlux()}

    override fun equals(other: Any?): Boolean {
        if(other !is VisChunkImpl) return false
        return this.hashCode() == other.hashCode() && this.visCap == other.visCap && this.vis == other.vis
    }

    private fun updateFlux(){
        if(flux+vis<100 && flux>=0 && vis>=0)return
        fluxImpl = fluxImpl.coerceIn(0..MAX_CHUNK_VIS)
        visImpl = visImpl.coerceIn(0..MAX_CHUNK_VIS-fluxImpl)
    }
    override fun readFromNbt(tag: NbtCompound) {
        try{
            if(chunk.pos.x == 0 && chunk.pos.z == 0)println("Reading $tag")
        }catch (_: NullPointerException){}
        if(tag.contains("baseVisCap"))baseVisCap = tag.getInt("baseVisCap")
        visImpl = if(tag.contains("vis"))tag.getInt("vis") else baseVisCap
        fluxImpl = if(tag.contains("flux"))tag.getInt("flux") else 0
        //loaded = true
        try{
            if(chunk.pos.x == 0 && chunk.pos.z == 0)println("Vis: $visImpl")
        }catch (_: NullPointerException){}
    }

    override fun writeToNbt(tag: NbtCompound) {
        //if(!loaded)return
        try{
            //if(vis == baseVisCap)return
            tag.putInt("baseVisCap", baseVisCap)
            tag.putInt("vis", visImpl)
            tag.putInt("flux", fluxImpl)
            if(chunk.pos.x == 0 && chunk.pos.z == 0)println("Writing $tag")
        }catch (_: NullPointerException){}
    }

    override fun serverTick() {
        //naturalVisFlow()


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

    override fun hashCode(): Int {
        return try{
            MathHelper.hashCode(chunk.pos.x, 0, chunk.pos.z).toInt()
        } catch (_: NullPointerException){
            super.hashCode()
        }
    }

    override fun markDirty(){
        LostArcanaComponentEntrypoint.CHUNK_VIS.sync(chunk)
    }

    override fun shouldSyncWith(player: ServerPlayerEntity): Boolean {
        if(chunk !is WorldChunk) return super.shouldSyncWith(player)
        return player.world == chunk.world && player.chunkPos.getChebyshevDistance(chunk.pos) < 20
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
                markDirty()
                otherComp.markDirty()
            }
            if(flux==0 || Random.nextFloat()>0.5){
                vis-=1
                otherComp.vis+=1
                markDirty()
                otherComp.markDirty()
            }
        }

    }


}