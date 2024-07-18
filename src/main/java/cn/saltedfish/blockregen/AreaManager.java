package cn.saltedfish.blockregen;

import cn.saltedfish.blockregen.Data.JsonFileManager;
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

    private static final File regenAreaListFile = new File(BlockRegen.getPlugin().getDataFolder(), "regenAreaList.yml");
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

    public static @NotNull List<String> getAreaRegenBlockList(String areaName){
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
        return cfg.getStringList(areaName + ".RegenBlock");
    }

    public static @NotNull String getAreaInfo(String areaName) {
        World world = getAreaWorld(areaName);
        Location loc1 = getAreaLoc1(areaName);
        Location loc2 = getAreaLoc2(areaName);
        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();
        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();
        int all = (x2 - x1) * (y2 - y1) * (z2 - z1);

        String worldName = world.getName();
        String loc1String = "[" + x1 + "," + y1 + "," + z1 + "]";
        String loc2String = "[" + x2 + "," + y2 + "," + z2 + "]";
        String activated = String.valueOf(JsonFileManager.getTagAmount(areaName));
        String allString = String.valueOf(all);
        String blockList = String.valueOf(getAreaRegenBlockList(areaName));

        return BlockRegen.getLanguage("Area.Info")
                .replaceAll("%AreaName", areaName)
                .replaceAll("%WorldName", worldName)
                .replaceAll("%Loc1", loc1String)
                .replaceAll("%Loc2", loc2String)
                .replaceAll("%Activated", activated)
                .replaceAll("%All", allString)
                .replaceAll("%BlockList", blockList);
    }

    public static void setArea(String areaName,World world, Location loc1, Location loc2){
        try {
            File jsonFIle = new File(BlockRegen.getPlugin().getDataFolder() + "/data/", areaName + ".json");
            if (!jsonFIle.exists()) {
                jsonFIle.createNewFile();
            }
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
            List<Integer> loc1s = new ArrayList<>();
            loc1s.add(loc1.getBlockX());
            loc1s.add(loc1.getBlockY());
            loc1s.add(loc1.getBlockZ());
            List<Integer> loc2s = new ArrayList<>();
            loc2s.add(loc2.getBlockX());
            loc2s.add(loc2.getBlockY());
            loc2s.add(loc2.getBlockZ());
            cfg.set(areaName + ".World", world.getName());
            cfg.set(areaName + ".Loc1", loc1s);
            cfg.set(areaName + ".Loc2", loc2s);
            cfg.save(getRegenAreaListFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeArea(String areaName) {
        try {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
            cfg.set(areaName, null);
            File jsonFIle = new File(BlockRegen.getPlugin().getDataFolder() + "/data/", areaName + ".json");
            if (jsonFIle.exists()) {
                jsonFIle.delete();
            }
            cfg.save(getRegenAreaListFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addRegenBlock(String areaName, String blockName) {
        try {
            File jsonFIle = new File(BlockRegen.getPlugin().getDataFolder() + "/data/", areaName + ".json");
            if (!jsonFIle.exists()) {
                jsonFIle.createNewFile();
            }
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
            List<String> regenBlockList = cfg.getStringList(areaName + ".RegenBlock");
            regenBlockList.add(blockName);
            cfg.set(areaName + ".RegenBlock", regenBlockList);
            cfg.save(getRegenAreaListFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeRegenBlock(String areaName, String blockName) {
        try {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenAreaListFile());
            List<String> regenBlockList = cfg.getStringList(areaName + ".RegenBlock");
            regenBlockList.remove(blockName);
            cfg.set(areaName + ".RegenBlock", regenBlockList);
            File jsonFIle = new File(BlockRegen.getPlugin().getDataFolder() + "/data/", areaName + ".json");

            if (jsonFIle.exists()) {
                jsonFIle.delete();
                jsonFIle.createNewFile();
                writeInformation(areaName);
            }
            cfg.save(getRegenAreaListFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInformation(String areaName){

        File file = new File(BlockRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");

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
                    for (int i = 0; i < getAreaRegenBlockList(areaName).size(); i++) {
                        if (BlockManager.getBlockMaterialList(getAreaRegenBlockList(areaName).get(i)).get(0) == world.getBlockAt(x, y, z).getType()){

                            if (!file.exists()){
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            material = world.getBlockAt(x, y, z).getType();
                            materialList = BlockManager.getBlockMaterialList(getAreaRegenBlockList(areaName).get(i));

                            JsonFileManager.setRegenBlockType(areaName, String.valueOf(a), getAreaRegenBlockList(areaName).get(i));
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

    public static void initializeJsonFile() {
        File file = new File(BlockRegen.getPlugin().getDataFolder() + "/data/");
        if (file.isDirectory()) {
            String[] files = file.list();
            if (files != null && files.length > 0) {
                for (String areaName : AreaManager.getRegenAreaList()) {
                    for (int j = 0; j < JsonFileManager.getTagAmount(areaName); j++) {
                        Material jsonMaterial = JsonFileManager.getMaterial(areaName, String.valueOf(j));
                        List<Material> jsonMaterialList = JsonFileManager.getMaterialList(areaName, String.valueOf(j));
                        if (jsonMaterialList.indexOf(jsonMaterial) != 0) {
                            JsonFileManager.setMaterial(areaName, String.valueOf(j), jsonMaterialList.get(0));
                            JsonFileManager.setTime(areaName, String.valueOf(j), 0L);
                            JsonFileManager.setInTimer(areaName, String.valueOf(j), false);
                        }
                    }
                }
            }
        }
    }
}
