package cn.saltedfish.oreregen.Commands;

import cn.saltedfish.oreregen.OreRegen;
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

public class OreRegenCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        switch (args[0]){
            case "reload": {
                OreRegen.isReload = true;
                Bukkit.getPluginManager().disablePlugin(OreRegen.getPlugin());
                Bukkit.getPluginManager().enablePlugin(OreRegen.getPlugin());
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    player.sendMessage("Reload complete");
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1){
            return Arrays.asList("reload");
        }
        return new ArrayList<>();
    }
}
