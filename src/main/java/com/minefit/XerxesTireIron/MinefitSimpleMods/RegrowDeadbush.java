package com.minefit.XerxesTireIron.MinefitSimpleMods;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RegrowDeadbush implements Listener {
    private final MinefitSimpleMods plugin;
    private final Random random = new Random();

    public RegrowDeadbush(MinefitSimpleMods instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack usedItem = event.getItem();
            Block block = event.getClickedBlock();
            String growConfig = "RegrowDeadbush.worlds." + block.getWorld().getName() + ".";

            if (usedItem.getType() == Material.INK_SACK && usedItem.getData().getData() == 15) {
                Location bLocation = block.getLocation();
                int growRadius = this.plugin.main_config.getInt(growConfig + "deadbush-grow-radius");
                int growPercent = this.plugin.main_config.getInt(growConfig + "deadbush-grow-percentage");
                int x = (int) bLocation.getX();
                int y = (int) bLocation.getY();
                int z = (int) bLocation.getZ();

                for (int x1 = 0 - growRadius; x1 <= growRadius; x1++) {
                    for (int z1 = 0 - growRadius; z1 <= growRadius; z1++) {
                        for (int y1 = -2; y1 <= 2; y1++) {
                            Block block2 = bLocation.getWorld().getBlockAt(x + x1, y + y1, z + z1);

                            if (block2.getType().equals(Material.SAND) && this.random.nextInt(100) < growPercent) {
                                Block altBlock = block2.getRelative(BlockFace.UP);

                                if (altBlock.getType().equals(Material.AIR)) {
                                    altBlock.setType(Material.DEAD_BUSH);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
