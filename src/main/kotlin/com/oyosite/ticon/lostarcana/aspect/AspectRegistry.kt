package com.oyosite.ticon.lostarcana.aspect

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.fluid.EssentiaFluid
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier

@Suppress("UnstableApiUsage")
object AspectRegistry: Map<Identifier, Aspect> {
    val ASPECTS = mutableMapOf(*mutableListOf(
        "aer"      to 0xf6f775,
        "ignis"    to 0xff6608,
        "perditio" to 0x4d5049,
        "terra"    to 0x66ee08,
        "aqua"     to 0x4ce6ff,
        "ordo"     to 0xd8d6fb,
    ).map{LostArcana.id(it.first) to Aspect(LostArcana.id(it.first), it.second)}.toTypedArray())

    val PRIMAL_ASPECTS = mutableMapOf(*Array(6, ASPECTS.entries.toList()::get).map { it.key to it.value }.toTypedArray())

    init{
        A("vacuos", 0x858585, "aer", "perditio")
        A("lux", 0xfcfebb, "aer", "ignis")
        A("motus", 0xc8befa, "aer", "ordo")
        A("gelum", 0xe3fffa, "ignis", "perditio")
        A("vitrius", 0x7fffff, "terra", "aer")
        A("metallum", 0xb5b6cc, "terra", "ordo")
        A("victus", 0xc10e16, "terra", "aqua")
        A("mortuus", 0x680106, "victus", "perditio")
        A("potentia", 0xc9fbfc, "ordo", "ignis")
        A("permutatio", 0x598360, "perditio", "ordo")
        A("praecantatio", 0xcd00ff, "potentia", "aer")
        A("aurum", 0, "praecantatio", "aer")
        A("alkima", 0, "praecantatio", "aqua")
        A("vitium", 0, "perditio", "praecantatio")
        A("tenebrae", 0, "vacuos", "lux")
        A("alienis", 0, "vacuos", "tenebrae")
        A("volatus", 0, "aer", "motus")
        A("herba", 0, "victus", "terra")
        reloadEssentiaVariants()
    }

    fun reloadEssentiaVariants(){
        EssentiaFluid.VARIANTS.clear()
        ASPECTS.forEach { id, aspect ->
            EssentiaFluid.VARIANTS[id.toString()] = FluidVariant.of(LostArcana.ESSENTIA, NbtCompound().apply { putString("aspect", id.toString()); putInt("color", aspect.color) })
        }
    }

    fun A(name: String, color: Int, component1: String, component2: String){
        val c1 = ASPECTS[LostArcana.id(component1)]?: return
        val c2 = ASPECTS[LostArcana.id(component2)]?: return
        ASPECTS[LostArcana.id(name)] = Aspect(LostArcana.id(name), color, c1 to c2)
    }

    fun registerAspect(aspect: Aspect) = ASPECTS.put(aspect.id, aspect)
    override val entries: Set<Map.Entry<Identifier, Aspect>>
        get() = ASPECTS.entries
    override val keys: Set<Identifier>
        get() = ASPECTS.keys
    override val size: Int
        get() = ASPECTS.size
    override val values: Collection<Aspect>
        get() = ASPECTS.values

    override fun isEmpty(): Boolean = ASPECTS.isEmpty()

    override operator fun get(key: Identifier): Aspect? = ASPECTS[key]

    override fun containsValue(value: Aspect): Boolean = ASPECTS.containsValue(value)

    override fun containsKey(key: Identifier): Boolean = ASPECTS.containsKey(key)
}