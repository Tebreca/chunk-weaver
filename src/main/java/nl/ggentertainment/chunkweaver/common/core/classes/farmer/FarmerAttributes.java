package nl.ggentertainment.chunkweaver.common.core.classes.farmer;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class FarmerAttributes {

    private static final DeferredRegister<Attribute> registry = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MOD_ID);

    public static void register(IEventBus bus) {
        registry.register(bus);
    }

    public static final Holder<Attribute> HARVEST_DROP_CHANCE = registerPercent("harvest_drop_chance", 1, 16); //15
    public static final Holder<Attribute> HARVEST_MUTATION_CHANCE = register("harvest_mutation_chance", 0, 0.6f); // 5
    public static final Holder<Attribute> HARVEST_MUTATION_QUALITY = register("harvest_mutation_quality", 0, 5f); // 4
    public static final Holder<Attribute> HARVEST_REGROW_CHANCE = registerPercent("harvest_regrow_chance", 0, 0.9f); // 8

    /*
    ====================================================================================================================
    Helper methods
    ====================================================================================================================
     */
    public static Holder<Attribute> register(String name, double min, double max) {
        return registry.register("class.farmer." + name, () -> new RangedAttribute("attribute.name.farmer." + name, Math.max(0, min), min, max).setSyncable(true));
    }

    public static Holder<Attribute> registerPercent(String name, double min, double max) {
        return registry.register("class.farmer." + name, () -> new PercentageAttribute("attribute.name.farmer." + name, Math.max(0, min), min, max).setSyncable(true));
    }


    public static Holder<Attribute> registerPercent(String name, double min, double max, double val) {
        return registry.register("class.farmer." + name, () -> new PercentageAttribute("attribute.name.farmer." + name, val, min, max).setSyncable(true));
    }


}
