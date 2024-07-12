package cn.saltedfish.oreregen.Data;

import cn.saltedfish.oreregen.OreRegen;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonFileManager {

    public static Integer getTagAmount(String areaName){
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        if(!file.exists()){
            return 0;
        }
        try (FileReader fr = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String s;
            while((s = br.readLine()) != null){
                sb.append(s);
            }
            Pattern pattern = Pattern.compile("RegenOreType");
            Matcher matcher = pattern.matcher(sb.toString());

            int num = 0;
            while(matcher.find()){
                num++;
            }
            return num;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setRegenOreType(String areaName, String tag, String oreName) {
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonObject main;

        // 尝试读取文件内容
        try (FileReader fr = new FileReader(file)) {
            main = JsonParser.parseReader(fr).getAsJsonObject();
        } catch (IOException | IllegalStateException e) {
            // 如果文件读取失败或者内容为空，创建一个新的 JsonObject
            main = new JsonObject();
        }

        // 获取或创建 tag 节点
        JsonObject setting = main.has(tag) ? main.getAsJsonObject(tag) : new JsonObject();
        setting.addProperty("RegenOreType", oreName);
        main.add(tag, setting);

        // 写回文件
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(main.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRegenOreType(String areaName, String tag){
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonParser parser = new JsonParser();
        try (FileReader fr = new FileReader(file)){
            JsonObject main = parser.parse(fr).getAsJsonObject();
            return main.get(tag).getAsJsonObject().get("RegenOreType").getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setLocation(String areaName, String tag, @NotNull Location location) {
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonObject main;

        // 尝试读取文件内容
        try (FileReader fr = new FileReader(file)) {
            main = JsonParser.parseReader(fr).getAsJsonObject();
        } catch (IOException | IllegalStateException e) {
            // 如果文件读取失败或者内容为空，创建一个新的 JsonObject
            main = new JsonObject();
        }

        // 获取或创建 tag 节点
        JsonObject setting = main.has(tag) ? main.getAsJsonObject(tag) : new JsonObject();
        World w = location.getWorld();
        assert w != null;
        setting.addProperty("World", w.getName());
        setting.addProperty("Location", String.format("[%d,%d,%d]", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        main.add(tag, setting);

        // 写回文件
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(main.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull Location getLocation(String areaName, String tag){
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonParser parser = new JsonParser();
        String worldName;
        World world;
        int x;
        int y;
        int z;
        try (FileReader fr = new FileReader(file)){
            JsonObject main = parser.parse(fr).getAsJsonObject();
            worldName = main.get(tag).getAsJsonObject().get("World").getAsString();
            world = Bukkit.getWorld(worldName);
            String locationStr = main.get(tag).getAsJsonObject().get("Location").getAsString();
            locationStr = locationStr.replace("\"", "").replace("[", "").replace("]", "");
            String[] locs = locationStr.split(",");
            x = Integer.parseInt(locs[0]);
            y = Integer.parseInt(locs[1]);
            z = Integer.parseInt(locs[2]);
            return new Location(world, x, y, z);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setMaterial(String areaName, String tag, @NotNull Material material) {
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonObject main;

        // 尝试读取文件内容
        try (FileReader fr = new FileReader(file)) {
            main = JsonParser.parseReader(fr).getAsJsonObject();
        } catch (IOException | IllegalStateException e) {
            // 如果文件读取失败或者内容为空，创建一个新的 JsonObject
            main = new JsonObject();
        }

        // 获取或创建 tag 节点
        JsonObject setting = main.has(tag) ? main.getAsJsonObject(tag) : new JsonObject();
        setting.addProperty("Material", material.getTranslationKey().replace("block.", "").replace("minecraft.", "minecraft:"));
        main.add(tag, setting);

        // 写回文件
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(main.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Material getMaterial(String areaName, String tag){
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonParser parser = new JsonParser();
        String materialName;
        try (FileReader fr = new FileReader(file)){
            JsonObject main = parser.parse(fr).getAsJsonObject();
            materialName = main.get(tag).getAsJsonObject().get("Material").getAsString();
            return Material.matchMaterial(materialName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setMaterialList(String areaName, String tag, @NotNull List<Material> materialList) {
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonObject main;

        // 尝试读取文件内容
        try (FileReader fr = new FileReader(file)) {
            main = JsonParser.parseReader(fr).getAsJsonObject();
        } catch (IOException | IllegalStateException e) {
            // 如果文件读取失败或者内容为空，创建一个新的 JsonObject
            main = new JsonObject();
        }

        // 获取或创建 tag 节点
        JsonObject setting = main.has(tag) ? main.getAsJsonObject(tag) : new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (Material material : materialList) {
            jsonArray.add(material.getTranslationKey().replace("block.", "").replace("minecraft.", "minecraft:"));
        }
        setting.add("MaterialList", jsonArray);
        main.add(tag, setting);

        // 写回文件
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(main.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Material> getMaterialList(String areaName, String tag){
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonParser parser = new JsonParser();
        String materialName;
        List<Material> materialList = new ArrayList<>();
        try (FileReader fr = new FileReader(file)){
            JsonObject main = parser.parse(fr).getAsJsonObject();
            JsonArray list = main.get(tag).getAsJsonObject().get("MaterialList").getAsJsonArray();
            for (int i = 0; i < list.size(); i++) {
                materialName = list.get(i).getAsString();
                materialList.add(Material.matchMaterial(materialName));
            }
            return materialList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearAndSortJson(String areaName, String tag) {
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonParser parser = new JsonParser();
        JsonObject main;

        try (FileReader fr = new FileReader(file)) {
            main = parser.parse(fr).getAsJsonObject();

            // 移除指定tag节点
            main.remove(tag);

            cleanEmptyNodes(main);

            // 重新排序并写回文件
            sortAndWriteToJson(areaName, main);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void cleanEmptyNodes(@NotNull JsonObject jsonObject) {
        jsonObject.entrySet().removeIf(entry -> entry.getValue().isJsonNull());
    }

    public static void sortAndWriteToJson(String areaName, @NotNull JsonObject main) {
        // 使用TreeMap按照 "0123" 的顺序排序
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        TreeMap<String, JsonElement> sorted = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));
        main.entrySet().stream()
                .filter(entry -> entry.getKey().matches("\\d+")) // 过滤出数字字符串的键
                .forEach(entry -> sorted.put(entry.getKey(), entry.getValue()));

        // 创建一个新的JsonObject，并将排序后的内容重新编号
        JsonObject sortedJsonObject = new JsonObject();
        int newKey = 0;
        for (Map.Entry<String, JsonElement> entry : sorted.entrySet()) {
            sortedJsonObject.add(String.valueOf(newKey++), entry.getValue());
        }

        // 写回文件
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(sortedJsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isLocationSet(String areaName, Location location){
        return getTagByLocation(areaName, location) != null;
    }

    private static @Nullable String getTagByLocation(String areaName, Location location) {
        for (int j = 0; j < JsonFileManager.getTagAmount(areaName); j++) {
            if (JsonFileManager.getLocation(areaName, String.valueOf(j)).equals(location)) {
                return String.valueOf(j);
            }
        }
        return null;
    }

    public static JsonObject getMainJsonObject(String areaName) {
        File file = new File(OreRegen.getPlugin().getDataFolder() + "/data", areaName + ".json");
        JsonObject main;

        try (FileReader fr = new FileReader(file)) {
            main = JsonParser.parseReader(fr).getAsJsonObject();
        } catch (IOException | IllegalStateException e) {
            main = new JsonObject();
        }
        return main;
    }
}
