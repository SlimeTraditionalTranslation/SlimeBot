package tsp.slimebot.listener.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.mini2Dx.gettext.GetText;
import tsp.slimebot.SlimeBot;
import tsp.slimebot.util.Log;

public class BotReadyListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(SlimeBot.getInstance().getConfig().getString("bot.guildId"));
        if (guild == null) {
            Log.error(GetText.tr("Invalid guild id!"));
            SlimeBot.getInstance().getBot().stopBot();
            return;
        }

        guild.upsertCommand("getid", GetText.tr("Retrieve an id."))
                .addOption(OptionType.CHANNEL, "channel", GetText.tr("Retrieve id of the text channel."))
                .queue();

        guild.upsertCommand("player", GetText.tr("Retrieve information about a player."))
                .addOption(OptionType.STRING, "name", GetText.tr("Player name"), true)
                .queue();
        guild.upsertCommand("group", GetText.tr("Retrieve information about a group (category)."))
                .addOption(OptionType.STRING, "name", GetText.tr("Group name"), true)
                .queue();
        guild.upsertCommand("groups", GetText.tr("List all groups."))
                .addOption(OptionType.INTEGER, "page", GetText.tr("Page"))
                .queue();
        guild.upsertCommand("item", GetText.tr("Retrieve detailed information about an item."))
                .addOption(OptionType.STRING, "name", GetText.tr("Item name"), true)
                .queue();
        guild.upsertCommand("items", GetText.tr("List all items"))
                .addOption(OptionType.STRING, "type", "ALL|ENABLED|DISABLED|RADIOACTIVE|a:ADDON|r:RESEARCH")
                .addOption(OptionType.INTEGER, "page", GetText.tr("Page"))
                .queue();
        guild.upsertCommand("recipe", GetText.tr("Retrieve information about a recipe"))
                .addOption(OptionType.STRING, "name", GetText.tr("Item name"), true)
                .queue();
        guild.upsertCommand("research", GetText.tr("Retrieve information about a research"))
                .addOption(OptionType.STRING, "name", GetText.tr("Research key"), true)
                .queue();
        guild.upsertCommand("researches", GetText.tr("List all researches."))
                .addOption(OptionType.INTEGER, "page", GetText.tr("Page"))
                .queue();
        guild.upsertCommand("addons", GetText.tr("List all installed addons."))
                .addOption(OptionType.INTEGER, "page", GetText.tr("Page"))
                .addOption(OptionType.BOOLEAN, "detailed", GetText.tr("Detailed list"))
                .queue();
    }
}
