package mccarthy.brian.warn.commands;

import mccarthy.brian.warn.Warn;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;

/**
 * \"\/warn remove\" command
 * @author Brian McCarthy
 *
 */
public class WarnRemoveCommand {

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
		int currWarnings = Warn.getInstance().getActions().getWarns(uuid);
		if (currWarnings - amount < 0) {
			caller.message("A player can not have less than 0 warnings.");
			caller.message("Please try a number smaller than " + amount + ".");
			return;
		}
		
		String formattedWarns = Warn.getInstance().getActions().colorWarnNumber(amount);
		Warn.getInstance().getActions().removeWarn(uuid, amount);
		caller.message("Removed " + formattedWarns + " warning(s) from " + parameters[0] + ".");
		Player target = Canary.getServer().getPlayer(parameters[0]);
		if (target != null) {
			target.message("You had " + formattedWarns + " warning(s) removed by " + caller.getName());
		}
	}

}