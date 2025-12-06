package nl.ggentertainment.chunkweaver.common.core.classes.explorer;

public interface GhostingEntity {

    long getLastGhost();

    void tryGhost();

    int ticksRemaining();

    default boolean isGhosted() {
        return ticksRemaining() > 0;
    }
}
