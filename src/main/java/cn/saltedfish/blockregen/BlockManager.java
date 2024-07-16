package cn.saltedfish.blockregen;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockManager {

    private static final File regenBlockListFile = new File(BlockRegen.getPlugin().getDataFolder(), "regenBlockList.yml");
    public static File getRegenBlockListFile() {
        return regenBlockListFile;
    }

    public List<String> getBlockList() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
        Set<String> keys = cfg.getKeys(false);
        return new ArrayList<>(keys);
    }

    public static @NotNull List<Material> getBlockMaterialList(String oreName) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
        List<String> list = cfg.getStringList(oreName + ".Material");
        List<Material> materials = new ArrayList<>();
        for (String s : list) {
            materials.add(Material.matchMaterial(s));
        }
        return materials;
    }

    public static void setBlock(String oreName, List<String> oreList) throws IOException {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenBlockListFile());
        cfg.set(oreName + ".Material", oreList);
        cfg.save(getRegenBlockListFile());
    }
}
