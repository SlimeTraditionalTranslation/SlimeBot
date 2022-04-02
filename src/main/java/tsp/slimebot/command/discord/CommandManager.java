package tsp.slimebot.command.discord;

import tsp.slimebot.SlimeBot;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Manager for {@link SlimeCommand commands}.
 */
public class CommandManager {

    private final Map<String, SlimeCommand> commands = new HashMap<>();
    private boolean logCommands = false;
    private List<String> disabled = null;
    private List<String> ephemeral = null;

    /**
     * Constructs a new manager.
     *
     * @param defaults Whether default commands should be registered
     */
    public CommandManager(boolean defaults) {
        if (defaults) {
            registerDefaults();
        }
    }

    public CommandManager() {
        this(false);
    }

    /**
     * Registers a command.
     * The name MUST be unique!
     *
     * @param command The command to register
     */
    public void register(SlimeCommand command) {
        commands.put(command.getName(), command);
    }

    /**
     * Registers a configurable command.
     *
     * @param command The command to register
     */
    private void registerConfigurable(SlimeCommand command) {
        SlimeBot instance = SlimeBot.getInstance();

        // Check if disabled list exists
        if (disabled == null) {
            disabled = instance.getConfig().getStringList("disabledCommands");
        }

        // Check if command is disabled by config
        if (!disabled.contains(command.getName())) {
            // Check if ephemeral list exists
            if (ephemeral == null) {
                ephemeral = instance.getConfig().getStringList("ephemeral");
            }

            // Check if command should be ephemeral by config
            if (ephemeral.contains(command.getName())) {
                command.setEphemeral(true);
            }

            // Set requires roles

            register(command);
        }
    }

    /**
     * Registers the default commands.
     */
    private void registerDefaults() {
        logCommands = SlimeBot.getInstance().getConfig().getBoolean("logCommands");

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

    /**
     * Whether the command manager logs executions.
     *
     * @return If the command manager logs executions
     */
    public boolean shouldLogCommands() {
        return logCommands;
    }

    /**
     * List of ephemeral commands.
     *
     * @return List of ephemeral commands
     */
    public List<String> getEphemeral() {
        return ephemeral;
    }

    /**
     * List of disabled commands.
     *
     * @return List of disabled commands
     */
    public List<String> getDisabled() {
        return disabled;
    }

    /**
     * Retrieve a {@link SlimeCommand} from the registry.
     *
     * @param name The name of the command
     * @return An optional command
     */
    public Optional<SlimeCommand> getCommand(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    /**
     * Retrieve a list of registered {@link SlimeCommand commands}.
     *
     * @return An unmodifiable list of commands
     */
    @Nonnull
    public Map<String, SlimeCommand> getCommands() {
        return Collections.unmodifiableMap(commands);
    }

}
