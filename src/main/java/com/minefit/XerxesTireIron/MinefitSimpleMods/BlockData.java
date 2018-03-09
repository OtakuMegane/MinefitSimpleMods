package com.minefit.XerxesTireIron.MinefitSimpleMods;

import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Stairs;

public class BlockData implements Listener{
    private final MinefitSimpleMods plugin;

    public BlockData(MinefitSimpleMods instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if(block == null)
        {
            return;
        }

        Player player = event.getPlayer();

        double humidity = block.getHumidity();
        double biomeTemperature = block.getTemperature();
        double blockTemperature = biomeTemperature;
        player.sendMessage("Material data: " + block.getType().getData().getSimpleName());

        if (block.getState().getData() instanceof Stairs)
        {
            Stairs stairs = (Stairs) block.getState().getData();
            player.sendMessage("Stairs facing: " + stairs.isInverted());
        }

        if(block.getY() > 64)
        {
            int raise = block.getY() - 64;
            blockTemperature = biomeTemperature - raise * 0.05F / 30.0F;
        }

        player.sendMessage("Biome Temperature: " + String.format("%.4g%n", biomeTemperature) + " | Block Temperature: "
                + String.format("%.4g%n", blockTemperature) + "  " + block.getRelative(BlockFace.UP).getLightFromSky());
    }
}
