package tsp.slimebot.listener.discord;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.mini2Dx.gettext.GetText;
import tsp.slimebot.SlimeBot;
import tsp.slimebot.command.discord.CommandManager;
import tsp.slimebot.command.discord.SlimeCommand;
import tsp.slimebot.util.Log;
import tsp.slimebot.util.Utils;

import java.util.List;
import java.util.Optional;

public class DiscordCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandManager manager = SlimeBot.getInstance().getCommandManager();
        Optional<SlimeCommand> commandTrace = manager.getCommand(event.getName());
        if (!commandTrace.isPresent()) {
            return;
        }
        SlimeCommand command = commandTrace.get();

        if (event.getMember() != null) {
            if (!hasPermission(event.getMember(), command.getRequiredRoles())) {
                event.replyEmbeds(Utils.embed(event)
                                .setAuthor(GetText.tr("No permission!"))
                                .appendDescription(GetText.tr("You do not have permission to run this command!"))
                                .build())
                        .setEphemeral(true) // No permission messages are always ephemeral
                        .queue();
                return;
            }
        }

        event.deferReply().queue(); // Some stuff may take >3 seconds, defer the reply for later
        event.getHook().setEphemeral(command.isEphemeral()); // If the command should be ephemeral
        command.handle(event);

        if (manager.shouldLogCommands()) {
            User user = event.getUser();
            Log.info(GetText.tr("{0} ({1}) executed command: {2}", user.getName(), user.getId(), command.getName()));
        }
    }

    private boolean hasPermission(Member member, List<Role> roles) {
        if (roles.isEmpty()) {
            return true;
        }

        for (Role role : roles) {
            if (member.getRoles().contains(role)) {
                return true;
            }
        }

        return false;
    }

}