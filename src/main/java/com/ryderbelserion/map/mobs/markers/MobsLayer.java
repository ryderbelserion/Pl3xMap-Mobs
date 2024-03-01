/*
 * MIT License
 *
 * Copyright (c) 2020-2023 William Blake Galbreath
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ryderbelserion.map.mobs.markers;

import com.ryderbelserion.map.mobs.Pl3xMapMobs;
import com.ryderbelserion.map.mobs.configuration.WorldConfig;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Tooltip;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.HashSet;

public class MobsLayer extends WorldLayer {

    public static final Pl3xMapMobs plugin = JavaPlugin.getPlugin(Pl3xMapMobs.class);

    public static final String KEY = "pl3xmap_mobs";

    private final Collection<Marker<?>> activeMarkers = new HashSet<>();

    private final WorldConfig config;

    public MobsLayer(@NotNull WorldConfig config) {
        super(KEY, config.getWorld(), () -> config.LAYER_LABEL);

        this.config = config;

        setShowControls(config.LAYER_SHOW_CONTROLS);
        setDefaultHidden(config.LAYER_DEFAULT_HIDDEN);
        setUpdateInterval(config.LAYER_UPDATE_INTERVAL);
        setPriority(config.LAYER_PRIORITY);
        setZIndex(config.LAYER_ZINDEX);
    }

    private @NotNull String mob(@NotNull Mob mob) {
        String name = mob.getCustomName();

        return name == null ? mob.getName() : name;
    }

    private @NotNull Point point(@NotNull Location loc) {
        return Point.of(loc.getBlockX(), loc.getBlockZ());
    }

    @Override
    public @NotNull Collection<Marker<?>> getMarkers() {
        retrieveMarkers();

        return this.activeMarkers;
    }

    private void retrieveMarkers() {
        World bukkitWorld = Bukkit.getWorld(this.config.getWorld().getName());

        // If world is null, do fuck all.
        if (bukkitWorld == null) {
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, () -> bukkitWorld.getEntitiesByClass(Mob.class).forEach(mob -> {
            if (config.ONLY_SHOW_MOBS_EXPOSED_TO_SKY && bukkitWorld.getHighestBlockYAt(mob.getLocation()) > mob.getLocation().getY()) {
                return;
            }

            String key = String.format("%s_%s_%s", KEY, getWorld().getName(), mob.getUniqueId());

            net.pl3x.map.core.markers.marker.@NotNull Icon icon = Marker.icon(key, point(mob.getLocation()), Icon.get(mob).getKey(), this.config.ICON_SIZE)
                    .setOptions(Options.builder().tooltipDirection(Tooltip.Direction.TOP).tooltipContent(config.ICON_TOOLTIP_CONTENT.replace("<mob-id>", mob(mob))).build());

            this.activeMarkers.removeIf(value -> value.getKey().equals(icon.getKey()));

            this.activeMarkers.add(icon);
        }));
    }
}