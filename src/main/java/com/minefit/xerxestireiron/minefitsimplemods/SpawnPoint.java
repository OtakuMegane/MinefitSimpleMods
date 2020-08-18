package com.minefit.xerxestireiron.minefitsimplemods;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnPoint implements CommandExecutor, Listener {
    private final MinefitSimpleMods plugin;
    private YamlConfiguration mainConfig = new YamlConfiguration();
    private YamlConfiguration spawnConfig = new YamlConfiguration();

    public SpawnPoint(MinefitSimpleMods instance) {
        this.plugin = instance;
        this.mainConfig = this.plugin.loadYaml("SpawnPoint.yml");
        this.spawnConfig = this.plugin.loadYaml("WorldSpawns.yml");
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (worldEnabled(player.getWorld()) && !event.isBedSpawn()) {
            moveToSpawn(player);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (worldEnabled(player.getWorld()) && !player.hasPlayedBefore()) {
            moveToSpawn(player);
        }
    }

    private Location getSpawnLocation(World world) {
        String worldName = world.getName();
        Location spawnLocation = new Location(world, 0.0D, 0.0D, 0.0D);
        Location worldDefault = world.getSpawnLocation();
        spawnLocation.setX(this.spawnConfig.getDouble("worlds." + worldName + ".spawnX", worldDefault.getX()));
        spawnLocation.setY(this.spawnConfig.getDouble("worlds." + worldName + ".spawnY", worldDefault.getY()));
        spawnLocation.setZ(this.spawnConfig.getDouble("worlds." + worldName + ".spawnZ", worldDefault.getZ()));
        spawnLocation
                .setYaw((float) this.spawnConfig.getDouble("worlds." + worldName + ".spawnYaw", worldDefault.getYaw()));
        spawnLocation.setPitch(
                (float) this.spawnConfig.getDouble("worlds." + worldName + ".spawnPitch", worldDefault.getPitch()));
        return spawnLocation;
    }

    private boolean worldEnabled(World world) {
        return this.mainConfig.getBoolean("worlds." + world.getName() + ".enabled", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            return true;
        }

        if (command.getName().equalsIgnoreCase("spawn")) {
            moveToSpawn(player);
        } else if (command.getName().equalsIgnoreCase("setspawn")) {
            setSpawn(player);
        }

        return true;
    }

    public void spawnCommand(Player player, Command command, String label, String[] arguments) {

        if (command.getName().equalsIgnoreCase("spawn")) {
            if (!player.hasPermission("simplemods.spawn")) {
                player.sendMessage("You are not allowed to use the /spawn command.");
                return;
            }

            moveToSpawn(player);
        } else if (command.getName().equalsIgnoreCase("setspawn")) {
            if (!player.hasPermission("simplemods.setspawn")) {
                player.sendMessage("You are not allowed to use the /setspawn command.");
                return;
            }

            setSpawn(player);
        }
    }

    public void moveToSpawn(Player player) {
        player.teleport(getSpawnLocation(player.getWorld()));
    }

    public void setSpawn(Player player) {
        World world = player.getWorld();
        String worldName = world.getName();
        Location newSpawn = player.getLocation();
        world.setSpawnLocation(newSpawn);
        this.spawnConfig.set("worlds." + worldName + ".spawnX", newSpawn.getX());
        this.spawnConfig.set("worlds." + worldName + ".spawnY", newSpawn.getY());
        this.spawnConfig.set("worlds." + worldName + ".spawnZ", newSpawn.getZ());
        this.spawnConfig.set("worlds." + worldName + ".spawnYaw", newSpawn.getYaw());
        this.spawnConfig.set("worlds." + worldName + ".spawnPitch", newSpawn.getPitch());
        this.plugin.saveYaml(this.spawnConfig, "WorldSpawns.yml");
    }
}
