package com.ryderbelserion.map.mobs.markers;

import com.ryderbelserion.map.mobs.configuration.WorldConfig;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Tooltip;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MobsManager {

    private static final Map<String, Collection<Marker<?>>> activeMarkers = new HashMap<>();

    public static Collection<Marker<?>> getActiveMarkers(String worldName) {
        return Collections.unmodifiableCollection(activeMarkers.get(worldName));
    }

    public static void clearMarkers(String worldName) {
        if (activeMarkers.get(worldName) != null) {
            activeMarkers.get(worldName).clear();
        }

        activeMarkers.remove(worldName);
    }

    public static void addMarker(String key, Mob mob, WorldConfig config) {
        net.pl3x.map.core.markers.marker.Icon icon = getIcon(key, mob, config);

        // Remove it if it exists.
        removeMarker(mob.getUniqueId(), config);

        // then add it back.
        activeMarkers.get(config.getWorld().getName()).add(icon);
    }

    public static void removeMarker(UUID uuid, WorldConfig config) {
        Mob mob = (Mob) Bukkit.getEntity(uuid);

        if (mob != null) {
            String key = String.format("%s_%s_%s", "pl3xmap_mobs", mob.getWorld().getName(), mob.getUniqueId());

            activeMarkers.get(config.getWorld().getName()).removeIf(value -> value.getKey().equalsIgnoreCase(key));
        }
    }

    private static @NotNull String mob(@NotNull Mob mob) {
        String name = mob.getCustomName();

        return name == null ? mob.getName() : name;
    }

    private static @NotNull Point point(@NotNull Location loc) {
        return Point.of(loc.getBlockX(), loc.getBlockZ());
    }

    public static net.pl3x.map.core.markers.marker.Icon getIcon(String key, Mob mob, WorldConfig config) {
        return Marker.icon(key, point(mob.getLocation()), Icon.get(mob).getKey(), config.ICON_SIZE)
                .setOptions(Options.builder()
                        .tooltipDirection(Tooltip.Direction.TOP)
                        .tooltipContent(config.ICON_TOOLTIP_CONTENT.replace("<mob-id>", mob(mob)))
                        .build());
    }
}