package net.pl3x.map.mobs.util;

import java.util.function.Function;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Panda;

public class MobHelper {
    public static Panda.Gene getTrait(Panda panda) {
        Panda.Gene mainGene = panda.getMainGene();
        if (!mainGene.isRecessive()) {
            return mainGene;
        }
        return switch (mainGene) {
            case BROWN -> Panda.Gene.BROWN;
            case WEAK -> Panda.Gene.WEAK;
            default -> Panda.Gene.NORMAL;
        };
    }

    @FunctionalInterface
    public interface Predicate<T extends Mob> extends Function<T, Boolean> {
    }
}
