package mccarthy.brian.warn;

import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.plugin.PluginListener;

/**
 * Main listener class
 * @author Brian McCarthy
 *
 */
public class WarnListener implements PluginListener {

	@HookHandler
	public void onLogin(ConnectionHook hook) {
		int warns = Warn.getInstance().getActions().getWarns(hook.getPlayer().getUUIDString());
		if (warns > WarnSettings.maxWarns) {
			hook.getPlayer().kick("You have been kicked because you have too many warnings");
		}
	}
	
}