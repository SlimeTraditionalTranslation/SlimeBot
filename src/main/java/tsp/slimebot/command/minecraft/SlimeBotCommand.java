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
        component.setText(Utils.colorize("&7正在運行&a黏液機器人 - " + build.getVersion() + " &7(&a建構 " + build.getNumber() + "&7)"));
        component.addExtra(Utils.colorize("\n&7由 &a" + SlimeBot.getInstance().getDescription().getAuthors() + " &7製作"));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/" + build.getAuthor() + "/SlimeBot"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText(
                Utils.colorize("&7點擊查看 GitHub 頁面."))
        ));

        sender.spigot().sendMessage(component);
        Utils.sendMessage(sender, "&7建構由 &a" + build.getAuthor() + " &7進行編譯 &a" + build.getCompiled());
    }

}
