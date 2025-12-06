package nl.ggentertainment.chunkweaver.common.core.classes.miner;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class MinerAttributes {

    private static final DeferredRegister<Attribute> registry = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MOD_ID);
    public static void register(IEventBus bus){
        registry.register(bus);
    }


    public static final Holder<Attribute> DROP_MODIFIER = register("drop_modifier", 1, 5);
    public static final Holder<Attribute> SUPERMINE_RANGE = register("supermine_range", 5, 25);
    public static final Holder<Attribute> DURABILITY_NEGATION = registerPercent("durability_negation", 0, 0.5);

    /* This class will also get underwater mining specialisation */
    public static final Holder<Attribute> ORE_BREATHING = register("ore_breathing", 0, 25);


    /*
    ====================================================================================================================
    Helper methods
    ====================================================================================================================
     */
    public static Holder<Attribute> register(String name, double min, double max) {
        return registry.register("class.miner." + name, () -> new RangedAttribute("attribute.name.miner." + name, Math.max(0, min), min, max).setSyncable(true));
    }

    public static Holder<Attribute> registerPercent(String name, double min, double max) {
        return registry.register("class.miner." + name, () -> new PercentageAttribute("attribute.name.miner." + name, Math.max(0, min), min, max).setSyncable(true));
    }


    public static Holder<Attribute> registerPercent(String name, double min, double max, double val) {
        return registry.register("class.miner." + name, () -> new PercentageAttribute("attribute.name.miner." + name, val, min, max).setSyncable(true));
    }
}
