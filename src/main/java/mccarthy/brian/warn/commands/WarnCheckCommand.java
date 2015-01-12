package mccarthy.brian.warn.commands;

import mccarthy.brian.warn.Warn;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;

/**
 *\"\/warn check\" command
 * @author Brian McCarthy
 *
 */
public class WarnCheckCommand {

	public void execute(MessageReceiver caller, String[] parameters) {
		if (parameters.length == 0) {
			if (!(caller instanceof Player)) {
				caller.message("Cannot check a non-players warns. Use \"/warn check <player>\"");
				return;
			} else {
				String uuid = Warn.getInstance().getActions().getUuidFromName(caller.getName());
				int warns = Warn.getInstance().getActions().getWarns(uuid);
				String formattedWarns = Warn.getInstance().getActions().colorWarnNumber(warns);
				caller.message("You have " + formattedWarns + " warnings.");
				return;
			}
		}

		String uuid = Warn.getInstance().getActions().getUuidFromName(parameters[0]);
		
		if (uuid == null) {
			caller.message("Could not find player " + parameters[0]);
		} else {
			if (caller.hasPermission("warn.check.other")) {
				int warns = Warn.getInstance().getActions().getWarns(uuid);
				String formattedWarns = Warn.getInstance().getActions().colorWarnNumber(warns);
				caller.message(parameters[0] + " has " + formattedWarns + " warnings.");
			} else {
				caller.message("You do not have permission to check other players warnings.");
				caller.message("Check your own using \"/warn check\"");
			}
		}
	}

}