package tsp.slimebot.command.discord;

import tsp.slimebot.SlimeBot;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

    private final Map<String, SlimeCommand> commands = new HashMap<>();
    private final List<String> disabled = new ArrayList<>();

    public void register(SlimeCommand command) {
        commands.put(command.getName(), command);
    }

    /**
     * Registers the command only if not disabled by config
     *
     * @param command The command to register
     */
    public void registerConfigurable(SlimeCommand command) {
        if (disabled.isEmpty()) {
            disabled.addAll(SlimeBot.getInstance().getConfig().getStringList("disabledCommands"));
        }

        if (!disabled.contains(command.getName())) {
            register(command);
        }
    }

    public void registerDefaults() {
        registerConfigurable(new IDCommand());

        registerConfigurable(new PlayerCommand());
        registerConfigurable(new AddonsCommand());
        registerConfigurable(new GroupCommand());
        registerConfigurable(new GroupListCommand());
        registerConfigurable(new ItemCommand());
        registerConfigurable(new ItemListCommand());
        registerConfigurable(new RecipeCommand());
        registerConfigurable(new ResearchCommand());
        registerConfigurable(new ResearchListCommand());
    }

    @Nullable
    public SlimeCommand getCommand(String name) {
        return commands.get(name);
    }

    public Map<String, SlimeCommand> getCommands() {
        return commands;
    }

}
