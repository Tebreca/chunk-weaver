package nl.ggentertainment.chunkweaver.common;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.economy.ItemValue;
import nl.ggentertainment.chunkweaver.common.core.economy.impl.LiteralItemValue;
import nl.ggentertainment.chunkweaver.common.core.economy.impl.TagItemValue;
import nl.ggentertainment.chunkweaver.common.core.rift.RiftTier;
import nl.ggentertainment.chunkweaver.common.util.PredicateUtil;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.ITEM_VALUE_REGISTRY;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkWeaverItemValues {

    public static final DeferredRegister<ItemValue> DEFERRED_REGISTER = DeferredRegister.create(ITEM_VALUE_REGISTRY, MOD_ID);

    static {

        // Gems & Precious Materials (Higher tier, moderate to low availability)
        DEFERRED_REGISTER.register("amethyst", () -> new TagItemValue(Tags.Items.GEMS_AMETHYST, 1, 0.9f, RiftTier.fromTierUp(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("diamond", () -> new TagItemValue(Tags.Items.GEMS_DIAMOND, 12, 0.3f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("emerald", () -> new TagItemValue(Tags.Items.GEMS_EMERALD, 3, 0.1f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("quartz", () -> new TagItemValue(Tags.Items.GEMS_QUARTZ, 2, 0.6f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("prismarine", () -> new TagItemValue(Tags.Items.GEMS_PRISMARINE, 0.4F, 0.3f, RiftTier.fromTierUp(RiftTier.SMALL)));

        // Metal Ingots (Mid-tier materials)
        DEFERRED_REGISTER.register("iron_ingot", () -> new LiteralItemValue(Holder.direct(Items.IRON_INGOT), 3, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("gold_ingot", () -> new LiteralItemValue(Holder.direct(Items.GOLD_INGOT), 6, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("copper_ingot", () -> new LiteralItemValue(Holder.direct(Items.COPPER_INGOT), 2, 0.4f, RiftTier.fromTierUp(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("netherite_ingot", () -> new LiteralItemValue(Holder.direct(Items.NETHERITE_INGOT), 50, 0.05f, RiftTier.forTier(RiftTier.STABLE)));
        DEFERRED_REGISTER.register("netherite_scrap", () -> new LiteralItemValue(Holder.direct(Items.NETHERITE_SCRAP), 12, 0.1f, RiftTier.fromTierUp(RiftTier.LARGE)));

        // Metal Blocks (Higher value than ingots)
        DEFERRED_REGISTER.register("iron_block", () -> new LiteralItemValue(Holder.direct(Items.IRON_BLOCK), 27, 0.3f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("gold_block", () -> new LiteralItemValue(Holder.direct(Items.GOLD_BLOCK), 54, 0.3f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("copper_block", () -> new LiteralItemValue(Holder.direct(Items.COPPER_BLOCK), 18, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("diamond_block", () -> new LiteralItemValue(Holder.direct(Items.DIAMOND_BLOCK), 108, 0.2f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("emerald_block", () -> new LiteralItemValue(Holder.direct(Items.EMERALD_BLOCK), 18, 0.05f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("netherite_block", () -> new LiteralItemValue(Holder.direct(Items.NETHERITE_BLOCK), 450, 0.01f, RiftTier.forTier(RiftTier.STABLE)));

        // Raw Ores
        DEFERRED_REGISTER.register("raw_ores", () -> new TagItemValue(Tags.Items.RAW_MATERIALS, 1, 0.3f, RiftTier.fromTierUp(RiftTier.SMALL)));

        // Dyes & Minerals
        DEFERRED_REGISTER.register("lapis_lazuli", () -> new LiteralItemValue(Holder.direct(Items.LAPIS_LAZULI), 1.5f, 0.5f, RiftTier.fromTierUp(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("redstone", () -> new TagItemValue(Tags.Items.DUSTS_REDSTONE, 0.8f, 0.7f, RiftTier.fromTierUp(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("glowstone_dust", () -> new TagItemValue(Tags.Items.DUSTS_GLOWSTONE, 1.2f, 0.5f, RiftTier.fromTierUp(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("dyes", () -> new TagItemValue(Tags.Items.DYES, 0.5f, 0.7f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));

        // Coal & Fuel
        DEFERRED_REGISTER.register("coal", () -> new LiteralItemValue(Holder.direct(Items.COAL), 0.8f, 0.8f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("charcoal", () -> new LiteralItemValue(Holder.direct(Items.CHARCOAL), 0.6f, 0.85f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("coal_block", () -> new LiteralItemValue(Holder.direct(Items.COAL_BLOCK), 7, 0.6f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));

        // Crops & Food (Low value, high availability)
        DEFERRED_REGISTER.register("mushrooms", () -> new TagItemValue(Tags.Items.MUSHROOMS, 2f, 0f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("wheat", () -> new LiteralItemValue(Holder.direct(Items.WHEAT), 0.3f, 0.3f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("wheat_seeds", () -> new LiteralItemValue(Holder.direct(Items.WHEAT_SEEDS), 0.1f, 0.95f, RiftTier.fromTierDown(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("potato", () -> new LiteralItemValue(Holder.direct(Items.POTATO), 0.2f, 0.9f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("carrot", () -> new LiteralItemValue(Holder.direct(Items.CARROT), 0.2f, 0.9f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("beetroot", () -> new LiteralItemValue(Holder.direct(Items.BEETROOT), 0.2f, 0.8f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("beetroot_seeds", () -> new LiteralItemValue(Holder.direct(Items.BEETROOT_SEEDS), 0.1f, 0.9f, RiftTier.fromTierDown(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("sweet_berries", () -> new LiteralItemValue(Holder.direct(Items.SWEET_BERRIES), 0.3f, 0.8f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("glow_berries", () -> new LiteralItemValue(Holder.direct(Items.GLOW_BERRIES), 0.4f, 0.7f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("melon_slice", () -> new LiteralItemValue(Holder.direct(Items.MELON_SLICE), 0.1f, 0.9f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("melon", () -> new LiteralItemValue(Holder.direct(Items.MELON), 0.9f, 0.85f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("pumpkin", () -> new LiteralItemValue(Holder.direct(Items.PUMPKIN), 0.5f, 0.8f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("apple", () -> new LiteralItemValue(Holder.direct(Items.APPLE), 0.4f, 0.8f, RiftTier.fromTierUp(RiftTier.TINY)));
        DEFERRED_REGISTER.register("golden_apple", () -> new LiteralItemValue(Holder.direct(Items.GOLDEN_APPLE), 25, 0.2f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("enchanted_golden_apple", () -> new LiteralItemValue(Holder.direct(Items.ENCHANTED_GOLDEN_APPLE), 100, 0.01f, RiftTier.forTier(RiftTier.STABLE)));

        // Nether Materials
        DEFERRED_REGISTER.register("nether_wart", () -> new LiteralItemValue(Holder.direct(Items.NETHER_WART), 1, 0.6f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("blaze_rod", () -> new LiteralItemValue(Holder.direct(Items.BLAZE_ROD), 8, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("blaze_powder", () -> new LiteralItemValue(Holder.direct(Items.BLAZE_POWDER), 4, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("ender_pearl", () -> new LiteralItemValue(Holder.direct(Items.ENDER_PEARL), 6, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("ender_eye", () -> new LiteralItemValue(Holder.direct(Items.ENDER_EYE), 10, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("ghast_tear", () -> new LiteralItemValue(Holder.direct(Items.GHAST_TEAR), 10, 0.2f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("magma_cream", () -> new LiteralItemValue(Holder.direct(Items.MAGMA_CREAM), 3, 0.5f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("ancient_debris", () -> new LiteralItemValue(Holder.direct(Items.ANCIENT_DEBRIS), 35, 0.02f, RiftTier.forTier(RiftTier.STABLE)));

        // Rare Drops & Special Items
        DEFERRED_REGISTER.register("nether_star", () -> new LiteralItemValue(Holder.direct(Items.NETHER_STAR), 100, 0.01f, RiftTier.forTier(RiftTier.STABLE)));
        DEFERRED_REGISTER.register("dragon_egg", () -> new LiteralItemValue(Holder.direct(Items.DRAGON_EGG), 500, 0.001f, RiftTier.forTier(RiftTier.STABLE)));
        DEFERRED_REGISTER.register("elytra", () -> new LiteralItemValue(Holder.direct(Items.ELYTRA), 75, 0.05f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("totem_of_undying", () -> new LiteralItemValue(Holder.direct(Items.TOTEM_OF_UNDYING), 40, 0.1f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("heart_of_the_sea", () -> new LiteralItemValue(Holder.direct(Items.HEART_OF_THE_SEA), 30, 0.1f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("dragon_breath", () -> new LiteralItemValue(Holder.direct(Items.DRAGON_BREATH), 15, 0.2f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("shulker_shell", () -> new LiteralItemValue(Holder.direct(Items.SHULKER_SHELL), 20, 0.15f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("trident", () -> new LiteralItemValue(Holder.direct(Items.TRIDENT), 50, 0.05f, RiftTier.fromTierUp(RiftTier.LARGE)));

        // Wood & Plant Materials (Very low value)
        DEFERRED_REGISTER.register("oak_log", () -> new LiteralItemValue(Holder.direct(Items.OAK_LOG), 0.3f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("spruce_log", () -> new LiteralItemValue(Holder.direct(Items.SPRUCE_LOG), 0.3f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("birch_log", () -> new LiteralItemValue(Holder.direct(Items.BIRCH_LOG), 0.3f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("jungle_log", () -> new LiteralItemValue(Holder.direct(Items.JUNGLE_LOG), 0.3f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("acacia_log", () -> new LiteralItemValue(Holder.direct(Items.ACACIA_LOG), 0.3f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("dark_oak_log", () -> new LiteralItemValue(Holder.direct(Items.DARK_OAK_LOG), 0.3f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("mangrove_log", () -> new LiteralItemValue(Holder.direct(Items.MANGROVE_LOG), 0.3f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("cherry_log", () -> new LiteralItemValue(Holder.direct(Items.CHERRY_LOG), 0.3f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("crimson_stem", () -> new LiteralItemValue(Holder.direct(Items.CRIMSON_STEM), 0.4f, 0.85f, RiftTier.fromTierUp(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("warped_stem", () -> new LiteralItemValue(Holder.direct(Items.WARPED_STEM), 0.4f, 0.85f, RiftTier.fromTierUp(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("oak_planks", () -> new LiteralItemValue(Holder.direct(Items.OAK_PLANKS), 0.08f, 0.95f, RiftTier.fromTierDown(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("stick", () -> new LiteralItemValue(Holder.direct(Items.STICK), 0.02f, 0.98f, RiftTier.fromTierDown(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("bamboo", () -> new LiteralItemValue(Holder.direct(Items.BAMBOO), 0.1f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("sugar_cane", () -> new LiteralItemValue(Holder.direct(Items.SUGAR_CANE), 0.2f, 0.9f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("cactus", () -> new LiteralItemValue(Holder.direct(Items.CACTUS), 0.3f, 0.8f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("kelp", () -> new LiteralItemValue(Holder.direct(Items.KELP), 0.1f, 0.9f, RiftTier.fromTierDown(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("vine", () -> new LiteralItemValue(Holder.direct(Items.VINE), 0.1f, 0.9f, RiftTier.fromTierDown(RiftTier.SMALL)));

        // Stone & Building Materials (Very low value)
        DEFERRED_REGISTER.register("cobblestone", () -> new LiteralItemValue(Holder.direct(Items.COBBLESTONE), 0.001f, 0.98f, RiftTier.fromTierDown(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("stone", () -> new TagItemValue(Tags.Items.STONES, 0.05f, 0.97f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("granite", () -> new LiteralItemValue(Holder.direct(Items.GRANITE), 0.05f, 0.95f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("diorite", () -> new LiteralItemValue(Holder.direct(Items.DIORITE), 0.05f, 0.95f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("andesite", () -> new LiteralItemValue(Holder.direct(Items.ANDESITE), 0.05f, 0.95f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("deepslate", () -> new LiteralItemValue(Holder.direct(Items.DEEPSLATE), 0.08f, 0.92f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("cobbled_deepslate", () -> new LiteralItemValue(Holder.direct(Items.COBBLED_DEEPSLATE), 0.06f, 0.95f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("netherrack", () -> new LiteralItemValue(Holder.direct(Items.NETHERRACK), 0.04f, 0.96f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("end_stone", () -> new LiteralItemValue(Holder.direct(Items.END_STONE), 0.3f, 0.8f, RiftTier.fromTierUp(RiftTier.LARGE)));

        // Sand & Soil
        DEFERRED_REGISTER.register("sand", () -> new LiteralItemValue(Holder.direct(Items.SAND), 0.08f, 0.96f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("red_sand", () -> new LiteralItemValue(Holder.direct(Items.RED_SAND), 0.08f, 0.95f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("gravel", () -> new LiteralItemValue(Holder.direct(Items.GRAVEL), 0.1f, 0.95f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("dirt", () -> new LiteralItemValue(Holder.direct(Items.DIRT), 0.02f, 0.98f, RiftTier.fromTierDown(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("coarse_dirt", () -> new LiteralItemValue(Holder.direct(Items.COARSE_DIRT), 0.02f, 0.98f, RiftTier.fromTierDown(RiftTier.SMALL)));
        DEFERRED_REGISTER.register("clay_ball", () -> new LiteralItemValue(Holder.direct(Items.CLAY_BALL), 0.2f, 0.85f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("clay", () -> new LiteralItemValue(Holder.direct(Items.CLAY), 0.8f, 0.8f, RiftTier.fromTierDown(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("terracotta", () -> new LiteralItemValue(Holder.direct(Items.TERRACOTTA), 0.3f, 0.85f, RiftTier.fromTierDown(RiftTier.MEDIUM)));

        // Mob Drops (Varied value)
        DEFERRED_REGISTER.register("bone", () -> new LiteralItemValue(Holder.direct(Items.BONE), 0.4f, 0.8f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("bone_meal", () -> new LiteralItemValue(Holder.direct(Items.BONE_MEAL), 0.1f, 0.9f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("string", () -> new TagItemValue(Tags.Items.STRINGS, 0.3f, 0.8f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("slime_ball", () -> new TagItemValue(Tags.Items.SLIME_BALLS, 2, 0.6f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("spider_eye", () -> new LiteralItemValue(Holder.direct(Items.SPIDER_EYE), 0.5f, 0.7f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("fermented_spider_eye", () -> new LiteralItemValue(Holder.direct(Items.FERMENTED_SPIDER_EYE), 1.5f, 0.5f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("gunpowder", () -> new LiteralItemValue(Holder.direct(Items.GUNPOWDER), 1.5f, 0.6f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("phantom_membrane", () -> new LiteralItemValue(Holder.direct(Items.PHANTOM_MEMBRANE), 5, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("rabbit_hide", () -> new LiteralItemValue(Holder.direct(Items.RABBIT_HIDE), 0.3f, 0.7f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("rabbit_foot", () -> new LiteralItemValue(Holder.direct(Items.RABBIT_FOOT), 3, 0.5f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("leather", () -> new LiteralItemValue(Holder.direct(Items.LEATHER), 1, 0.7f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("feather", () -> new TagItemValue(Tags.Items.FEATHERS, 0.3f, 0.8f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("rotten_flesh", () -> new LiteralItemValue(Holder.direct(Items.ROTTEN_FLESH), 0.1f, 0.9f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("zombie_head", () -> new LiteralItemValue(Holder.direct(Items.ZOMBIE_HEAD), 10, 0.1f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("skeleton_skull", () -> new LiteralItemValue(Holder.direct(Items.SKELETON_SKULL), 10, 0.1f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("creeper_head", () -> new LiteralItemValue(Holder.direct(Items.CREEPER_HEAD), 10, 0.1f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("wither_skeleton_skull", () -> new LiteralItemValue(Holder.direct(Items.WITHER_SKELETON_SKULL), 25, 0.05f, RiftTier.fromTierUp(RiftTier.LARGE)));

        // Ocean Materials
        DEFERRED_REGISTER.register("prismarine_shard", () -> new LiteralItemValue(Holder.direct(Items.PRISMARINE_SHARD), 2, 0.5f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("prismarine_crystals", () -> new LiteralItemValue(Holder.direct(Items.PRISMARINE_CRYSTALS), 3, 0.4f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("nautilus_shell", () -> new LiteralItemValue(Holder.direct(Items.NAUTILUS_SHELL), 12, 0.2f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("turtle_scute", () -> new LiteralItemValue(Holder.direct(Items.TURTLE_SCUTE), 4, 0.4f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("ink_sac", () -> new LiteralItemValue(Holder.direct(Items.INK_SAC), 0.4f, 0.7f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("glow_ink_sac", () -> new LiteralItemValue(Holder.direct(Items.GLOW_INK_SAC), 1.5f, 0.5f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("sponge", () -> new LiteralItemValue(Holder.direct(Items.SPONGE), 5, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("wet_sponge", () -> new LiteralItemValue(Holder.direct(Items.WET_SPONGE), 5, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));

        // Brewing & Potions
        DEFERRED_REGISTER.register("brewing_stand", () -> new LiteralItemValue(Holder.direct(Items.BREWING_STAND), 5, 0.5f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("cauldron", () -> new LiteralItemValue(Holder.direct(Items.CAULDRON), 21, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("glass_bottle", () -> new LiteralItemValue(Holder.direct(Items.GLASS_BOTTLE), 0.5f, 0.8f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("experience_bottle", () -> new LiteralItemValue(Holder.direct(Items.EXPERIENCE_BOTTLE), 8, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));

        // Miscellaneous Valuable Items
        DEFERRED_REGISTER.register("honeycomb", () -> new LiteralItemValue(Holder.direct(Items.HONEYCOMB), 1.5f, 0.6f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("honey_bottle", () -> new LiteralItemValue(Holder.direct(Items.HONEY_BOTTLE), 2, 0.6f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("honey_block", () -> new LiteralItemValue(Holder.direct(Items.HONEY_BLOCK), 8, 0.5f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("echo_shard", () -> new LiteralItemValue(Holder.direct(Items.ECHO_SHARD), 20, 0.15f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("disc_fragment", () -> new LiteralItemValue(Holder.direct(Items.DISC_FRAGMENT_5), 8, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("book", () -> new LiteralItemValue(Holder.direct(Items.BOOK), 1.5f, 0.7f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("enchanted_book", () -> new LiteralItemValue(Holder.direct(Items.ENCHANTED_BOOK), 15, 0.2f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("name_tag", () -> new LiteralItemValue(Holder.direct(Items.NAME_TAG), 10, 0.2f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("saddle", () -> new LiteralItemValue(Holder.direct(Items.SADDLE), 12, 0.2f, RiftTier.fromTierUp(RiftTier.MEDIUM)));

        // Specialty Items
        DEFERRED_REGISTER.register("bell", () -> new LiteralItemValue(Holder.direct(Items.BELL), 18, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("beacon", () -> new LiteralItemValue(Holder.direct(Items.BEACON), 200, 0.01f, RiftTier.forTier(RiftTier.STABLE)));
        DEFERRED_REGISTER.register("conduit", () -> new LiteralItemValue(Holder.direct(Items.CONDUIT), 90, 0.05f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("recovery_compass", () -> new LiteralItemValue(Holder.direct(Items.RECOVERY_COMPASS), 80, 0.05f, RiftTier.fromTierUp(RiftTier.LARGE)));
        DEFERRED_REGISTER.register("spyglass", () -> new LiteralItemValue(Holder.direct(Items.SPYGLASS), 8, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("goat_horn", () -> new LiteralItemValue(Holder.direct(Items.GOAT_HORN), 5, 0.3f, RiftTier.fromTierUp(RiftTier.MEDIUM)));

        // Utility Items
        DEFERRED_REGISTER.register("flint", () -> new LiteralItemValue(Holder.direct(Items.FLINT), 0.3f, 0.8f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("flint_and_steel", () -> new LiteralItemValue(Holder.direct(Items.FLINT_AND_STEEL), 3.5f, 0.5f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("compass", () -> new LiteralItemValue(Holder.direct(Items.COMPASS), 16, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("clock", () -> new LiteralItemValue(Holder.direct(Items.CLOCK), 24, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("bucket", () -> new LiteralItemValue(Holder.direct(Items.BUCKET), 9, 0.5f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("water_bucket", () -> new LiteralItemValue(Holder.direct(Items.WATER_BUCKET), 9, 0.5f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("lava_bucket", () -> new LiteralItemValue(Holder.direct(Items.LAVA_BUCKET), 12, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("powder_snow_bucket", () -> new LiteralItemValue(Holder.direct(Items.POWDER_SNOW_BUCKET), 9.5f, 0.45f, RiftTier.fromTierUp(RiftTier.MEDIUM)));

        // Glass & Decorative
        DEFERRED_REGISTER.register("glass", () -> new TagItemValue(Tags.Items.GLASS_BLOCKS, 0.3f, 0.9f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("glass_pane", () -> new LiteralItemValue(Holder.direct(Items.GLASS_PANE), 0.12f, 0.92f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("sea_lantern", () -> new LiteralItemValue(Holder.direct(Items.SEA_LANTERN), 12, 0.4f, RiftTier.fromTierUp(RiftTier.MEDIUM)));
        DEFERRED_REGISTER.register("lantern", () -> new LiteralItemValue(Holder.direct(Items.LANTERN), 8.5f, 0.5f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("soul_lantern", () -> new LiteralItemValue(Holder.direct(Items.SOUL_LANTERN), 9, 0.45f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.MEDIUM), RiftTier.forTier(RiftTier.STABLE))));

        // Wool & Decorative Blocks
        DEFERRED_REGISTER.register("white_wool", () -> new LiteralItemValue(Holder.direct(Items.WHITE_WOOL), 0.6f, 0.8f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));
        DEFERRED_REGISTER.register("carpet", () -> new LiteralItemValue(Holder.direct(Items.WHITE_CARPET), 0.4f, 0.85f, PredicateUtil.or(RiftTier.fromTierDown(RiftTier.SMALL), RiftTier.forTier(RiftTier.STABLE))));

        // Music Discs (Collectibles)
        DEFERRED_REGISTER.register("music_disc_13", () -> new TagItemValue(Tags.Items.MUSIC_DISCS, 10, 0.15f, RiftTier.fromTierUp(RiftTier.LARGE)));
    }

}
