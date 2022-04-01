package tsp.slimebot.command.discord;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.lang.Validate;
import tsp.slimebot.SlimeBot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a discord command
 */
public class SlimeCommand {

    private final SlimeBot instance = SlimeBot.getInstance();
    private String name;
    private CommandEvent event;
    private boolean ephemeral;
    private List<Role> requiredRoles = null;

    public SlimeCommand(String name) {
        this.name = name;
    }

    public void handle(SlashCommandInteractionEvent event) {
        this.event.onCommand(event);
    }

    // Getters
    public String getName() {
        return name;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRequiredRoles() {
        if (requiredRoles == null) {
            String guildId = instance.getConfig().getString("bot.guildId");
            List<String> rawRoles = instance.getConfig().getStringList("roles." + name);
            if (rawRoles.isEmpty()) {
                return requiredRoles = new ArrayList<>();
            }

            requiredRoles = rawRoles
                    .stream().map(raw -> instance.getJDA().getGuildById(guildId).getRoleById(raw))
                    .collect(Collectors.toList());
        }

        return requiredRoles;
    }

    public void setRequiredRoles(List<Role> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    public void setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    public void setEvent(CommandEvent event) {
        this.event = event;
    }

    // Command Builder
    public static class Builder {

        private String name;
        private CommandEvent event;
        private boolean ephemeral;
        private List<Role> requiredRoles = null;

        public Builder(String name) {
            this.name = name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder event(CommandEvent event) {
            this.event = event;
            return this;
        }

        public Builder ephemeral(boolean b) {
            this.ephemeral = b;
            return this;
        }

        public Builder requiredRoles(List<Role> roles) {
            this.requiredRoles = roles;
            return this;
        }

        public SlimeCommand build() {
            Validate.notNull(name, "Name can not be null!");
            Validate.notNull(event, "Event can not be null!");

            SlimeCommand command = new SlimeCommand(name);
            command.setEvent(event);
            command.setEphemeral(ephemeral);
            command.setRequiredRoles(requiredRoles);
            return command;
        }

    }
    
}
