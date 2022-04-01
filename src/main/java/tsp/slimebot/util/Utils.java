package tsp.slimebot.util;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public final class Utils {

    //public static final int CHARACTER_LIMIT = 4096;
    private static final List<SlimefunItem> items = new ArrayList<>();

    private Utils() {
        // tf u doing?
    }

    public static List<SlimefunItem> getItems() {
        if (items.isEmpty()) {
            items.addAll(Slimefun.getRegistry().getAllSlimefunItems());
        }

        return items;
    }

    // TODO: Better/Cleaner way to do this
    public static String asString(List<?> list) {
        StringBuilder builder = new StringBuilder();
        if (list == null || list.isEmpty()) {
            return "";
        }

        if (list.get(0) instanceof Research) {
            for (Object entry : list) {
                Research research = (Research) entry;
                builder.append("**" + research.getKey().getKey().toUpperCase() + "**")
                        .append("\n > Namespace: " + wrap(research.getKey().getNamespace()))
                        .append("\n > Items included: " + research.getAffectedItems().size())
                        .append("\n\n");
            }
        } else if (list.get(0) instanceof ItemGroup) {
            for (Object entry : list) {
                ItemGroup group = (ItemGroup) entry;
                builder.append("**" + group.getUnlocalizedName() + "**")
                        .append("\n > Tier: " + group.getTier())
                        .append("\n > Items included: " + group.getItems().size())
                        .append("\n\n");
            }
        } else if (list.get(0) instanceof SlimefunItem) {
            for (Object entry : list) {
                SlimefunItem item = (SlimefunItem) entry;
                builder.append(Utils.wrap(stripColor(item.getItemName()))).append(",");
            }
        }

        return builder.toString();
    }

    public static String description(SlimefunItem item) {
        List<String> lore = item.getItem().getItemMeta().hasLore() ? item.getItem().getItemMeta().getLore() : Collections.singletonList("None");
        StringBuilder description = new StringBuilder();
        for (String line : lore) {
            description.append(stripColor(line)).append("\n");
        }

        return description.toString();
    }

    public static String information(SlimefunItem item) {
        String wiki = item.getWikipage().isPresent() ? item.getWikipage().get() : "None" + "\n";
        return " > ID: " + wrap(item.getId()) + "\n" +
                " > Addon: " + wrap(item.getAddon().getName()) + "\n" +
                " > State: " + wrap(item.getState().name()) + "\n" +
                " > Wiki: " + wiki + "\n";
    }

    public static String category(ItemGroup group) {
        String origin = group.getAddon() != null ? group.getAddon().getName() : "None" + "\n";
        return " > ID: " + wrap(group.getKey().toString()) + "\n" +
                " > Name: " + wrap(group.getUnlocalizedName()) + "\n" +
                " > Tier: " + wrap(group.getTier()) + "\n" +
                " > Addon: " + wrap(origin) + "\n";
    }

    public static String research(Research research) {
        String result = "None" + "\n";
        if (research != null) {
            List<SlimefunItem> researchItems = research.getAffectedItems();
            result = " > ID: " + wrap(research.getUnlocalizedName()) + "\n" +
                    " > Cost: " + wrap(research.getCost()) + "\n" +
                    " > Items included: " + researchItems.size() + "\n";
        }

        return result;
    }

    public static String recipe(SlimefunItem item) {
        SlimefunItem machine = item.getRecipeType().getMachine();
        String name = machine != null ? machine.getItemName() : "Unknown";

        return " > Machine ID: " + wrap(item.getRecipeType().getKey().toString()) + "\n" +
                " > Machine: " + wrap(stripColor(name)) + "\n" +
                " > Output (" + item.getRecipeOutput().getAmount() + "): " + wrap(Utils.getName(item.getRecipeOutput()));
    }

    public static String recipeGrid(SlimefunItem item) {
        return recipeGrid(item.getRecipe());
    }

    public static String recipeGrid(ItemStack[] recipe) {
        Map<ItemStack, Character> mapper = new HashMap<>();

        // Maps each ItemStack to it's corresponding character
        char[] keys = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
        int last = 0;
        for (ItemStack entry : recipe) {
            if (entry == null || entry.getType() == Material.AIR) {
                continue;
            }

            if (!mapper.containsKey(entry)) {
                mapper.put(entry, keys[last]);
                last++;
            }
        }

        // Build recipe grid
        StringBuilder builder = new StringBuilder();
        builder.append("``` | ");
        for (int i = 1; i <= 9; i++) {
            ItemStack recipeItem = recipe[i - 1];
            if (recipeItem == null) {
                builder.append(" ");
            } else {
                builder.append(mapper.get(recipeItem));
            }
            builder.append(" | ");

            if (i % 3 == 0) {
                builder.append("\n");
                if (i != 9) {
                    builder.append(" | ");
                }
            }
        }

        // Build keys legend
        builder.append("```\n");
        for (Map.Entry<ItemStack, Character> entry : mapper.entrySet()) {
            builder.append(entry.getValue())
                    .append(" = ")
                    .append(wrap(getName(entry.getKey())))
                    .append("\n");
        }

        return builder.toString();
    }

    public static String vanillaRecipeGrids(List<Recipe> recipes) {
        StringBuilder builder = new StringBuilder();
        for (Recipe recipe : recipes) {
            if(recipe instanceof ShapedRecipe) {
                ShapedRecipe shaped = (ShapedRecipe)recipe;
                builder.append("**Crafting (Shaped**)");
                builder.append("*Unimplemented by the bot*"); // TODO: finish
            } else if(recipe instanceof ShapelessRecipe) {
                ShapelessRecipe shapeless = (ShapelessRecipe)recipe;
                builder.append("**Crafting (Shapeless)**");
                builder.append(recipeGrid(shapeless.getIngredientList().toArray(new ItemStack[8])));
            } else if(recipe instanceof FurnaceRecipe) {
                FurnaceRecipe furnace = (FurnaceRecipe)recipe;
                builder.append("**Furnace**");
                builder.append(" > Input: " + furnace.getInput());
                builder.append(" > Result: " + furnace.getResult().getItemMeta().getDisplayName());
            }

            builder.append("\n");
        }

        return builder.toString();
    }

    public static String getName(ItemStack item) {
        if (item == null) return "null";

        String name = item.getType().name();
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
                name = stripColor(item.getItemMeta().getDisplayName());
            }
        }

        return name;
    }

    public static boolean matches(String raw, SlimefunItem provided) {
        String name = ChatColor.stripColor(provided.getItemName().toLowerCase());
        raw = raw.toLowerCase();
        return raw.equalsIgnoreCase(provided.getId())
                || raw.equalsIgnoreCase(name)
                || raw.startsWith(name)
                || raw.replace("_", " ").startsWith(name) // Example: "enhanced crafting"
                || name.contains(raw.replace("_", " ")); // Example: "crafting table" (Enhanced Crafting Table)
    }

    public static boolean matches(String raw, Research provided) {
        String name = provided.getUnlocalizedName().toLowerCase();
        raw = raw.toLowerCase();
        return provided.getKey().getKey().equalsIgnoreCase(raw)
                || name.equalsIgnoreCase(raw)
                || name.startsWith(raw)
                || raw.replace("_", " ").startsWith(name)
                || name.contains(raw.replace("_", " "));
    }

    public static String wrap(String string) {
        return "`" + string + "`";
    }

    public static String wrap(int i) {
        return wrap(String.valueOf(i));
    }

    public static String stripColor(String s) {
        return ChatColor.stripColor(s);
    }

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static EmbedBuilder embed(SlashCommandInteractionEvent event) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setFooter("Requested by " + event.getUser().getAsTag())
                .setTimestamp(Instant.now());
    }

    /**
     * returns a view (not a new list) of the sourceList for the
     * range based on page and pageSize
     * @param sourceList List to check
     * @param page, page number should start from 1
     * @param pageSize page size
     */
    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            return Collections.emptyList();
        }

        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() <= fromIndex){
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    public static void sendMessage(CommandSender receiver, String s) {
        receiver.sendMessage(colorize(s));
    }

}
