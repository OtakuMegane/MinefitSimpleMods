package com.minefit.xerxestireiron.minefitsimplemods;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnPoint implements Listener {
    private final MinefitSimpleMods plugin;

    public SpawnPoint(MinefitSimpleMods instance) {
        this.plugin = instance;

        try {
            this.plugin.spawn_config.load(new File(this.plugin.getDataFolder(), "SpawnPoint.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private Location customSpawn(Player player) {
        World world = player.getWorld();
        Location spawnLocation = new Location(world, 0.0D, 0.0D, 0.0D);
        Location worldDefault = world.getSpawnLocation();
        spawnLocation
                .setX(this.plugin.spawn_config.getDouble("worlds." + world.getName() + ".spawnX", worldDefault.getX()));
        spawnLocation
                .setY(this.plugin.spawn_config.getDouble("worlds." + world.getName() + ".spawnY", worldDefault.getY()));
        spawnLocation
                .setZ(this.plugin.spawn_config.getDouble("worlds." + world.getName() + ".spawnZ", worldDefault.getZ()));
        spawnLocation.setYaw((float) this.plugin.spawn_config.getDouble("worlds." + world.getName() + ".spawnYaw",
                worldDefault.getYaw()));
        spawnLocation.setPitch((float) this.plugin.spawn_config.getDouble("worlds." + world.getName() + ".spawnPitch",
                worldDefault.getPitch()));
        return spawnLocation;
    }

    private boolean worldEnabled(World world) {
        if (this.plugin.spawn_config.getBoolean("worlds." + world.getName() + ".enabled")) {
            return true;
        }

        return false;
    }

    private boolean customSet(World world) {
        if (this.plugin.spawn_config.getBoolean("worlds." + world.getName() + ".customSet")) {
            return true;
        }

        return false;
    }

    private void spawnCommand(Player player) {
        if (!player.hasPermission("simplemods.gospawn")) {
            player.sendMessage("You are not allowed to use the /spawn command.");
            return;
        }

        moveToSpawn(player);
    }

    public void moveToSpawn(Player player) {
        World world = player.getWorld();

        if (customSet(world)) {
            player.teleport(customSpawn(player));
        } else {
            Location defaultSpawn = world.getSpawnLocation();
            player.teleport(new Location(world, defaultSpawn.getX(), world.getHighestBlockYAt(defaultSpawn),
                    defaultSpawn.getZ()));
        }
    }

    private void setOriginalSpawn(World world) {
        String worldName = world.getName();

        if (!this.plugin.spawn_config.contains("worlds." + worldName + ".originalSpawnX")
                || !this.plugin.spawn_config.isSet("worlds." + worldName + ".originalSpawnX")) {
            this.plugin.spawn_config.set("worlds." + worldName + ".originalSpawnX", world.getSpawnLocation().getX());
        }

        if (!this.plugin.spawn_config.contains("worlds." + worldName + ".originalSpawnY")
                || !this.plugin.spawn_config.isSet("worlds." + worldName + ".originalSpawnY")) {
            plugin.spawn_config.set("worlds." + worldName + ".originalSpawnY", world.getSpawnLocation().getY());
        }

        if (!this.plugin.spawn_config.contains("worlds." + worldName + ".originalSpawnZ")
                || !this.plugin.spawn_config.isSet("worlds." + worldName + ".originalSpawnZ")) {
            this.plugin.spawn_config.set("worlds." + worldName + ".originalSpawnZ", world.getSpawnLocation().getZ());
        }

        if (!this.plugin.spawn_config.contains("worlds." + worldName + ".originalSpawnYaw")
                || !this.plugin.spawn_config.isSet("worlds." + worldName + ".originalSpawnYaw")) {
            this.plugin.spawn_config.set("worlds." + worldName + ".originalSpawnYaw",
                    world.getSpawnLocation().getYaw());
        }

        if (!this.plugin.spawn_config.contains("worlds." + worldName + ".originalSpawnPitch")
                || !this.plugin.spawn_config.isSet("worlds." + worldName + ".originalSpawnPitch")) {
            this.plugin.spawn_config.set("worlds." + worldName + ".originalSpawnPitch",
                    world.getSpawnLocation().getPitch());
        }

        save();
    }

    public void setSpawn(Player player) {
        if (!player.hasPermission("simplemods.setspawn")) {
            player.sendMessage("You are not allowed to use the /spawn command.");
            return;
        }

        World world = player.getWorld();
        String worldName = world.getName();
        Location newSpawn = player.getLocation();

        if (!this.plugin.spawn_config.getBoolean("worlds." + worldName + ".customSet")) {
            setOriginalSpawn(world);
        }

        world.setSpawnLocation(newSpawn.getBlockX(), newSpawn.getBlockY(), newSpawn.getBlockZ());
        this.plugin.spawn_config.set("worlds." + worldName + ".customSet", true);
        this.plugin.spawn_config.set("worlds." + worldName + ".spawnX", newSpawn.getX());
        this.plugin.spawn_config.set("worlds." + worldName + ".spawnY", newSpawn.getY());
        this.plugin.spawn_config.set("worlds." + worldName + ".spawnZ", newSpawn.getZ());
        this.plugin.spawn_config.set("worlds." + worldName + ".spawnYaw", newSpawn.getYaw());
        this.plugin.spawn_config.set("worlds." + worldName + ".spawnPitch", newSpawn.getPitch());

        save();
    }

    private void save() {
        try {
            this.plugin.spawn_config.save(new File(this.plugin.getDataFolder(), "SpawnPoint.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
