package com.minefit.xerxestireiron.minefitsimplemods;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class RandomMOTD implements Listener {
    private final MinefitSimpleMods plugin;
    private final Random random = new Random();
    private final List<String> MOTDMessages;
    private int messageCount = 0;

    public RandomMOTD(MinefitSimpleMods instance) {
        this.plugin = instance;
        this.MOTDMessages = this.plugin.loadYaml("MOTDMessages.yml").getStringList("messages");
        this.messageCount = this.MOTDMessages.size();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onServerPing(ServerListPingEvent event) {
        if (this.messageCount != 0) {
            event.setMotd(ChatColor.translateAlternateColorCodes('&',
                    this.MOTDMessages.get(this.random.nextInt(this.messageCount))));
        }
    }
}
