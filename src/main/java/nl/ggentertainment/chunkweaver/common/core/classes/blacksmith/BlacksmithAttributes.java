package nl.ggentertainment.chunkweaver.common.core.classes.blacksmith;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

//TODO not player attributes??
public class BlacksmithAttributes {


    private static final DeferredRegister<Attribute> registry = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MOD_ID);

    /* Masterworks */

    /* Chance to forge a masterwork */
    public static final Holder<Attribute> FORGE_CHANCE = registerPercent("forge_chance", 0, 1);
    /* Max amount of apotheosis gem sockets to add when forging a masterwork*/
    public static final Holder<Attribute> MAX_SLOT_COUNT = register("max_slot_count", 0, 8);
    /* Min amount of (...) */
    public static final Holder<Attribute> MIN_SLOT_COUNT = register("min_slot_count", 0, 4);
    /* Affix power modifier*/
    public static final Holder<Attribute> FORGE_MODIFIER = register("forge_modifier", 0, 1);
    /* Affix rolls*/
    public static final Holder<Attribute> FORGE_ROLLS = register("forge_rolls", 0, 8);

    /* Anvil usage */

    /* % of the normal repair material cost; calculated as cost_new = ceil(mod * cost_old) */
    public static final Holder<Attribute> REPAIR_COST_MODFIER = registerPercent("repair_cost_modifier", 0.1, 1, 1);

    /* % of the normal xp cost, can go to 0 */
    public static final Holder<Attribute> XP_COST_MODFIER = registerPercent("xp_cost_modifier", 0, 1, 1);

    public static void register(IEventBus bus) {
        registry.register(bus);
    }

    /*
    ====================================================================================================================
    Helper methods
    ====================================================================================================================
     */
    public static Holder<Attribute> register(String name, double min, double max) {
        return registry.register("class.blacksmith." + name, () -> new RangedAttribute("attribute.name.blacksmith." + name, Math.max(0, min), min, max).setSyncable(true));
    }

    public static Holder<Attribute> registerPercent(String name, double min, double max) {
        return registry.register("class.blacksmith." + name, () -> new PercentageAttribute("attribute.name.blacksmith." + name, Math.max(0, min), min, max));
    }


    public static Holder<Attribute> registerPercent(String name, double min, double max, double val) {
        return registry.register("class.blacksmith." + name, () -> new PercentageAttribute("attribute.name.blacksmith." + name, val, min, max));
    }

}
