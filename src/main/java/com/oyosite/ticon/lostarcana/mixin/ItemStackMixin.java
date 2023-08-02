package com.oyosite.ticon.lostarcana.mixin;

import com.oyosite.ticon.lostarcana.aspect.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements EssentiaProvider {

    @Shadow
    public abstract Item getItem();

    @NotNull
    @Override
    public Map<Aspect, Long> getEssentia() {
        if(this.getItem() instanceof ItemStackEssentiaProvider) return ((ItemStackEssentiaProvider)this.getItem()).getEssentia((ItemStack)(Object)(this));
        return ((BakedEssentiaProvider)this.getItem()).getBakedEssentia();
    }
}
