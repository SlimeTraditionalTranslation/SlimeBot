package tsp.slimebot.listener.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.SlimeBot;
import tsp.slimebot.command.discord.CommandManager;
import tsp.slimebot.command.discord.SlimeCommand;
import tsp.slimebot.util.Utils;

public class DiscordCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandManager manager = SlimeBot.getInstance().getCommandManager();
        SlimeCommand command = manager.getCommand(event.getName());
        if (command == null) {
            return;
        }

        if (event.getMember() != null) {
            if (!event.getMember().hasPermission(command.getPermission())) {
                event.replyEmbeds(Utils.embed(event)
                                .setAuthor("沒有權限!")
                                .appendDescription("這個指令需要 " + Utils.wrap(command.getPermission().getName()))
                                .build())
                        .setEphemeral(true)
                        .queue();
                return;
            }
        }

        event.deferReply().queue(); // Some stuff may take >3 seconds, defer the reply for later
        command.onCommand(event);
    }

}