package com.minefit.xerxestireiron.minefitsimplemods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class MinefitSimpleMods extends JavaPlugin implements Listener {

    final YamlConfiguration main_config = new YamlConfiguration();
    final YamlConfiguration spawn_config = new YamlConfiguration();
    final Logger logger = Logger.getLogger("Minecraft");
    private PrecisePortal precisePortal;
    private RegrowDeadbush regrowDeadbush;
    private SpawnPoint spawnPoint;
    private MinecartFix minecartFix;
    private RandomMOTD randomMOTD;
    private CreeperNerf creeperNerf;
    private BlockData blockData;

    Random random = new Random();
    String name = getServer().getClass().getPackage().getName();
    String version = name.substring(name.lastIndexOf('.') + 1);

    public void onEnable() {
        try {
            loadYaml("config.yml", "MinefitSimpleMods");
            loadYaml("spawn.yml", "Spawn");

            if (this.main_config.getBoolean("MinefitSimpleMods.enablePrecisePortal")) {
                loadYaml("PrecisePortal.yml", "PrecisePortal");
                this.precisePortal = new PrecisePortal(this);
                this.getServer().getPluginManager().registerEvents(precisePortal, this);
                this.logger.info("[Minefit SimpleMods] PrecisePortal enabled!");
            }

            if (this.main_config.getBoolean("MinefitSimpleMods.enableRegrowDeadbush")) {
                loadYaml("RegrowDeadbush.yml", "RegrowDeadbush");
                this.regrowDeadbush = new RegrowDeadbush(this);
                this.getServer().getPluginManager().registerEvents(regrowDeadbush, this);
                this.logger.info("[Minefit SimpleMods] RegrowDeadBush enabled!");
            }

            if (this.main_config.getBoolean("MinefitSimpleMods.enableSpawnPoint")) {
                this.spawnPoint = new SpawnPoint(this);
                this.getServer().getPluginManager().registerEvents(spawnPoint, this);
                this.logger.info("[Minefit SimpleMods] SpawnPoint enabled!");
            }

            if (this.main_config.getBoolean("MinefitSimpleMods.enableMinecartFix")) {
                this.minecartFix = new MinecartFix(this);
                this.getServer().getPluginManager().registerEvents(minecartFix, this);
                this.logger.info("[Minefit SimpleMods] MinecartFix enabled!");
            }

            if (this.main_config.getBoolean("MinefitSimpleMods.enableRandomMOTD")) {
                loadYaml("RandomMOTD.yml", "RandomMOTD");
                this.randomMOTD = new RandomMOTD(this);
                this.getServer().getPluginManager().registerEvents(randomMOTD, this);
                this.logger.info("[Minefit SimpleMods] RandomMOTD enabled!");
            }

            if (this.main_config.getBoolean("MinefitSimpleMods.enableCreeperNerf")) {
                loadYaml("CreeperNerf.yml", "CreeperNerf");
                this.creeperNerf = new CreeperNerf(this);
                this.getServer().getPluginManager().registerEvents(creeperNerf, this);
                this.logger.info("[Minefit SimpleMods] CreeperNerf enabled!");
            }

            if (this.main_config.getBoolean("MinefitSimpleMods.enableBlockData")) {
                loadYaml("BlockData.yml", "BlockData");
                this.blockData = new BlockData(this);
                this.getServer().getPluginManager().registerEvents(blockData, this);
                this.logger.info("[Minefit SimpleMods] BlockData enabled!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.logger.info("[Minefit SimpleMods] Minefit SimpleMods enabled!");
    }

    public YamlConfiguration getSectionAsConfig(YamlConfiguration config, String path) {
        YamlConfiguration newConfig = new YamlConfiguration();
        ConfigurationSection section = config.getConfigurationSection(path);
        for (String key : section.getKeys(true)) {
            newConfig.set(key, section.get(key));
        }
        return newConfig;

    }

    public void onDisable() {
        this.precisePortal = null;
        this.regrowDeadbush = null;
        this.spawnPoint = null;
        this.minecartFix = null;
        this.creeperNerf = null;

        this.logger.info("[Minefit SimpleMods] Minefit SimpleMods disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arguments) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("spawn") && this.spawnPoint != null) {
            this.spawnPoint.moveToSpawn(player);
        } else if (cmd.getName().equalsIgnoreCase("setspawn") && this.spawnPoint != null) {
            this.spawnPoint.setSpawn(player);
        }

        return false;
    }

    private void firstRun(String fileName) throws Exception {
        File configFile = new File(getDataFolder(), fileName);

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            copy(getResource(fileName), configFile);
        }
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadYaml(String fileName, String modName) {
        YamlConfiguration temp_load = new YamlConfiguration();

        try {
            firstRun(fileName);
            temp_load.load(new File(getDataFolder(), fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.main_config.createSection(modName, temp_load.getValues(true));
    }

}
