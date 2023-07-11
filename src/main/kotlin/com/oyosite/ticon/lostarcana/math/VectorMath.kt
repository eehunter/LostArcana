package com.oyosite.ticon.lostarcana.math

import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i

operator fun Vec3i.minus(other: Vec3i) = Vec3i(x-other.x, y-other.y, z-other.z)
operator fun Vec3i.plus(other: Vec3i) = Vec3i(x+other.x, y+other.y, z+other.z)

val Vec3d.vec3i get() = Vec3i(x.toInt(),y.toInt(),z.toInt())


operator fun Vec3d.minus(other: Vec3d) = Vec3d(x-other.x, y-other.y, z-other.z)
operator fun Vec3d.plus(other: Vec3d) = Vec3d(x+other.x, y+other.y, z+other.z)