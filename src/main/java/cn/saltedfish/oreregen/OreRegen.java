package cn.saltedfish.oreregen;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public final class OreRegen extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Info the plugin
        this.getLogger().info("OreRegen plugin is enabled");
        this.getLogger().info("Here is the plugin's information");
        this.getLogger().info("> Name:" + name);
        this.getLogger().info("> Version:" + version);
        this.getLogger().info("> Authors:" + authors);

        //Register
        regCommands();
        regTabs();
        regListeners();

        //Initialize resource in pack
        initializeResource();


        AreaManager.createJsonFile("Area1");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void onLoad(){
        //Plugin loaded logic
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

    }

    private void regTabs(){

    }

    private void regListeners(){

    }

    public void reloadPlugin(){
        Bukkit.getPluginManager().disablePlugin(this);
        Bukkit.getPluginManager().enablePlugin(this);
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

        if (!new File(this.getDataFolder(), "regenOreList.yml").exists()){
            getLogger().warning("Could not find regenOreList.yml");
            getLogger().info("Recreating regenOreList.yml");
            saveResource("regenOreList.yml", true);
        }else{
            getLogger().info("Found regenOreList.yml");
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
