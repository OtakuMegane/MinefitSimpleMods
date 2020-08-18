package com.minefit.xerxestireiron.minefitsimplemods;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

public class MinefitSimpleMods extends JavaPlugin implements Listener {

    private YamlConfiguration config = new YamlConfiguration();
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
        this.saveDefaultConfig();
        copyConfigIfNotPresent("config.yml");
        copyConfigIfNotPresent("BlockData.yml");
        copyConfigIfNotPresent("CreeperNerf.yml");
        copyConfigIfNotPresent("MOTDMessages.yml");
        copyConfigIfNotPresent("PrecisePortal.yml");
        copyConfigIfNotPresent("RegrowDeadbush.yml");
        copyConfigIfNotPresent("SpawnPoint.yml");
        this.config = loadYaml("config.yml");

        if (this.config.getBoolean("enablePrecisePortal")) {
            this.precisePortal = new PrecisePortal(this);
            this.getServer().getPluginManager().registerEvents(precisePortal, this);
            this.logger.info("[MinefitSimpleMods] PrecisePortal enabled!");
        }

        if (this.config.getBoolean("enableRegrowDeadbush")) {
            this.regrowDeadbush = new RegrowDeadbush(this);
            this.getServer().getPluginManager().registerEvents(regrowDeadbush, this);
            this.logger.info("[MinefitSimpleMods] RegrowDeadbush enabled!");
        }

        if (this.config.getBoolean("enableSpawnPoint")) {
            this.spawnPoint = new SpawnPoint(this);
            this.getServer().getPluginManager().registerEvents(this.spawnPoint, this);
            this.getCommand("spawn").setExecutor(this.spawnPoint);
            this.getCommand("setspawn").setExecutor(this.spawnPoint);
            this.logger.info("[MinefitSimpleMods] SpawnPoint loaded!");
        }

        if (this.config.getBoolean("enableMinecartFix")) {
            this.minecartFix = new MinecartFix(this);
            this.getServer().getPluginManager().registerEvents(minecartFix, this);
            this.logger.info("[MinefitSimpleMods] MinecartFix enabled!");
        }

        if (this.config.getBoolean("enableRandomMOTD")) {
            this.randomMOTD = new RandomMOTD(this);
            this.getServer().getPluginManager().registerEvents(randomMOTD, this);
            this.logger.info("[MinefitSimpleMods] RandomMOTD enabled!");
        }

        if (this.config.getBoolean("enableCreeperNerf")) {
            this.creeperNerf = new CreeperNerf(this);
            this.getServer().getPluginManager().registerEvents(creeperNerf, this);
            this.logger.info("[MinefitSimpleMods] CreeperNerf enabled!");
        }

        if (this.config.getBoolean("enableBlockData")) {
            this.blockData = new BlockData(this);
            this.getServer().getPluginManager().registerEvents(blockData, this);
            this.logger.info("[MinefitSimpleMods] BlockData enabled!");
        }

        this.logger.info("[MinefitSimpleMods] Minefit SimpleMods enabled!");
    }

    public void onDisable() {
        this.precisePortal = null;
        this.regrowDeadbush = null;
        this.spawnPoint = null;
        this.minecartFix = null;
        this.randomMOTD = null;
        this.creeperNerf = null;
        this.blockData = null;
        this.logger.info("[MinefitSimpleMods] Minefit SimpleMods disabled!");
    }

    private boolean copyConfigIfNotPresent(String fileName) {
        File configFile = new File(getDataFolder(), fileName);

        if (!configFile.exists()) {
            this.saveResource(fileName, false);
            return true;
        }

        return false;
    }

    public YamlConfiguration loadYaml(String fileName) {
        YamlConfiguration newConfig = new YamlConfiguration();

        try {
            newConfig.load(new File(getDataFolder(), fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newConfig;
    }

    public boolean saveYaml(YamlConfiguration config, String fileName) {
        try {
            config.load(new File(getDataFolder(), fileName));
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
