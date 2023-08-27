package com.oyosite.ticon.lostarcana.block.entity

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.block.ArcanePedestalBlock
import com.oyosite.ticon.lostarcana.block.InfusionDataProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState
import software.bernie.geckolib.util.GeckoLibUtil
import kotlin.math.min

class ArcanePedestalBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(LostArcana.ARCANE_PEDESTAL_BLOCK_ENTITY, pos, state), Inventory, InfusionDataProvider, GeoBlockEntity {

    var stack = ItemStack.EMPTY

    override fun clear() { stack = ItemStack.EMPTY }

    override fun size(): Int = 1

    override fun isEmpty(): Boolean = stack.isEmpty

    override fun getStack(slot: Int): ItemStack = if(slot==0)stack else ItemStack.EMPTY

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        if(slot!=0)return ItemStack.EMPTY
        return stack.copy().apply{ count = min(count, amount); stack.decrement(amount) }
    }

    override fun removeStack(slot: Int): ItemStack = if(slot==0)stack.also{stack = ItemStack.EMPTY} else ItemStack.EMPTY

    override fun setStack(slot: Int, stack: ItemStack?) {
        if(slot!=0)return
        this.stack = stack?: ItemStack.EMPTY
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean = true


    override fun writeNbt(nbt: NbtCompound) {
        Inventories.writeNbt(nbt, DefaultedList.ofSize(1, ItemStack.EMPTY).apply{set(0, stack)})
    }

    override fun readNbt(nbt: NbtCompound) {
        val dummyStacks = DefaultedList.ofSize(1, ItemStack.EMPTY)
        Inventories.readNbt(nbt, dummyStacks)
        stack = dummyStacks[0]
    }

    override val itemStacks: List<ItemStack>
        get() = listOf(stack)

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = NbtCompound().also(this::writeNbt)

    val DEPLOY: RawAnimation = RawAnimation.begin().thenPlayAndHold("arcane_pedestal.deploy")//.thenPlay("misc.deploy").thenLoop("misc.idle")
    private val cache = GeckoLibUtil.createInstanceCache(this)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(AnimationController(this, this::deployAnimController))
    }
    private fun deployAnimController(state: AnimationState<ArcanePedestalBlockEntity>): PlayState {
        return state.setAndContinue(DEPLOY)
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

    override fun stability(other: InfusionDataProvider?): Double {
        other?:return -1.0
        if(other !is ArcanePedestalBlockEntity) return 0.0
        return if(isEmpty xor other.isEmpty) -1.0 else 1.0
    }

}