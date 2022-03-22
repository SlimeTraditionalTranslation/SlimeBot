package tsp.slimebot.listener.minecraft;

import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.EventHandler;
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
            Log.warning("未知的頻道ID: " + id);
            return;
        }

        channel.sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(":book:" + event.getPlayer().getName() + " 解鎖了一項研究!")
                .appendDescription(Utils.research(event.getResearch()))
                .setTimestamp(Instant.now())
                .build()).queue();
    }

}
