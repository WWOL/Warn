package mccarthy.brian.warn;

import net.canarymod.config.Configuration;
import net.visualillusionsent.utils.PropertiesFile;

/**
 * Contain and load various properties
 * @author Brian McCarthy
 *
 */
public class WarnSettings {

	private static PropertiesFile props;
	
	public static int maxWarns = 3;
	
	public static void setup() {
		props = Configuration.getPluginConfig(Warn.getInstance(), "Settings");
		if (props.getBoolean("useDefaults", false)) {
			// Do not load these values from the properties file, use defaults
			return;
		}
		
		maxWarns = props.getInt("maxWarns", maxWarns);
	}
}
