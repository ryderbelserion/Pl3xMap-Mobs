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
package com.ryderbelserion.map.mobs.listener;

import com.ryderbelserion.map.mobs.Pl3xMapMobs;
import com.ryderbelserion.map.mobs.markers.Icon;
import com.ryderbelserion.map.mobs.markers.MobsLayer;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.event.EventHandler;
import net.pl3x.map.core.event.EventListener;
import net.pl3x.map.core.event.server.Pl3xMapEnabledEvent;
import net.pl3x.map.core.event.server.ServerLoadedEvent;
import net.pl3x.map.core.event.world.WorldLoadedEvent;
import net.pl3x.map.core.event.world.WorldUnloadedEvent;
import net.pl3x.map.core.world.World;
import com.ryderbelserion.map.mobs.configuration.WorldConfig;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class WorldListener implements EventListener, Listener {

    @NotNull
    private final Pl3xMapMobs plugin = Pl3xMapMobs.get();

    public WorldListener() {
        Pl3xMap.api().getEventRegistry().register(this);
    }

    @EventHandler
    public void onPl3xMapEnabled(@NotNull Pl3xMapEnabledEvent event) {
        Icon.register();
    }

    @EventHandler
    public void onServerLoaded(@NotNull ServerLoadedEvent event) {
        Icon.register();
        Pl3xMap.api().getWorldRegistry().forEach(this::registerWorld);
    }

    @EventHandler
    public void onWorldLoaded(@NotNull WorldLoadedEvent event) {
        registerWorld(event.getWorld());
    }

    @EventHandler
    public void onWorldUnloaded(@NotNull WorldUnloadedEvent event) {
        try {
            // Clear when world is unloaded.
            this.plugin.getMobsManager().clearMarkers(event.getWorld().getName());

            // Unregister layer.
            event.getWorld().getLayerRegistry().unregister(MobsLayer.KEY);
        } catch (Throwable ignore) {}
    }

    private void registerWorld(@NotNull World world) {
        // Add new world.
        this.plugin.getMobsManager().addWorld(world.getName());

        // Add new layer.
        world.getLayerRegistry().register(new MobsLayer(new WorldConfig(world)));
    }
}