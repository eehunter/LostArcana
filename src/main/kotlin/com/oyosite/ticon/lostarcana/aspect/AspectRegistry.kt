package com.oyosite.ticon.lostarcana.aspect

import com.oyosite.ticon.lostarcana.LostArcana
import com.oyosite.ticon.lostarcana.fluid.EssentiaFluid
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.item.Item
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.item.Items.*

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
        A("aurum", 0xf5c6f6, "praecantatio", "aer")
        A("alkima", 0x24ad9d, "praecantatio", "aqua")
        A("vitium", 0x790180, "perditio", "praecantatio")
        A("tenebrae", 0x232323, "vacuos", "lux")
        A("alienis", 0x854f84, "vacuos", "tenebrae")
        A("volatus", 0xe8e9d6, "aer", "motus")
        A("herba", 0x038b02, "victus", "terra")
        A("instrumentum", 0x4243db, "metallum", "potentia")
        A("fabrico", 0x7e9f7d, "permutatio", "instrumentum")
        A("machina", 0x8080a0, "motus", "instrumentum")
        A("vinculum", 0x978186, "motus", "perditio")
        A("spiritus", 0x444c50, "victus", "mortuus")
        A("cognito", 0xf79781, "ignis", "spiritus")
        A("sensus", 0xc5fcc3, "aer", "spiritus")
        A("aversio", 0xc14f4f, "spiritus", "perditio")
        A("praemunio", 0x00beb9, "spiritus", "terra")
        A("desiderium", 0xe5be42, "spiritus", "vacuos")
        A("exanimis", 0x384000, "motus", "mortuus")
        A("bestia", 0x9e640a, "motus", "victus")
        A("humanus", 0xffd8c4, "spiritus", "victus")
        reloadEssentiaVariants()

        TORCH("lux" to 5)
        IRON_INGOT("metallum" to 15)

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

    operator fun Item.invoke(vararg aspects: Pair<String, Number>){
        (this as BakedEssentiaProvider).bakedEssentia = mapOf(*aspects.map{ASPECTS[LostArcana.id(it.first)]!! to it.second.toLong()}.toTypedArray())
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