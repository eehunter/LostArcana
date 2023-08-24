package com.oyosite.ticon.lostarcana.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState
import software.bernie.geckolib.util.GeckoLibUtil


class InfusionPillarBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(TODO(), pos, state), GeoBlockEntity {
    val DEPLOY: RawAnimation = RawAnimation.begin().thenPlay("misc.deploy").thenLoop("misc.idle")
    private val cache = GeckoLibUtil.createInstanceCache(this)
    override fun registerControllers(ontrollers: AnimatableManager.ControllerRegistrar) {
        ontrollers.add(AnimationController(this, this::deployAnimController))
    }


    private fun deployAnimController(state: AnimationState<InfusionPillarBlockEntity>): PlayState {
        return state.setAndContinue(DEPLOY)
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

}