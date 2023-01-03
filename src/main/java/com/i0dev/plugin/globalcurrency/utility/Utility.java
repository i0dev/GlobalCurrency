package com.i0dev.plugin.globalcurrency.utility;

import com.i0dev.plugin.globalcurrency.object.Pair;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utility {

    public static boolean isClassInProject(String classpath) {
        try {
            Class.forName(classpath);
            return true;
        } catch (Exception ingored) {
            return false;
        }
    }

    public static String formatNumber(long number) {
        return NumberFormat.getIntegerInstance().format(number);
    }

    public static Integer getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> color(List<String> ss) {
        List<String> ret = new ArrayList<>();
        ss.forEach(s -> ret.add(color(s)));
        return ret;
    }

    public static ItemStack makeItemStackManual(Material material, int amount, short data, String name, List<String> lore, boolean glow) {
        if (lore == null)
            lore = new ArrayList<>();
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        meta.setLore(color(lore));
        if (glow) meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack modifyItemStackManual(ItemStack item, Material material, int amount, short data, String name, List<String> lore, boolean glow) {
        if (lore == null)
            lore = new ArrayList<>();
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        meta.setLore(color(lore));
        if (glow) meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    @SafeVarargs
    public static String pair(String msg, Pair<String, String>... pairs) {
        for (Pair<String, String> pair : pairs) {
            msg = msg.replace(pair.getKey(), pair.getValue());
        }
        return msg;
    }


    public static Integer parseIntOrNull(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void sendActionBarTextToPlayer(Player player, String message) {
        try {
            message = color(message);
            Object entity = getOBCClass("entity.CraftPlayer").cast(player);
            Object handle = entity.getClass().getMethod("getHandle").invoke(entity);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            Object chatText = getNMSClass("ChatComponentText").getConstructor(String.class).newInstance(message);
            Object packet;
            try {
                packet = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class).newInstance(chatText, (byte) 2);
            } catch (Exception ex2) {
                packet = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), getNMSClass("ChatMessageType")).newInstance(chatText, Enum.valueOf((Class<? extends Enum>) getNMSClass("ChatMessageType"), "GAME_INFO"));
            }
            connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Class<?> getOBCClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?> getNMSClass(String name, String def) {
        return getNMSClass(name) != null ? getNMSClass(name) : getNMSClass(def.split("\\.")[0]).getDeclaredClasses()[0];
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static ItemStack[] deserializeBase64ToItems(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) items[i] = (ItemStack) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack[0];
        }
    }

    public static String formatTimePeriod(long timePeriod) {
        long millis = timePeriod;

        String output = "";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        if (days > 1) output += days + " days ";
        else if (days == 1) output += days + " day ";

        if (hours > 1) output += hours + " hours ";
        else if (hours == 1) output += hours + " hour ";

        if (minutes > 1) output += minutes + " minutes ";
        else if (minutes == 1) output += minutes + " minute ";

        if (seconds > 1) output += seconds + " seconds ";
        else if (seconds == 1) output += seconds + " second ";

        if (output.isEmpty()) return "0 seconds";

        return StringUtils.trim(output);
    }

    public static String serializeItemsToBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Inventory fillInventoryWithBorder(Inventory inv, ItemStack border) {

        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack content = inv.getContents()[i];
            if (content == null || Material.AIR.equals(content.getType())) {
                inv.setItem(i, border);
            }
        }
        return inv;

    }

    public int getMaxInventorySize(int number) {
        if (number >= 0 && number <= 9) {
            return 9;
        } else if (number >= 10 && number <= 18) {
            return 18;
        } else if (number >= 19 && number <= 27) {
            return 27;
        } else if (number >= 28 && number <= 36) {
            return 36;
        } else if (number >= 37 && number <= 45) {
            return 45;
        } else {
            return 54;
        }
    }


    public static String getUUIDFromIGNAPI(String ign) {
        try {
            StringBuilder result = new StringBuilder();
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + ign).openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 403) {
                return null;
            }
            String line;
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) result.append(line);
            rd.close();
            return ((JSONObject) new JSONParser().parse(result.toString())).get("id").toString();
        } catch (ParseException | IOException exception) {
            return null;
        }
    }

    public static String getIGNFromUUID(String uuid) {
        try {
            return getGeneralRequest("GET", "https://sessionserver.mojang.com/session/minecraft/profile/", uuid).get("name").toString();
        } catch (Exception ignored) {
            return null;
        }
    }

    private static JSONObject getGeneralRequest(String method, String url, String param) {
        return getGeneralRequest(method, url, param, "", "");
    }

    private static JSONObject getGeneralRequest(String method, String url, String param, String HeaderKey, String HeaderValue) {
        try {
            StringBuilder result = new StringBuilder();
            HttpURLConnection conn = (HttpURLConnection) new URL(url + param).openConnection();
            conn.setRequestMethod(method);
            if (!HeaderKey.equals("") && !HeaderValue.equals("")) {
                conn.setRequestProperty(HeaderKey, HeaderValue);
            }
            if (conn.getResponseCode() == 403) {
                return new JSONObject();
            }
            String line;
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) result.append(line);
            rd.close();
            conn.disconnect();
            if (result.toString().contains("�")) return null;
            return (JSONObject) new JSONParser().parse(result.toString());
        } catch (ParseException | IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String ordinal(int i) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    public static String capitalizeFirst(String... words) {
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
        }
        return result.toString().trim();
    }
}
