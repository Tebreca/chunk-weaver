package nl.ggentertainment.chunkweaver.common.util;


import java.util.Map.Entry;
import java.util.function.Function;

public interface EntryFunction<A, B, T> extends Function<Entry<A, B>, T> {
    @Override
    default T apply(Entry<A, B> abEntry) {
        return apply(abEntry.getKey(), abEntry.getValue());
    }

    T apply(A a, B b);

}
