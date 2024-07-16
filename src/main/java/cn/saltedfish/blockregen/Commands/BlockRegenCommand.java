package cn.saltedfish.blockregen.Commands;

import cn.saltedfish.blockregen.BlockRegen;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockRegenCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        switch (args[0]){
            case "reload": {
                BlockRegen.isReload = true;
                Bukkit.getPluginManager().disablePlugin(BlockRegen.getPlugin());
                Bukkit.getPluginManager().enablePlugin(BlockRegen.getPlugin());
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    player.sendMessage("Reload complete");
                }
            }
            case "area": {

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
        if (args.length == 2){
            switch (args[0]){
                case "area": {
                    return null;
                }
            }
        }
        return new ArrayList<>();
    }
}
