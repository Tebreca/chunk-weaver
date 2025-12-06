package nl.ggentertainment.chunkweaver.common.util;

import java.util.function.Predicate;

public class PredicateUtil {

    public static <T> Predicate<T> or(Predicate<T> p1, Predicate<T> p2) {
        return t -> p1.test(t) || p2.test(t);
    }
}
