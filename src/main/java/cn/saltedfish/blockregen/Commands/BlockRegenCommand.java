package cn.saltedfish.blockregen.Commands;

import cn.saltedfish.blockregen.AreaManager;
import cn.saltedfish.blockregen.BlockManager;
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
                        //blockregen area createArea x1 y1 z1 x2 y2 z2 areaName
                        if (args.length != 9) {
                            message = BlockRegen.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        double x1 = Double.parseDouble(args[2]);
                        double y1 = Double.parseDouble(args[3]);
                        double z1 = Double.parseDouble(args[4]);
                        double x2 = Double.parseDouble(args[5]);
                        double y2 = Double.parseDouble(args[6]);
                        double z2 = Double.parseDouble(args[7]);
                        String areaName = args[8];

                        if (AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegen.getLanguage("Area.AreaAlreadyExists").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        World world = player.getWorld();
                        Location loc1 = new Location(world, x1, y1, z1);
                        Location loc2 = new Location(world, x2, y2, z2);

                        AreaManager.setArea(areaName, world, loc1, loc2);

                        message = BlockRegen.getLanguage("Area.Create").replace("%AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "remove": {
                        //blockregen area remove areaName
                        if (args.length != 3) {
                            message = BlockRegen.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegen.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        AreaManager.removeArea(areaName);
                        message = BlockRegen.getLanguage("Area.Remove").replace("%AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "detect": {
                        //blockregen area detect areaName
                        if (args.length != 3) {
                            message = BlockRegen.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegen.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        AreaManager.writeInformation(areaName);
                        message = BlockRegen.getLanguage("Area.Detect").replace("%AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "addBlock": {
                        //blockregen area addBlock areaName blockName
                        if (args.length != 4) {
                            message = BlockRegen.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];
                        String blockName = args[3];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegen.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        if (!BlockManager.getBlockList().contains(blockName)) {
                            message = BlockRegen.getLanguage("Area.BlockNotFoundInServer").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        AreaManager.addRegenBlock(areaName, blockName);
                        message = BlockRegen.getLanguage("Area.AddBlock").replace("%BlockName", blockName).replace("%AreaName", blockName);
                        player.sendMessage(message);
                        break;
                    }
                    case "removeBlock": {
                        //blockregen area removeBlock areaName blockName
                        if (args.length != 4) {
                            message = BlockRegen.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];
                        String blockName = args[3];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegen.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        if (!AreaManager.getAreaRegenBlockList(areaName).contains(blockName)) {
                            message = BlockRegen.getLanguage("Area.BlockNotFoundInArea").replaceAll("%BlockName", blockName);
                            player.sendMessage(message);
                            return true;
                        }

                        AreaManager.removeRegenBlock(areaName, blockName);
                        message = BlockRegen.getLanguage("Area.RemoveBlock").replace("%BlockName", blockName).replace("%AreaName", areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "info": {
                        //blockregen area info areaName
                        if (args.length != 3) {
                            message = BlockRegen.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        String areaName = args[2];

                        if (!AreaManager.getRegenAreaList().contains(areaName)) {
                            message = BlockRegen.getLanguage("Area.AreaNotFound").replaceAll("%AreaName", areaName);
                            player.sendMessage(message);
                            return true;
                        }

                        message = AreaManager.getAreaInfo(areaName);
                        player.sendMessage(message);
                        break;
                    }
                    case "list": {
                        //blockregen area list
                        if (args.length != 2) {
                            message = BlockRegen.getLanguage("InvalidCommand");
                            player.sendMessage(message);
                            return true;
                        }

                        List<String> areaList = AreaManager.getRegenAreaList();
                        message = BlockRegen.getLanguage("Area.List").replaceAll("%AreaList", String.valueOf(areaList));
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
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1){
            return Arrays.asList("reload", "area");
        }
        if ("area".equals(args[0])) {

            if (args.length == 2) {
                return Arrays.asList("create", "remove", "detect", "addBlock", "removeBlock", "info", "list");
            }
            if ("create".equals(args[1])) {
                //blockregen area create x1 y1 z1 x2 y2 z2 areaName
                Player player;
                double x = 0;
                double y = 0;
                double z = 0;
                if (commandSender instanceof Player) {
                    player = (Player) commandSender;
                    x = player.getLocation().getX();
                    y = player.getLocation().getY();
                    z = player.getLocation().getZ();
                }

                if (args.length == 3) {
                    return Arrays.asList(String.valueOf(x), String.valueOf(y), String.valueOf(z));
                }
                if (args.length == 4) {
                    return Arrays.asList(String.valueOf(y), String.valueOf(z));
                }
                if (args.length == 5) {
                    return Collections.singletonList(String.valueOf(z));
                }
                if (args.length == 6) {
                    return Arrays.asList(String.valueOf(x), String.valueOf(y), String.valueOf(z));
                }
                if (args.length == 7) {
                    return Arrays.asList(String.valueOf(y), String.valueOf(z));
                }
                if (args.length == 8) {
                    return Collections.singletonList(String.valueOf(z));
                }
                if (args.length == 9) {
                    return new ArrayList<>();
                }
            }
            if ("remove".equals(args[1])) {
                //blockregen area remove areaName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
            }
            if ("detect".equals(args[1])) {
                //blockregen area detect areaName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
            }
            if ("addBlock".equals(args[1])) {
                //blockregen area addBlock areaName blockName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
                if (args.length == 4) {
                    return BlockManager.getBlockList();
                }
            }
            if ("removeBlock".equals(args[1])) {
                //blockregen area removeBlock areaName blockName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
                if (args.length == 4) {
                    return AreaManager.getAreaRegenBlockList(args[2]);
                }
            }
            if ("info".equals(args[1])) {
                //blockregen area info areaName
                if (args.length == 3) {
                    return AreaManager.getRegenAreaList();
                }
            }
        }
        return new ArrayList<>();
    }
}
