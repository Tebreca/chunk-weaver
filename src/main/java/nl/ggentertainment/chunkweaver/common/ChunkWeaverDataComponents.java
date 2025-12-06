package nl.ggentertainment.chunkweaver.common;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nl.ggentertainment.chunkweaver.common.core.classes.engineer.stamp.StampData;
import nl.ggentertainment.chunkweaver.common.core.rift.infusion.PylonBlock;

import java.util.UUID;

import static nl.ggentertainment.chunkweaver.ChunkWeaver.MOD_ID;

public class ChunkWeaverDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WALLET_CONTENTS = DEFERRED_REGISTER.register("wallet_contents",//
            () -> DataComponentType.<Integer>builder()//
                    .persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PylonBlock.PylonType>> PYLON_TYPE =
            DEFERRED_REGISTER.register("pylon_type", () -> DataComponentType.<PylonBlock.PylonType>builder()//;
                    .persistent(PylonBlock.PylonType.CODEC).networkSynchronized(PylonBlock.PylonType.STREAM_CODEC)
                    .build()
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<StampData>> STAMP = DEFERRED_REGISTER.register("stamp",//
            () -> DataComponentType.<StampData>builder().persistent(StampData.CODEC).networkSynchronized(StampData.STREAM_CODEC)//
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> KEY_ID = DEFERRED_REGISTER.register("key", //
            () -> DataComponentType.<UUID>builder().persistent(Codec.stringResolver(UUID::toString, UUID::fromString))//
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString)).build());


    public static void addTooltips(ItemTooltipEvent event) {
        event.getItemStack().addToTooltip(STAMP, event.getContext(), event.getToolTip()::add, event.getFlags());
        if (event.getItemStack().has(KEY_ID)) {// no fancy formatting
            event.getToolTip().add(Component.literal(event.getItemStack().get(KEY_ID).toString()).withStyle(ChatFormatting.GRAY));
        }
    }
}
