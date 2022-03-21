package tsp.slimebot.command.discord;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import tsp.slimebot.SlimeBot;
import tsp.slimebot.util.Utils;

/**
 * This command will only shutdown the bot, not the plugin.
 */
public class ShutdownCommand implements SlimeCommand {

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        event.getHook().editOriginalEmbeds(Utils.embed(event)
                .setAuthor("JDA 已關閉.")
                .appendDescription("JDA 已被關閉.")
                .build()).complete();

        SlimeBot.getInstance().stopBot();
    }

}
