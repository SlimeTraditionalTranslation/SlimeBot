package tsp.slimebot.command.minecraft;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import tsp.slimebot.SlimeBot;
import tsp.slimebot.util.BuildProperties;
import tsp.slimebot.util.Utils;

public class SlimeBotCommand extends MinecraftCommand {

    private final BuildProperties build = SlimeBot.getInstance().getBuild();

    public SlimeBotCommand() {
        super("slimebot");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        TextComponent component = new TextComponent();
        component.setText(colorize("&7Running &aSlimeBot - ") + build.getVersion());
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/TheSilentPro/SlimeBot"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText(
                "Created by Silent." +
                "\nClick to visit the GitHub page.")
        ));

        sender.spigot().sendMessage(component);
        Utils.sendMessage(sender, "&7Build &a" + build.getNumber() + " &7compiled by &a" + build.getAuthor());
        Utils.sendMessage(sender, "&7Compiled on &a" + build.getCompiled());
    }

    private String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
