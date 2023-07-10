package tsp.slimebot.command.minecraft;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.mini2Dx.gettext.GetText;
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
        component.setText(Utils.colorize(GetText.tr("&7Running &aSlimeBot - ") + build.getVersion() + GetText.tr(" &7(&aBuild {0}&7)", build.getNumber())));
        component.addExtra(Utils.colorize("\n" + GetText.tr("&7Created by &a{0}", SlimeBot.getInstance().getDescription().getAuthors())));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/" + build.getAuthor() + "/SlimeBot"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText(
                Utils.colorize(GetText.tr("&7Click to visit the GitHub page.")))
        ));

        sender.spigot().sendMessage(component);
        Utils.sendMessage(sender, GetText.tr("&7Compiled by &a{0} &7on &a{1}", build.getAuthor(), build.getCompiled()));
    }

}
