package cn.saltedfish.oreregen;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OreManager {

    private static final File regenOreListFile = new File(OreRegen.getPlugin().getDataFolder(), "regenOreList.yml");
    public static File getRegenOreListFile() {
        return regenOreListFile;
    }

    public List<String> getOreList() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenOreListFile());
        Set<String> keys = cfg.getKeys(false);
        return new ArrayList<>(keys);
    }

    public static @NotNull List<Material> getOreMaterials(String oreName) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenOreListFile());
        List<String> list = cfg.getStringList(oreName + ".Material");
        List<Material> materials = new ArrayList<>();
        for (String s : list) {
            materials.add(Material.matchMaterial(s));
        }
        return materials;
    }

    public static void setOre(String oreName, List<String> oreList) throws IOException {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(getRegenOreListFile());
        cfg.set(oreName + ".Material", oreList);
        cfg.save(getRegenOreListFile());
    }
}
