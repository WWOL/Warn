package mccarthy.brian.warn.commands;

import mccarthy.brian.warn.Warn;
import mccarthy.brian.warn.WarnSettings;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;

/**
 * \"\/warn\" command
 * @author Brian McCarthy
 *
 */
public class WarnCommand {

	public void execute(MessageReceiver caller, String[] parameters) {
		String uuid = Warn.getInstance().getActions().getUuidFromName(parameters[0]);
		if (uuid == null) {
			caller.message("Could not find player " + parameters[0]);
			return;
		}
		
		int amount = 1;
		if (parameters.length == 2) {
			try {
				amount = Integer.parseInt(parameters[1]);
			} catch (Exception e) {
				caller.notice("Could not parse number of warns. Using 1.");
			}
		}
		
		String formattedWarns = Warn.getInstance().getActions().colorWarnNumber(amount);
		Warn.getInstance().getActions().warnPlayer(uuid, amount);
		caller.message("Warned " + parameters[0] + " " + formattedWarns + " time(s).");
		
		Player target = Canary.getServer().getPlayer(parameters[0]);
		if (target != null) {
			target.message("You were given " + formattedWarns + " warning(s) by " + caller.getName());
			int warns = Warn.getInstance().getActions().getWarns(target.getUUIDString());
			if (warns > WarnSettings.maxWarns) {
				target.kick("You have been kicked because you have too many warnings");
			}
		}
	}

}