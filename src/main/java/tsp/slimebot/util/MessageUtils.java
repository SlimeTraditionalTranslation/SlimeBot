package tsp.slimebot.util;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtils {

    public static String asString(Collection<SlimefunItem> items) {
        StringBuilder builder = new StringBuilder();
        for (SlimefunItem item : items) {
            builder.append(MessageUtils.wrap(stripColor(item.getItemName()))).append(",");
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
        String wiki = item.getWikipage().isPresent() ? item.getWikipage().get() : "";
        return " > ID: " + wrap(item.getId()) + "\n" +
                " > Addon: " + wrap(item.getAddon().getName()) + "\n" +
                " > State: " + wrap(item.getState().name()) + "\n" +
                " > Wiki: " + (wiki.isEmpty() ? "None" : wiki) + "\n";
    }

    public static String category(SlimefunItem item) {
        return " > ID: " + wrap(item.getItemGroup().getKey().toString()) + "\n" +
                " > Name: " + wrap(item.getItemGroup().getUnlocalizedName()) + "\n" +
                " > Tier: " + wrap(item.getItemGroup().getTier()) + "\n" +
                " > Addon: " + wrap(item.getItemGroup().getAddon().getName()) + "\n";
    }

    public static String research(SlimefunItem item) {
        String research = "None";
        if (item.getResearch() != null) {
            research = " > ID: " + wrap(item.getResearch().getKey().toString()) + "\n" +
                    " > Items included: " + wrap(item.getResearch().getAffectedItems().size()) + "\n" +
                    " > Cost: " + wrap(item.getResearch().getCost()) + "\n";
        }

        return research;
    }

    public static String recipe(SlimefunItem item) {
        return " > Machine ID: " + wrap(item.getRecipeType().getKey().toString()) + "\n" +
                " > Machine Name: " + wrap(stripColor(item.getRecipeType().getMachine().getItemName())) + "\n" +
                " > Output (" + item.getRecipeOutput().getAmount() + "): " + wrap(MessageUtils.getName(item.getRecipeOutput()));
    }

    public static String recipeGrid(SlimefunItem item) {
        ItemStack[] recipe = item.getRecipe();
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


    public static String wrap(String string) {
        return "`" + string + "`";
    }

    public static String wrap(int i) {
        return wrap(String.valueOf(i));
    }

    public static String stripColor(String s) {
        return ChatColor.stripColor(s);
    }

}
