package com.minefit.xerxestireiron.minefitsimplemods;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PrecisePortal implements Listener {
    private final MinefitSimpleMods plugin;
    private YamlConfiguration mainConfig = new YamlConfiguration();

    public PrecisePortal(MinefitSimpleMods instance) {
        this.plugin = instance;
        this.mainConfig = this.plugin.loadYaml("PrecisePortal.yml");
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerPortal(PlayerPortalEvent event) {

        // If it's not a nether portal, there's no need to do anything
        if (event.isCancelled() || event.getCause() != TeleportCause.NETHER_PORTAL) {
            return;
        }

        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        int searchRadius = this.mainConfig.getInt(".worlds." + worldName + ".search-radius", 16);
        event.setSearchRadius(searchRadius);
        int creationRadius = this.mainConfig.getInt(".worlds." + worldName + ".create-radius", 16);
        event.setCreationRadius(creationRadius);
    }
}
