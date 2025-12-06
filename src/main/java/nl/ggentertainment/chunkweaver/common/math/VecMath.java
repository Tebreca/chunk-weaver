package nl.ggentertainment.chunkweaver.common.math;

import net.minecraft.world.phys.Vec2;

public class VecMath {

    public static Vec2 multiply(Vec2 a, Vec2 b) {
        return new Vec2(a.x * b.x, a.y * b.y);
    }
}
