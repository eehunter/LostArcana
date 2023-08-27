package com.oyosite.ticon.lostarcana

import com.oyosite.ticon.lostarcana.aspect.AspectRegistry
import com.oyosite.ticon.lostarcana.block.BlockRegistry
import com.oyosite.ticon.lostarcana.block.entity.*
import com.oyosite.ticon.lostarcana.config.LostArcanaConfig
import com.oyosite.ticon.lostarcana.fluid.EssentiaFluid
import com.oyosite.ticon.lostarcana.item.ItemRegistry
import com.oyosite.ticon.lostarcana.recipe.AlchemyRecipe
import com.oyosite.ticon.lostarcana.recipe.ArcaneWorkbenchRecipe
import com.oyosite.ticon.lostarcana.recipe.NitorDyeRecipe
import com.oyosite.ticon.lostarcana.recipe.UniqueVisCrystalRecipe
import com.oyosite.ticon.lostarcana.world.VisCrystalFeature
import com.oyosite.ticon.lostarcana.world.VisCrystalFeatureConfig
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import me.shedaniel.autoconfig.serializer.PartitioningSerializer
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.registry.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import net.minecraft.util.math.intprovider.UniformIntProvider
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.ConfiguredFeatures
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import software.bernie.geckolib.GeckoLib
import kotlin.jvm.optionals.getOrNull

object LostArcana : ModInitializer {
    const val MODID = "lostarcana"

    @JvmStatic
    val LOGGER: Logger = LogManager.getLogger("Lost Arcana")

    val ARCANE_WORKBENCH_BLOCK_ENTITY: BlockEntityType<ArcaneWorkbenchBlockEntity> = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("arcane_workbench"), FabricBlockEntityTypeBuilder.create(::ArcaneWorkbenchBlockEntity).build())
    val ARCANE_WORKBENCH_SCREEN_HANDLER: ScreenHandlerType<ArcaneWorkbenchScreenHandler> = Registry.register(Registries.SCREEN_HANDLER, id("arcane_workbench"), ScreenHandlerType<ArcaneWorkbenchScreenHandler>(::ArcaneWorkbenchScreenHandler, FeatureFlags.VANILLA_FEATURES))

    val CRUCIBLE_BLOCK_ENTITY: BlockEntityType<CrucibleBlockEntity> = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("crucible"), FabricBlockEntityTypeBuilder.create(::CrucibleBlockEntity).addBlock(BlockRegistry.CRUCIBLE).build())
    val VIS_CRYSTAL_BLOCK_ENTITY: BlockEntityType<GrowingVisCrystalBlockEntity> = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("growing_vis_crystal"), FabricBlockEntityTypeBuilder.create(::GrowingVisCrystalBlockEntity, BlockRegistry.GROWING_VIS_CRYSTAL).build())
    val RUNIC_MATRIX_BLOCK_ENTITY: BlockEntityType<RunicMatrixBlockEntity> = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("runic_matrix"), FabricBlockEntityTypeBuilder.create(::RunicMatrixBlockEntity, BlockRegistry.RUNIC_MATRIX).build())
    val ARCANE_PEDESTAL_BLOCK_ENTITY: BlockEntityType<ArcanePedestalBlockEntity> = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("arcane_pedestal"), FabricBlockEntityTypeBuilder.create(::ArcanePedestalBlockEntity, BlockRegistry.ARCANE_PEDESTAL).build())
    val INFUSION_PILLAR_BLOCK_ENTITY: BlockEntityType<InfusionPillarBlockEntity> = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("infusion_pillar"), FabricBlockEntityTypeBuilder.create(::InfusionPillarBlockEntity, BlockRegistry.INFUSION_PILLAR).build())

    val ESSENTIA = Registry.register(Registries.FLUID, id("essentia"), EssentiaFluid())

    val CONFIG get() = AutoConfig.getConfigHolder(LostArcanaConfig::class.java).config
    val CLIENT_CONFIG get() = CONFIG.clientConfig
    val COMMON_CONFIG get() = CONFIG.commonConfig

    val AURA_VISION = ClampedEntityAttribute("attribute.name.generic.$MODID.aura_vision", 0.0, 0.0, 1.0).setTracked(true)

    val VALID_CRYSTAL_GROWTH_BASES = TagKey.of(RegistryKeys.BLOCK, id("valid_crystal_growth_bases"))

    val VIS_CRYSTAL_FEATURE_ID = id("vis_crystal_feature")
    val VIS_CRYSTAL_FEATURE = VisCrystalFeature()
    val CONFIGURED_VIS_CRYSTAL_FEATURE = ConfiguredFeature(VIS_CRYSTAL_FEATURE, DefaultFeatureConfig.INSTANCE)
    val PLACED_VIS_CRYSTAL_FEATURE = PlacedFeature(RegistryEntry.of(CONFIGURED_VIS_CRYSTAL_FEATURE), listOf(CountPlacementModifier.of(UniformIntProvider.create(10, 20)), SquarePlacementModifier.of()))

    override fun onInitialize(){
        //println("ItemRegistry class: ${ItemRegistry.clazz.name}")
        AspectRegistry
        BlockRegistry.registerAll()
        ItemRegistry.registerAll()
        //AlchemyRecipe.Type
        Registry.register(Registries.RECIPE_TYPE, id("alchemy"), AlchemyRecipe.Type)
        Registry.register(Registries.RECIPE_SERIALIZER, id("alchemy"), AlchemyRecipe.Serializer)

        Registry.register(Registries.RECIPE_TYPE, id("arcane_workbench"), ArcaneWorkbenchRecipe.Type)
        Registry.register(Registries.RECIPE_SERIALIZER, id("arcane_workbench"), ArcaneWorkbenchRecipe.Serializer)
        //Registry.register(Registries.RECIPE_TYPE, id("nitor_dye"), NitorDyeRecipe.Type)
        Registry.register(Registries.RECIPE_SERIALIZER, id("nitor_dye"), NitorDyeRecipe.Serializer)
        Registry.register(Registries.RECIPE_SERIALIZER, id("unique_vis_crystal_recipe"), UniqueVisCrystalRecipe.Serializer)
        //Registry.register(Registries.RECIPE_TYPE, id("structure_transformation"), StructureTransformationRecipe.Type)
        AutoConfig.register(LostArcanaConfig::class.java, PartitioningSerializer.wrap(::JanksonConfigSerializer))
        Registry.register(Registries.ATTRIBUTE, id("aura_vision"), AURA_VISION)

        Registry.register(Registries.FEATURE, VIS_CRYSTAL_FEATURE_ID, VIS_CRYSTAL_FEATURE)
        DynamicRegistrySetupCallback.EVENT.register{

            it.getOptional(RegistryKeys.CONFIGURED_FEATURE).getOrNull()?.also { Registry.register(it, VIS_CRYSTAL_FEATURE_ID, CONFIGURED_VIS_CRYSTAL_FEATURE); println("Registered Configured Feature") }
            it.getOptional(RegistryKeys.PLACED_FEATURE).getOrNull()?.also { Registry.register(it, VIS_CRYSTAL_FEATURE_ID, PLACED_VIS_CRYSTAL_FEATURE); println("Registered Placed Feature") }
        }
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, VIS_CRYSTAL_FEATURE_ID))
        GeckoLib.initialize()
        //Registry.register(RegistryKeys.CONFIGURED_FEATURE, VIS_CRYSTAL_FEATURE_ID, CONFIGURED_VIS_CRYSTAL_FEATURE)
    }

    fun id(id: String) = Identifier(if(id.contains(":")) id else "$MODID:$id")
}