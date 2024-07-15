package cn.saltedfish.oreregen;

import cn.saltedfish.oreregen.Data.JsonFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AreaManager {

    private static final File regenAreaListFile = new File(OreRegen.getPlugin().getDataFolder(), "regenAreaList.yml");
    public static File getRegenAreaListFile() {
        return regenAreaListFile;
    }

    public static @NotNull List<String> getRegenAreaList() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
        Set<String> keys = cfg.getKeys(false);
        return new ArrayList<>(keys);
    }

    public static World getAreaWorld(String areaName) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
        String worldName = cfg.getString(areaName + ".World");
        assert worldName != null;
        return Bukkit.getWorld(worldName);
    }

    @Contract("_ -> new")
    public static @NotNull Location getAreaLoc1(String areaName) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
        List<String> locations = cfg.getStringList(areaName + ".Loc1");
        String worldName = cfg.getString(areaName + ".World");
        assert worldName != null;
        return new Location(Bukkit.getWorld(worldName), Double.parseDouble(locations.get(0)), Double.parseDouble(locations.get(1)), Double.parseDouble(locations.get(2)));
    }

    @Contract("_ -> new")
    public static @NotNull Location getAreaLoc2(String areaName) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
        List<String> locations = cfg.getStringList(areaName + ".Loc2");
        String worldName = cfg.getString(areaName + ".World");
        assert worldName != null;
        return new Location(Bukkit.getWorld(worldName), Double.parseDouble(locations.get(0)), Double.parseDouble(locations.get(1)), Double.parseDouble(locations.get(2)));
    }

    public static @NotNull List<String> getAreaRegenOreList(String areaName){
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
        return cfg.getStringList(areaName + ".RegenOre");
    }

    public static void setArea(String areaName,World world, Location loc1, Location loc2){
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
        cfg.set(areaName + ".World", world);
        cfg.set(areaName + ".Loc1", loc1);
        cfg.set(areaName + ".Loc2", loc2);
    }

    public static void addRegenOre(String areaName, String oreName){
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
        List<String> regenOreList = cfg.getStringList(areaName + ".RegenOre");
        regenOreList.add(oreName);
        cfg.set(areaName + ".RegenOre", regenOreList);
    }

    public static void writeInformation(String areaName){

        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");

        Location loc1 = getAreaLoc1(areaName);
        Location loc2 = getAreaLoc2(areaName);

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        int a = 0;
        World world = getAreaWorld(areaName);

        Material material;
        List<Material> materialList;

        for (int x = minX; x <= maxX; x++){
            for (int y = minY; y <= maxY; y++){
                for (int z = minZ; z <= maxZ; z++){
                    for (int i = 0; i < getAreaRegenOreList(areaName).size(); i++) {
                        if (OreManager.getOreMaterialList(getAreaRegenOreList(areaName).get(i)).get(0) == world.getBlockAt(x, y, z).getType()){

                            if (!file.exists()){
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            material = world.getBlockAt(x, y, z).getType();
                            materialList = OreManager.getOreMaterialList(getAreaRegenOreList(areaName).get(i));

                            JsonFileManager.setRegenOreType(areaName, String.valueOf(a), getAreaRegenOreList(areaName).get(i));
                            JsonFileManager.setLocation(areaName, String.valueOf(a), new Location(world, x, y, z));
                            JsonFileManager.setMaterial(areaName, String.valueOf(a), material);
                            JsonFileManager.setMaterialList(areaName, String.valueOf(a), materialList);
                            JsonFileManager.setTime(areaName, String.valueOf(a), 0L);
                            JsonFileManager.setInTimer(areaName, String.valueOf(a), false);
                            a++;
                        }
                    }
                }
            }
        }
    }
}
