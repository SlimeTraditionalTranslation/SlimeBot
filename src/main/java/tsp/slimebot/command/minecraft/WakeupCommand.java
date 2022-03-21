package tsp.slimebot.command.minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.SlimeBot;

import javax.security.auth.login.LoginException;

public class WakeupCommand extends MinecraftCommand {

    public WakeupCommand() {
        super("wakeup");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            return false; // Hidden from non-console
        }

        if (SlimeBot.getInstance().getJDA() != null) {
            sender.sendMessage("機器人已正在運行.");
            return true;
        }

        try {
            SlimeBot.getInstance().startBot();
        } catch (LoginException e) {
            sender.sendMessage("無法啟動機器人.");
            e.printStackTrace();
        }
        return true;
    }

}
