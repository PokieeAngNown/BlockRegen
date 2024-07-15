package cn.saltedfish.oreregen.Listeners;

import cn.saltedfish.oreregen.AreaManager;
import cn.saltedfish.oreregen.Data.JsonFileManager;
import cn.saltedfish.oreregen.OreManager;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.saltedfish.oreregen.AreaManager.getAreaRegenOreList;

public class PlayerEventsListener implements Listener {

    /*
        iron_ore
        stone
        cobblestone
        bedrock

     */


    @EventHandler
    public void onPlayerBreak(@NotNull BlockBreakEvent event) {
        //获得事件的方块和位置
        Material material = event.getBlock().getType();
        Location location = event.getBlock().getLocation();
        World world = location.getWorld();
        assert world != null;

        //循环：有多少个区域
        for (String areaName : AreaManager.getRegenAreaList()) {
            //循环：区域内有多少个Tag
            for (int j = 0; j < JsonFileManager.getTagAmount(areaName); j++) {
                //获得json文件原本的方块和位置
                final Material[] jsonMaterial = {JsonFileManager.getMaterial(areaName, String.valueOf(j))};
                Location jsonLoc = JsonFileManager.getLocation(areaName, String.valueOf(j));
                //判断：json文件的方块和位置都与事件的相同 => 通过
                if (jsonLoc.equals(location) && jsonMaterial[0].equals(material)) {
                    //获取json文件的方块列表
                    List<Material> jsonMaterialList = JsonFileManager.getMaterialList(areaName, String.valueOf(j));
                    //获得事件方块是json文件的方块列表的第几个 => index
                    int index = jsonMaterialList.indexOf(material) + 1;
                    //判断：如果index超出了json文件的方块列表的大小 => 通过
                    if (index >= jsonMaterialList.size()) {
                        //整理json文件
                        JsonFileManager.clearAndSortJson(areaName, String.valueOf(j));
                        return;
                    }
                    //按照MaterialList顺序更改json文件的方块
                    JsonFileManager.setMaterial(areaName, String.valueOf(j), jsonMaterialList.get(index));
                    if (!JsonFileManager.getInTimer(areaName, String.valueOf(j))){
                        if (index == 1){
                            JsonFileManager.setInTimer(areaName, String.valueOf(j), !JsonFileManager.getInTimer(areaName, String.valueOf(j)));
                        }
                    }
                    //重新获取json文件的方块
                    jsonMaterial[0] = JsonFileManager.getMaterial(areaName, String.valueOf(j));
                    //在相关位置放置下一个方块
                    location.getBlock().setType(jsonMaterial[0]);
                    //获取上一个方块的掉落物并掉落
                    ItemStack itemStack = new ItemStack(material, 1);
                    world.dropItemNaturally(location, itemStack);
                    //取消事件
                    event.setCancelled(true);
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
            for (int j = 0; j <= JsonFileManager.getTagAmount(areaName); j++){
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
                            JsonFileManager.setTime(areaName, String.valueOf(a), 0L);
                            JsonFileManager.setInTimer(areaName, String.valueOf(a), false);

                            JsonObject mainJsonObject = JsonFileManager.getMainJsonObject(areaName);
                            JsonFileManager.sortAndWriteToJson(areaName, mainJsonObject);
                        }
                    }
                }
            }
        }
    }
}
