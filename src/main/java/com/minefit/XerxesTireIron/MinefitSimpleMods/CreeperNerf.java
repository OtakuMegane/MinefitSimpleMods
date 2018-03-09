package com.minefit.XerxesTireIron.MinefitSimpleMods;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CreeperNerf implements Listener {
    private final MinefitSimpleMods plugin;

    public CreeperNerf(MinefitSimpleMods instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onEntityExplode(EntityExplodeEvent event) {
        String creepConfig = "CreeperNerf.worlds." + event.getLocation().getWorld().getName() + ".";

        if (event.isCancelled() || event.getEntity() == null
                || !this.plugin.main_config.getBoolean(creepConfig + "enabled", false)) {
            return;
        }

        Entity entity = event.getEntity();

        if (entity.getType() != EntityType.CREEPER) {
            return;
        }

        Location creeperLoc = entity.getLocation();

        if (creeperLoc.getY() > this.plugin.main_config.getInt(creepConfig + "block-damage-ceiling", 0)) {
            event.setYield(0.0F);
            event.setCancelled(true);
            event.getEntity().getWorld().createExplosion(creeperLoc, 0.0F, false);
        }
    }
}
