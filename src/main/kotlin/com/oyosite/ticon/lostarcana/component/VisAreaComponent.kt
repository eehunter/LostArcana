package com.oyosite.ticon.lostarcana.component

import dev.onyxstudios.cca.api.v3.component.ComponentV3
import com.oyosite.ticon.lostarcana.vis.MAX_CHUNK_VIS

interface VisAreaComponent: ComponentV3 {
    /**This is the natural level below which vis will passively increase. The hard maximum amount of vis in a chunk is always equal to [MAX_CHUNK_VIS]*/
    val visCap: Int
    var vis: Int
    var flux: Int
    val visPressure: Int get() = vis+flux/2-visCap
}