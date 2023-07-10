package tsp.slimebot.listener.minecraft;

import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.event.EventHandler;
import org.mini2Dx.gettext.GetText;
import tsp.slimebot.SlimeBot;
import tsp.slimebot.util.Log;
import tsp.slimebot.util.Utils;

import java.time.Instant;

// TODO: Make this interval based such that it shows multiple researches in one message
public class ResearchUnlockListener extends MinecraftListener {

    @EventHandler
    public void onResearchUnlock(ResearchUnlockEvent event) {
        JDA jda = SlimeBot.getInstance().getJDA();
        if (jda == null) {
            return;
        }

        String id = SlimeBot.getInstance().getConfig().getString("announcements.researchUnlock");
        if (id == null || id.isEmpty()) {
            return;
        }

        TextChannel channel = jda.getTextChannelById(id);
        if (channel == null) {
            Log.warning(GetText.tr("Invalid channel id: ") + id);
            return;
        }

        channel.sendMessageEmbeds(new EmbedBuilder()
                .setAuthor("\uD83D\uDCD6" + GetText.tr(" {0} unlocked a research!", event.getPlayer().getName()))
                .appendDescription(Utils.research(event.getResearch()))
                .setTimestamp(Instant.now())
                .build()).queue();
    }

}
