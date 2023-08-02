package com.oyosite.ticon.lostarcana.aspect;

import java.util.HashMap;
import java.util.Map;

public interface BakedEssentiaProvider {
    Map<Aspect, Long> getBakedEssentia();
    void setBakedEssentia(Map<Aspect, Long> essentia);
}
