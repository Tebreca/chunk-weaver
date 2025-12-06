package nl.ggentertainment.chunkweaver.common.util;

import com.mojang.serialization.Codec;

import java.util.UUID;

public class CustomCodecs {
    public static final Codec<UUID> UUID = Codec.stringResolver(java.util.UUID::toString, java.util.UUID::fromString);
}
