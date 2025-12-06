package nl.ggentertainment.chunkweaver.common.core.classes.fighter;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class FighterAttributes {
    private static final DeferredRegister<Attribute> registry = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MOD_ID);

    public static void register(IEventBus bus) {
        registry.register(bus);
    }

    public static final Holder<Attribute> ABILITY_LENGTH = registerPercent("ability_length", 0, 5);
    public static final Holder<Attribute> ABILITY_POWER = registerPercent("ability_power", 0, 5);

    /* Brawler */
    public static final Holder<Attribute> BLOODLUST_RADIUS = register("bloodlust_radius", 3, 8);
    public static final Holder<Attribute> LIFE_STEAL = registerPercent("lifesteal", 0, 2.5);

    /* Ranger */
    public static final Holder<Attribute> ARROW_POWER_MODIFIER = registerPercent("arrow_power_modifier", 1, 5);
    public static final Holder<Attribute> ACCURACY = registerPercent("bow_accuracy", 0, 1);

    /* Tank */
    public static final Holder<Attribute> NEGATE_CHANCE = registerPercent("damage_negation_chance", 0, 1);
    public static final Holder<Attribute> NEGATE_AMOUNT = registerPercent("damage_negation_amount", 0, 0.9);

    /*
    ====================================================================================================================
    Helper methods
    ====================================================================================================================
     */
    public static Holder<Attribute> register(String name, double min, double max) {
        return registry.register("class.fighter." + name, () -> new RangedAttribute("attribute.name.fighter." + name, Math.max(0, min), min, max).setSyncable(true));
    }

    public static Holder<Attribute> registerPercent(String name, double min, double max) {
        return registry.register("class.fighter." + name, () -> new PercentageAttribute("attribute.name.fighter." + name, Math.max(0, min), min, max).setSyncable(true));
    }

    public static Holder<Attribute> registerPercent(String name, double min, double max, double val) {
        return registry.register("class.fighter." + name, () -> new PercentageAttribute("attribute.name.fighter." + name, val, min, max).setSyncable(true));
    }


}
