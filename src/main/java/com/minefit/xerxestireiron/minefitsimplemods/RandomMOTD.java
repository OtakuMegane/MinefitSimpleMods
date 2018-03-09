package com.minefit.xerxestireiron.minefitsimplemods;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class RandomMOTD implements Listener
{
	private final MinefitSimpleMods plugin;
	private final Random random = new Random();
	private final List<String> MOTDList;
	private int listLength = 0;

	public RandomMOTD(MinefitSimpleMods instance)
	{
		this.plugin = instance;
		this.MOTDList = this.plugin.main_config.getStringList("RandomMOTD.messages");
		this.listLength = this.MOTDList.size();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	private void onServerPing(ServerListPingEvent event)
	{
		if(this.listLength != 0)
		{
			event.setMotd(ChatColor.translateAlternateColorCodes('&', this.MOTDList.get(this.random.nextInt(this.listLength))));
		}
	}
}
