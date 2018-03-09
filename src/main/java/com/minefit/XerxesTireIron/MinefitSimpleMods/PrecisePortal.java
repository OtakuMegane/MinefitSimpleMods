package com.minefit.XerxesTireIron.MinefitSimpleMods;

import org.bukkit.TravelAgent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PrecisePortal implements Listener {
    private final MinefitSimpleMods plugin;

    public PrecisePortal(MinefitSimpleMods instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerPortal(PlayerPortalEvent event) {

        // If it's not a nether portal, there's no need to do anything
        if (event.getCause() != TeleportCause.NETHER_PORTAL) {
            return;
        }

        event.setCancelled(false);
        TravelAgent pta = event.getPortalTravelAgent();
        Player player = event.getPlayer();
        int searchRadius = this.plugin.main_config
                .getInt("PrecisePortal.worlds." + player.getWorld().getName() + ".search-radius", 16);
        pta.setSearchRadius(searchRadius);

        int creationRadius = this.plugin.main_config
                .getInt("PrecisePortal.worlds." + player.getWorld().getName() + ".create-radius", 16);
        pta.setCreationRadius(creationRadius);
    }
}
