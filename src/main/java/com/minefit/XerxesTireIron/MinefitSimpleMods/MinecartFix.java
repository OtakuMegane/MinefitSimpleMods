package com.minefit.XerxesTireIron.MinefitSimpleMods;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class MinecartFix implements Listener {
    private final MinefitSimpleMods plugin;

    public MinecartFix(MinefitSimpleMods instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onVehicleCreate(VehicleCreateEvent event) {
        Entity vehicle = event.getVehicle();
        EntityType entityType = event.getVehicle().getType();

        if (entityType == EntityType.MINECART) {
            ((Minecart) vehicle).setSlowWhenEmpty(false);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onVehicleEnter(VehicleEnterEvent event) {
        Entity vehicle = event.getVehicle();
        EntityType entityType = event.getVehicle().getType();

        if (entityType == EntityType.MINECART && ((Minecart) vehicle).isSlowWhenEmpty()) {
            ((Minecart) vehicle).setSlowWhenEmpty(false);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onChunkLoad(ChunkLoadEvent event) {
        Entity[] entities = event.getChunk().getEntities();
        int entityCount = entities.length;

        for (int i = 0; i < entityCount; i++) {
            if (entities[i].getType() == EntityType.MINECART && ((Minecart) entities[i]).isSlowWhenEmpty()) {
                ((Minecart) entities[i]).setSlowWhenEmpty(false);
            }
        }

    }
}
