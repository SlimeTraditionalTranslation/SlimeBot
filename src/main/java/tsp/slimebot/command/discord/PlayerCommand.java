package tsp.slimebot.command.discord;

import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.Bukkit;
import tsp.slimebot.util.Utils;

import java.util.Set;

public class PlayerCommand extends SlimeCommand {

    public PlayerCommand() {
      super("player");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String name = event.getOption("name").getAsString();

        PlayerProfile.get(Bukkit.getOfflinePlayer(name), profile -> {
            String online = profile.getPlayer() != null ? ":green_circle:" : ":red_circle:";
            Set<Research> researches = profile.getResearches();
            int allResearches = Slimefun.getRegistry().getResearches().size();
            float progress = Math.round(((researches.size() * 100.0F) / allResearches) * 100.0F) / 100.0F;
            HashedArmorpiece[] armor = profile.getArmor();
            String head = armor[0].getItem().isPresent() ? armor[0].getItem().get().getItemName() : "None";
            String torso = armor[1].getItem().isPresent() ? armor[1].getItem().get().getItemName() : "None";
            String legs = armor[2].getItem().isPresent() ? armor[2].getItem().get().getItemName() : "None";
            String boots = armor[3].getItem().isPresent() ? armor[3].getItem().get().getItemName() : "None";

            event.getHook().editOriginalEmbeds(Utils.embed(event)
                    .appendDescription(online + name + " (" + profile.getUUID().toString()  + ")")
                    .appendDescription("\nTitle: " + Utils.wrap(profile.getTitle()))
                    .appendDescription("\nResearch Progress: " + Utils.wrap(progress + "% " + '(' + researches.size() + " / " + allResearches + ')'))
                    .appendDescription("\nTotal XP Levels spent: " + Utils.wrap(researches.stream().mapToInt(Research::getCost).sum()))
                            .appendDescription("\n")
                            .appendDescription("\n**Armor**")
                            .appendDescription("\n" + head)
                            .appendDescription("\n" + torso)
                            .appendDescription("\n" + legs)
                            .appendDescription("\n" + boots)
                    .build()).queue();
        });
    }

}
