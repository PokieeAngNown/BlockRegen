package cn.saltedfish.blockregen.Commands;

import cn.saltedfish.blockregen.AreaManager;
import cn.saltedfish.blockregen.BlockRegen;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockRegenCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        String message;
        switch (args[0]){
            case "reload": {
                Bukkit.getPluginManager().disablePlugin(BlockRegen.getPlugin());
                Bukkit.getPluginManager().enablePlugin(BlockRegen.getPlugin());
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    message = BlockRegen.getLanguage("Reload");
                    player.sendMessage(message);
                }
            }
            break;
            case "area": {
                Player player;
                if (!(commandSender instanceof Player)) {
                    return true;
                }
                player = (Player) commandSender;
                switch (args[1]) {
                    case "create": {
                        //blockregen area create x1 y1 z1 x2 y2 z2 areaName
                        double x1 = Double.parseDouble(args[2]);
                        double y1 = Double.parseDouble(args[3]);
                        double z1 = Double.parseDouble(args[4]);
                        double x2 = Double.parseDouble(args[5]);
                        double y2 = Double.parseDouble(args[6]);
                        double z2 = Double.parseDouble(args[7]);
                        String areaName = args[8];

                        World world = player.getWorld();
                        Location loc1 = new Location(world, x1, y1, z1);
                        Location loc2 = new Location(world, x2, y2, z2);

                        AreaManager.setArea(areaName, world, loc1, loc2);

                        message = BlockRegen.getLanguage("Area.Create").replace("$AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "detect": {
                        //blockregen area detect areaName
                        String areaName = args[2];
                        AreaManager.writeInformation(areaName);
                        message = BlockRegen.getLanguage("Area.Detect").replace("$AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    default: {
                        message = BlockRegen.getLanguage("InvalidCommand");
                        player.sendMessage(message);
                        break;
                    }
                }
                break;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1){
            return Arrays.asList("reload", "area");
        }
        if ("area".equals(args[0])) {

            if (args.length == 2) {
                return Arrays.asList("create", "detect");
            }
            if ("create".equals(args[1])) {
                //blockregen area create x1 y1 z1 x2 y2 z2 areaName
                if (args.length == 3) {
                    return Arrays.asList("~", "~ ~", "~ ~ ~");
                }
                if (args.length == 4) {
                    return Arrays.asList("~", "~ ~");
                }
                if (args.length == 5) {
                    return Collections.singletonList("~");
                }
                if (args.length == 6) {
                    return Arrays.asList("~", "~ ~", "~ ~ ~");
                }
                if (args.length == 7) {
                    return Arrays.asList("~", "~ ~");
                }
                if (args.length == 8) {
                    return Collections.singletonList("~");
                }
                if (args.length == 9) {
                    return new ArrayList<>();
                }
            }
            if ("detect".equals(args[1])) {
                //blockregen area detect areaName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
            }
        }
        return new ArrayList<>();
    }
}
