package com.minefit.xerxestireiron.minefitsimplemods;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CreeperNerf implements Listener {
    private final MinefitSimpleMods plugin;
    private YamlConfiguration mainConfig = new YamlConfiguration();

    public CreeperNerf(MinefitSimpleMods instance) {
        plugin = instance;
        this.mainConfig = this.plugin.loadYaml("CreeperNerf.yml");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        Location entityLocation = entity.getLocation();
        World world = event.getLocation().getWorld();
        String worldName = world.getName();

        if (event.isCancelled() || entity == null || entity.getType() != EntityType.CREEPER
                || !this.mainConfig.getBoolean("worlds." + worldName + ".enabled", false)) {
            return;
        }

        if (entityLocation.getY() > this.mainConfig.getInt("worlds." + worldName + ".block-damage-ceiling", 0)) {
            event.setYield(0.0F);
            event.setCancelled(true);
            event.getEntity().getWorld().createExplosion(entityLocation, 0.0F, false);
        }
    }
}
