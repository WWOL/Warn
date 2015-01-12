package mccarthy.brian.warn.commands;

import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;

/**
 * List of all commands, used for registering with the system
 * @author Brian McCarthy
 *
 */
public class CommandList implements CommandListener {

	@Command(aliases = {"warn"},
			description = "Warn a player",
			permissions = {"warn"},
			toolTip = "/warn <<player> [amount] | check <player> | remove <player>>",
			version = 2,
			min = 1,
			max = 2)
	public void warnCommand(MessageReceiver caller, String[] parameters) {
		new WarnCommand().execute(caller, parameters);
	}

	@Command(aliases = {"check"},
			description = "Check the number of warns a player has",
			permissions = {"warn.check"},
			toolTip = "/warn check [player]",
			version = 2,
			parent = "warn",
			min = 0,
			max = 1)
	public void warnCheckCommand(MessageReceiver caller, String[] parameters) {
		new WarnCheckCommand().execute(caller, parameters);
	}
	
	@Command(aliases = {"remove"},
			description = "Remove warnings from a player",
			permissions = {"warn.remove"},
			toolTip = "/warn remove <player> [amount]",
			version = 2,
			parent = "warn",
			min = 1,
			max = 2)
	public void warnRemoveCommand(MessageReceiver caller, String[] parameters) {
		new WarnRemoveCommand().execute(caller, parameters);
	}
	
}