package tsp.slimebot.listener.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.SlimeBot;

public class BotReadyListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(SlimeBot.getInstance().getCfg().getString("bot.guildId"));
        if (guild == null) {
            SlimeBot.getInstance().getLogger().severe("未知 Discord 伺服器ID!");
            SlimeBot.getInstance().stopBot();
            return;
        }

        guild.upsertCommand("getid", "檢索ID.")
                .addOption(OptionType.CHANNEL, "channel", "檢索文字頻道ID.")
                .queue();
        if (SlimeBot.getInstance().getCfg().getBoolean("debug")) {
            guild.upsertCommand("shutdown", "關閉機器人.")
                    .queue();
        }

        guild.upsertCommand("player", "檢索有關玩家的資訊.")
                .addOption(OptionType.STRING, "name", "玩家名稱", true)
                .queue();
        guild.upsertCommand("group", "檢索有關組別的資訊 (類別).")
                .addOption(OptionType.STRING, "name", "組別名稱", true)
                .queue();
        guild.upsertCommand("groups", "列出所有組別.")
                .addOption(OptionType.INTEGER, "page", "頁面")
                .queue();
        guild.upsertCommand("item", "檢索有關物品的詳細資訊.")
                .addOption(OptionType.STRING, "name", "物品名稱", true)
                .queue();
        guild.upsertCommand("items", "列出所有物品")
                .addOption(OptionType.STRING, "type", "ALL|ENABLED|DISABLED|RADIOACTIVE|a:ADDON|r:RESEARCH")
                .addOption(OptionType.INTEGER, "page", "頁面")
                .queue();
        guild.upsertCommand("recipe", "檢索有關配方的資訊")
                .addOption(OptionType.STRING, "name", "物品名稱")
                .queue();
        guild.upsertCommand("research", "檢索有關研究的資訊")
                .addOption(OptionType.STRING, "name", "研究鍵")
                .queue();
        guild.upsertCommand("researches", "列出所有的研究.")
                .addOption(OptionType.INTEGER, "page", "頁面")
                .queue();
    }
}
