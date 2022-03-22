package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.Bukkit;
import tsp.slimebot.util.Utils;

import java.util.Set;

public class PlayerCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "player";
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        String name = event.getOption("name").getAsString();

        PlayerProfile.get(Bukkit.getOfflinePlayer(name), profile -> {
            String online = profile.getPlayer() != null ? ":green_circle:" : ":red_circle:";
            Set<Research> researches = profile.getResearches();
            int allResearches = Slimefun.getRegistry().getResearches().size();
            float progress = Math.round(((researches.size() * 100.0F) / allResearches) * 100.0F) / 100.0F;
            HashedArmorpiece[] armor = profile.getArmor();
            String head = armor[0].getItem().isPresent() ? armor[0].getItem().get().getItemName() : "無";
            String torso = armor[1].getItem().isPresent() ? armor[1].getItem().get().getItemName() : "無";
            String legs = armor[2].getItem().isPresent() ? armor[2].getItem().get().getItemName() : "無";
            String boots = armor[3].getItem().isPresent() ? armor[3].getItem().get().getItemName() : "無";

            event.getHook().editOriginalEmbeds(Utils.embed(event)
                    .appendDescription(online + name + " (" + profile.getUUID().toString()  + ")")
                    .appendDescription("\n稱號: " + Utils.wrap(profile.getTitle()))
                    .appendDescription("\n研究進度: " + Utils.wrap(progress + "% " + '(' + researches.size() + " / " + allResearches + ')'))
                    .appendDescription("\n總花費經驗等級: " + Utils.wrap(researches.stream().mapToInt(Research::getCost).sum()))
                            .appendDescription("\n")
                            .appendDescription("\n**裝備**")
                            .appendDescription("\n" + head)
                            .appendDescription("\n" + torso)
                            .appendDescription("\n" + legs)
                            .appendDescription("\n" + boots)
                    .build()).queue();
        });
    }

}
