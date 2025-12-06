package nl.ggentertainment.chunkweaver.common.core.classes.wizard;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class WizardAttributes {
    private static final DeferredRegister<Attribute> registry = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MOD_ID);

    public static void register(IEventBus bus) {
        registry.register(bus);
    }


    public static final Holder<Attribute> MAGE_LEVEL = register("level", 0, 5);


    /*
   ====================================================================================================================
   Helper methods
   ====================================================================================================================
    */
    public static Holder<Attribute> register(String name, double min, double max) {
        return registry.register("class.wizard." + name, () -> new RangedAttribute("attribute.name.wizard." + name, Math.max(0, min), min, max).setSyncable(true));
    }

    public static Holder<Attribute> registerPercent(String name, double min, double max) {
        return registry.register("class.wizard." + name, () -> new PercentageAttribute("attribute.name.wizard." + name, Math.max(0, min), min, max).setSyncable(true));
    }


    public static Holder<Attribute> registerPercent(String name, double min, double max, double val) {
        return registry.register("class.wizard." + name, () -> new PercentageAttribute("attribute.name.wizard." + name, val, min, max).setSyncable(true));
    }
}
