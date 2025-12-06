package nl.ggentertainment.chunkweaver.common;

import com.google.common.hash.HashingOutputStream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.util.PredicateUtil;

import java.util.function.Supplier;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;
import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkweaverSoundEvents {

    public static final DeferredRegister<SoundEvent> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MOD_ID);

    public static final Supplier<SoundEvent> RIFT_AMBIENCE = DEFERRED_REGISTER.register("rift_ambience", () -> SoundEvent.createFixedRangeEvent(fromNamespaceAndPath(MOD_ID, "rift_ambience"), 8));
}
