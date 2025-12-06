package nl.ggentertainment.chunkweaver.common.core.classes.explorer;


import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ExplorerAttributes {

    public static final DeferredRegister<Attribute> registry = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MOD_ID);

    public static final Holder<Attribute> GHOST_DURATION = register("ghost_duration", 1, 5);
    public static final Holder<Attribute> LOOT_MODIFIER = registerPercent("loot_modifier", 1, 5);

    public static void register(IEventBus bus) {
        registry.register(bus);
    }

    /*
    ====================================================================================================================
    Helper methods
    ====================================================================================================================
     */
    public static Holder<Attribute> register(String name, double min, double max) {
        return registry.register("class.explorer." + name, () -> new RangedAttribute("attribute.name.explorer." + name, Math.max(0, min), min, max).setSyncable(true));
    }

    public static Holder<Attribute> registerPercent(String name, double min, double max) {
        return registry.register("class.explorer." + name, () -> new PercentageAttribute("attribute.name.explorer." + name, Math.max(0, min), min, max));
    }


    public static Holder<Attribute> registerPercent(String name, double min, double max, double val) {
        return registry.register("class.explorer." + name, () -> new PercentageAttribute("attribute.name.explorer." + name, val, min, max));
    }

}
