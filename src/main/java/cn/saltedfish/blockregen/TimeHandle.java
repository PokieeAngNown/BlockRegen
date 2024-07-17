package cn.saltedfish.blockregen;

import cn.saltedfish.blockregen.Data.JsonFileManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;

public class TimeHandle {

    public static final long DeepBlockRegenTime = BlockRegen.getPlugin().getConfig().getLong("DeepBlockRegenTime");
    public static final long MiddleBlockRegenTime = BlockRegen.getPlugin().getConfig().getLong("MiddleBlockRegenTime");
    private static final int DetectTime = BlockRegen.getPlugin().getConfig().getInt("DetectTime");

    public static void runTimeTask(){

        new BukkitRunnable(){
            @Override
            public void run(){
                File file = new File(BlockRegen.getPlugin().getDataFolder() + "/data/");
                if (file.isDirectory()) {
                    String[] files = file.list();
                    if (files != null && files.length > 0) {
                        function();
                    }
                }
            }
        }.runTaskTimer(BlockRegen.getPlugin(), DetectTime * 20L, DetectTime * 20L);
    }

    private static void function() {
        for (String areaName : AreaManager.getRegenAreaList()) {
            if (JsonFileManager.getTagAmount(areaName) != 0) {
                for (int j = 0; j < JsonFileManager.getTagAmount(areaName); j++) {
                    long jsonTime = JsonFileManager.getTime(areaName, String.valueOf(j));
                    Material jsonMaterial = JsonFileManager.getMaterial(areaName, String.valueOf(j));
                    List<Material> jsonMaterialList = JsonFileManager.getMaterialList(areaName, String.valueOf(j));
                    int index = jsonMaterialList.indexOf(jsonMaterial) + 1;
                    boolean b = JsonFileManager.getInTimer(areaName, String.valueOf(j));
                    if (index != 1 && index < jsonMaterialList.size() && jsonTime == 0 && b) {
                        JsonFileManager.setTime(areaName, String.valueOf(j), TimeHandle.MiddleBlockRegenTime);
                        JsonFileManager.setInTimer(areaName, String.valueOf(j), false);
                    } else if (index == jsonMaterialList.size() && jsonTime > TimeHandle.DeepBlockRegenTime) {
                        JsonFileManager.setTime(areaName, String.valueOf(j), TimeHandle.DeepBlockRegenTime);
                        JsonFileManager.setInTimer(areaName, String.valueOf(j), false);
                    }
                    jsonTime = JsonFileManager.getTime(areaName, String.valueOf(j));
                    if (jsonTime > 0) {
                        jsonTime = jsonTime - DetectTime;
                        JsonFileManager.setTime(areaName, String.valueOf(j), jsonTime);
                    } else {
                        Location jsonLocation = JsonFileManager.getLocation(areaName, String.valueOf(j));
                        World jsonWorld = jsonLocation.getWorld();
                        assert jsonWorld != null;
                        jsonWorld.setType(jsonLocation, jsonMaterialList.get(0));
                        JsonFileManager.setMaterial(areaName, String.valueOf(j), jsonMaterialList.get(0));
                    }
                }
            }
        }
    }
}
