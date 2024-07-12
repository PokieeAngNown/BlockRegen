package cn.saltedfish.oreregen.Listeners;

import cn.saltedfish.oreregen.AreaManager;
import cn.saltedfish.oreregen.Data.JsonFileManager;
import cn.saltedfish.oreregen.OreManager;
import cn.saltedfish.oreregen.OreRegen;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.saltedfish.oreregen.AreaManager.getAreaRegenOreList;

public class PlayerEventsListener implements Listener {
    @EventHandler
    public void onPlayerBreak(@NotNull BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        Location location = event.getBlock().getLocation();
        World world = location.getWorld();
        assert world != null;

        for (String areaName : AreaManager.getRegenAreaList()) {
            for (int j = 0; j < JsonFileManager.getTagAmount(areaName); j++) {
                Location jsonLoc = JsonFileManager.getLocation(areaName, String.valueOf(j));
                final Material[] jsonMaterial = {JsonFileManager.getMaterial(areaName, String.valueOf(j))};
                if (jsonLoc.equals(location) && jsonMaterial[0].equals(material)) {
                    List<Material> materialList = JsonFileManager.getMaterialList(areaName, String.valueOf(j));
                    int index = materialList.indexOf(material);
                    index++;
                    if (index >= materialList.size()) {
                        JsonFileManager.clearAndSortJson(areaName, String.valueOf(j));

                        // 取消倒计时任务
                        Bukkit.getScheduler().cancelTasks(OreRegen.getPlugin());
                        return;
                    }
                    event.setCancelled(true);
                    JsonFileManager.setMaterial(areaName, String.valueOf(j), materialList.get(index));
                    jsonMaterial[0] = JsonFileManager.getMaterial(areaName, String.valueOf(j));
                    location.getBlock().setType(jsonMaterial[0]);
                    ItemStack itemStack = new ItemStack(material, 1);
                    world.dropItemNaturally(location, itemStack);
                    if (index < materialList.size() -1){
                        int finalJ = j;
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                JsonFileManager.setMaterial(areaName, String.valueOf(finalJ), materialList.get(0));
                                jsonMaterial[0] = JsonFileManager.getMaterial(areaName, String.valueOf(finalJ));
                                location.getBlock().setType(jsonMaterial[0]);
                            }
                        }.runTaskLater(OreRegen.getPlugin(), OreRegen.getPlugin().getConfig().getInt("MiddleOreRegenTime") * 20L);
                    }
                    if (index == materialList.size() - 1) {
                        int finalJ = j;
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                JsonFileManager.setMaterial(areaName, String.valueOf(finalJ), materialList.get(0));
                                jsonMaterial[0] = JsonFileManager.getMaterial(areaName, String.valueOf(finalJ));
                                location.getBlock().setType(jsonMaterial[0]);
                            }
                        }.runTaskLater(OreRegen.getPlugin(), OreRegen.getPlugin().getConfig().getInt("DeepOreRegenTime") * 20L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(@NotNull BlockPlaceEvent event) {
        Material material = event.getBlock().getType();
        Location location = event.getBlock().getLocation();
        World world = location.getWorld();
        assert world != null;

        for (String areaName : AreaManager.getRegenAreaList()){
            for (int j = 0; j < JsonFileManager.getTagAmount(areaName); j++){
                if (!JsonFileManager.isLocationSet(areaName, location)){
                    for (int i = 0; i < getAreaRegenOreList(areaName).size(); i++){
                        List<Material> materialList = OreManager.getOreMaterialList(getAreaRegenOreList(areaName).get(i));

                        int x = location.getBlockX();
                        int y = location.getBlockY();
                        int z = location.getBlockZ();
                        int a = JsonFileManager.getTagAmount(areaName);
                        if (OreManager.getOreMaterialList(getAreaRegenOreList(areaName).get(i)).get(0) == material){
                            JsonFileManager.setRegenOreType(areaName, String.valueOf(a), getAreaRegenOreList(areaName).get(i));
                            JsonFileManager.setLocation(areaName, String.valueOf(a), new Location(world, x, y, z));
                            JsonFileManager.setMaterial(areaName, String.valueOf(a), material);
                            JsonFileManager.setMaterialList(areaName, String.valueOf(a), materialList);

                            JsonObject mainJsonObject = JsonFileManager.getMainJsonObject(areaName);
                            JsonFileManager.sortAndWriteToJson(areaName, mainJsonObject);
                        }
                    }
                }
            }
        }
    }
}
