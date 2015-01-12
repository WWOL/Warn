package mccarthy.brian.warn;

import net.canarymod.Canary;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.ChatFormat;
import net.canarymod.config.Configuration;
import net.visualillusionsent.utils.PropertiesFile;

/**
 * Actions that are used across multiple commands / listeners
 * @author Brian McCarthy
 *
 */
public class WarnActions {

	private PropertiesFile warnsFile;
	
	public WarnActions() {
		warnsFile = Configuration.getPluginConfig(Warn.getInstance(), "Warns");
	}
	
	public void warnPlayer(String uuid) {
		warnPlayer(uuid, 1);
	}
	
	public void warnPlayer(String uuid, int value) {	
		int currWarns = getWarns(uuid);
		warnsFile.setInt(uuid, currWarns + value);
		warnsFile.save();
	}
	
	public void removeWarn(String uuid) {
		removeWarn(uuid, 1);
	}
	
	public void removeWarn(String uuid, int amount) {
		warnPlayer(uuid, -amount);
	}
	
	public int getWarns(String uuid) {
		int playerWarns = warnsFile.getInt(uuid, 0);
		return playerWarns;
	}
	
	public String getUuidFromName(String name) {
		String uuid = null;
		Player p = Canary.getServer().matchPlayer(name);
		if (p != null) {
			uuid = p.getUUIDString();
		} else {
			OfflinePlayer op = Canary.getServer().getOfflinePlayer(name);
			if (op != null) {
				uuid = op.getUUIDString();
			}
		}
		return uuid;
	}
	
	public String colorWarnNumber(int warns) {
		String color = ChatFormat.BOLD.toString();
		color += warns == 0 ? ChatFormat.GREEN.toString() : ChatFormat.RED.toString();
		return color + warns + ChatFormat.RESET;
	}
	
}