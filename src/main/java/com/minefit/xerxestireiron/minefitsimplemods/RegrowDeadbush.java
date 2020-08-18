package com.minefit.xerxestireiron.minefitsimplemods;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RegrowDeadbush implements Listener {
    private final MinefitSimpleMods plugin;
    private final Random random = new Random();
    private YamlConfiguration mainConfig = new YamlConfiguration();

    public RegrowDeadbush(MinefitSimpleMods instance) {
        this.plugin = instance;
        this.mainConfig = this.plugin.loadYaml("RegrowDeadbush.yml");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack usedItem = event.getItem();
            Block block = event.getClickedBlock();
            World world = block.getWorld();
            String worldName = world.getName();

            if (!this.mainConfig.getBoolean("worlds." + worldName + ".enabled")) {
                return;
            }

            if (usedItem.getType() == Material.BONE_MEAL) {
                Location bLocation = block.getLocation();
                int growRadius = this.mainConfig.getInt("worlds." + worldName + ".deadbush-grow-radius");
                int growPercent = this.mainConfig.getInt("worlds." + worldName + ".deadbush-grow-percentage");
                int x = (int) bLocation.getX();
                int y = (int) bLocation.getY();
                int z = (int) bLocation.getZ();

                for (int x1 = 0 - growRadius; x1 <= growRadius; x1++) {
                    for (int z1 = 0 - growRadius; z1 <= growRadius; z1++) {
                        for (int y1 = -2; y1 <= 2; y1++) {
                            Block block2 = bLocation.getWorld().getBlockAt(x + x1, y + y1, z + z1);

                            if (isSand(block2) && this.random.nextInt(100) < growPercent) {
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

    private boolean isSand(Block block) {
        Material blockType = block.getType();
        return blockType == Material.SAND || blockType == Material.RED_SAND;
    }
}
