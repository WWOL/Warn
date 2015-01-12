package mccarthy.brian.warn;

import mccarthy.brian.warn.commands.CommandList;
import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.plugin.Plugin;

/**
 * Main plugin class
 * @author Brian McCarthy
 *
 */
public class Warn extends Plugin {

	private static Warn INSTANCE;
	private WarnActions actions;
	
	public Warn() {
		INSTANCE = this;
	}
	
	@Override
	public boolean enable() {
		actions = new WarnActions();
		WarnSettings.setup();
		
		Canary.hooks().registerListener(new WarnListener(), this);
		try {
			Canary.commands().registerCommands(new CommandList(), this, false);
		} catch (CommandDependencyException e) {
			getLogman().warn("Could not load " + getName() + " because of a command dependency exception!");
			return false;
		}
		return true;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

	public static Warn getInstance() {
		return INSTANCE;
	}

	public WarnActions getActions() {
		return actions;
	}
	
}