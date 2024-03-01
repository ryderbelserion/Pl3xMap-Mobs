package com.ryderbelserion.map.mobs.listener;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.ryderbelserion.map.mobs.Pl3xMapMobs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class EntityListener implements Listener {

    @NotNull
    private final Pl3xMapMobs plugin = Pl3xMapMobs.get();

    @EventHandler
    public void onEntityRemove(EntityRemoveFromWorldEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Mob mob) {
            this.plugin.getMobsManager().removeMarker(mob);
        }
    }
}