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
        component.setText(colorize("&7正在運行&a黏液機器人 - ") + build.getVersion());
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/SlimeTraditionalTranslation/SlimeBot"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText(
                "由 Silent 製作." +
                "\n點擊查看.")
        ));

        sender.spigot().sendMessage(component);
        Utils.sendMessage(sender, "&7建構 &a" + build.getNumber() + " &7由 &a" + build.getAuthor() + " &7進行編譯");
        Utils.sendMessage(sender, "&7編譯於 &a" + build.getCompiled());
    }

    private String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
