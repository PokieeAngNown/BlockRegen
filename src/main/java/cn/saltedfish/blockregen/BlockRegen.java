package cn.saltedfish.blockregen;

import cn.saltedfish.blockregen.Commands.BlockRegenCommand;
import cn.saltedfish.blockregen.Listeners.PlayerEventsListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;

public final class BlockRegen extends JavaPlugin {

    public static boolean isReload = false;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Info the plugin
        if (!isReload){
            this.getLogger().info(name + "plugin is enabled");
            this.getLogger().info("Here is the plugin's information");
            this.getLogger().info("> Name:" + name);
            this.getLogger().info("> Version:" + version);
            this.getLogger().info("> Authors:" + authors);
        }

        //Register
        regCommands();
        regTabs();
        regListeners();

        //Initialize resource in pack
        initializeResource();


        AreaManager.writeInformation("Area1");

        //TimeTask
        TimeHandle.runTimeTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info(name + "plugin is disabled");
    }

    @Override
    public void onLoad(){
        //Plugin loaded logic
        this.getLogger().info(name + "plugin is loaded");
    }

    /*
        [Info]
     */
    private final String name = this.getDescription().getName();
    private final String version = this.getDescription().getVersion();
    private final List<String> authors = this.getDescription().getAuthors();

    /*
        [Reg]
     */
    private void regCommands(){
        Objects.requireNonNull(this.getCommand("blockregen")).setExecutor(new BlockRegenCommand());
    }

    private void regTabs(){
        Objects.requireNonNull(this.getCommand("blockregen")).setTabCompleter(new BlockRegenCommand());
    }

    private void regListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerEventsListener(), this);
    }

    /*
        [Resource]
     */
    private void initializeResource(){
        if (!new File(this.getDataFolder(), "config.yml").exists()){
            getLogger().warning("Could not find config.yml");
            getLogger().info("Recreating config.yml");
            saveResource("config.yml", true);
        }else{
            getLogger().info("Found config.yml");
        }

        if (!new File(this.getDataFolder(), "regenBlockList.yml").exists()){
            getLogger().warning("Could not find regenBlockList.yml");
            getLogger().info("Recreating regenBlockList.yml");
            saveResource("regenBlockList.yml", true);
        }else{
            getLogger().info("Found regenBlockList.yml");
        }

        if (!new File(this.getDataFolder(), "regenAreaList.yml").exists()){
            getLogger().warning("Could not find regenAreaList.yml");
            getLogger().info("Recreating regenAreaList.yml");
            saveResource("regenAreaList.yml", true);
        }else{
            getLogger().info("Found regenAreaList.yml");
        }
        if (!new File(this.getDataFolder() + "/data").exists()){
            new File(this.getDataFolder() + "/data").mkdir();
        }
    }

    /*
        [Function]
     */
    public static Plugin getPlugin(){
        return Bukkit.getPluginManager().getPlugin("OreRegen");
    }
}
