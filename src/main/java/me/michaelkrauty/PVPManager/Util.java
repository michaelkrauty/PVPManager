package me.michaelkrauty.PVPManager;

/**
 * Created on 7/2/2015.
 *
 * @author michaelkrauty
 */
public class Util {

    public static String parseFormat(String str) {
        return str
                .replace("&1", "§1")
                .replace("&2", "§2")
                .replace("&3", "§3")
                .replace("&4", "§4")
                .replace("&5", "§5")
                .replace("&6", "§6")
                .replace("&7", "§7")
                .replace("&8", "§8")
                .replace("&9", "§9")
                .replace("&a", "§a")
                .replace("&b", "§b")
                .replace("&c", "§c")
                .replace("&d", "§d")
                .replace("&e", "§e")
                .replace("&f", "§f")
                .replace("&k", "§k")
                .replace("&l", "§l")
                .replace("&m", "§m")
                .replace("&n", "§n")
                .replace("&o", "§o")
                .replace("&r", "§r");
    }

    public static String formatMessage(String str, String target) {
        return parseFormat(str).replace("%target%", target);
    }

    public static String formatMessage(String str, int seconds) {
        return parseFormat(str).replace("%seconds%", Integer.toString(seconds));
    }

    public static String formatMessage(String str, String target, int seconds) {
        return parseFormat(str)
                .replace("%target%", target)
                .replace("%seconds%", Integer.toString(seconds));
    }
}
