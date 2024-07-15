package cn.saltedfish.oreregen;

import cn.saltedfish.oreregen.Data.JsonFileManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TimeHandle {

    public static final long DeepOreRegenTime = OreRegen.getPlugin().getConfig().getLong("DeepOreRegenTime");
    public static final long MiddleOreRegenTime = OreRegen.getPlugin().getConfig().getLong("MiddleOreRegenTime");
    private static final int DetectTime = OreRegen.getPlugin().getConfig().getInt("DetectTime");

    public static void runTimeTask(){

        new BukkitRunnable(){
            @Override
            public void run(){
                for (String areaName : AreaManager.getRegenAreaList()) {
                    if (JsonFileManager.getTagAmount(areaName) != 0){
                        for (int j = 0; j < JsonFileManager.getTagAmount(areaName); j++) {
                            long jsonTime = JsonFileManager.getTime(areaName, String.valueOf(j));
                            Material jsonMaterial = JsonFileManager.getMaterial(areaName, String.valueOf(j));
                            List<Material> jsonMaterialList = JsonFileManager.getMaterialList(areaName, String.valueOf(j));
                            int index = jsonMaterialList.indexOf(jsonMaterial) + 1;
                            boolean b = JsonFileManager.getInTimer(areaName, String.valueOf(j));
                            if (index != 1 && index < jsonMaterialList.size() && jsonTime == 0 && b) {
                                JsonFileManager.setTime(areaName, String.valueOf(j), TimeHandle.MiddleOreRegenTime);
                                JsonFileManager.setInTimer(areaName, String.valueOf(j), false);
                            } else if (index == jsonMaterialList.size() &&jsonTime > TimeHandle.DeepOreRegenTime) {
                                JsonFileManager.setTime(areaName, String.valueOf(j), TimeHandle.DeepOreRegenTime);
                                JsonFileManager.setInTimer(areaName, String.valueOf(j), false);
                            }
                            jsonTime = JsonFileManager.getTime(areaName, String.valueOf(j));
                            if (jsonTime > 0){
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
        }.runTaskTimer(OreRegen.getPlugin(), DetectTime * 20L, DetectTime * 20L);
    }
}
