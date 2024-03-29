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
package com.ryderbelserion.map.mobs;

import com.ryderbelserion.map.mobs.listener.EntityListener;
import com.ryderbelserion.map.mobs.markers.MobsManager;
import net.pl3x.map.core.Pl3xMap;
import com.ryderbelserion.map.mobs.listener.WorldListener;
import com.ryderbelserion.map.mobs.markers.MobsLayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Pl3xMapMobs extends JavaPlugin {

    @NotNull
    public static Pl3xMapMobs get() {
        return JavaPlugin.getPlugin(Pl3xMapMobs.class);
    }

    private MobsManager mobsManager;

    @Override
    public void onEnable() {
        try {
            Class.forName("io.papermc.paper.configuration.PaperConfigurations");
        } catch (ClassNotFoundException exception) {
            getLogger().severe("This is not a paper server.");
            getServer().getPluginManager().disablePlugin(this);
        }

        if (!getServer().getPluginManager().isPluginEnabled("Pl3xMap")) {
            getLogger().severe("Pl3xMap not found!");

            getServer().getPluginManager().disablePlugin(this);

            return;
        }

        this.mobsManager = new MobsManager();

        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        getServer().getPluginManager().registerEvents(new EntityListener(), this);
    }

    @Override
    public void onDisable() {
        Pl3xMap.api().getWorldRegistry().forEach(world -> {
            try {
                world.getLayerRegistry().unregister(MobsLayer.KEY);
            } catch (Throwable ignore) {}
        });
    }

    @NotNull
    public MobsManager getMobsManager() {
        return this.mobsManager;
    }
}