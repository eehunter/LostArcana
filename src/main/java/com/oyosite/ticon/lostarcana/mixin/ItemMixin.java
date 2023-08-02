package com.oyosite.ticon.lostarcana.mixin;

import com.oyosite.ticon.lostarcana.aspect.Aspect;
import com.oyosite.ticon.lostarcana.aspect.BakedEssentiaProvider;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(Item.class)
public class ItemMixin implements BakedEssentiaProvider {
    @Unique
    private Map<Aspect, Long> essentia = new HashMap<>();

    @Unique
    @Override
    public Map<Aspect, Long> getBakedEssentia() {
        return essentia;
    }

    @Unique
    @Override
    public void setBakedEssentia(Map<Aspect, Long> essentia) {
        this.essentia = essentia;
    }
}
