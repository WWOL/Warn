/*
 * BUG FIXES:
 * Stopped the ability to add negative warns. (Checks on login and on command /removewarn.)
 * Fixed property file generation. (It was off by on "s" dammit!)
 * Cleaned up a lot of code.
 * 
 * ADDITIONS:
 * Custom message support! Change most messages into your own language. Defaults to English.
 * 
 * REMOVALS:
 * Shitty code is shitty.
 */

//TODO Test the bugs with the new system.

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Logger;

public class Warn extends Plugin {
	private Logger log = Logger.getLogger("Minecraft");
	public static PropertiesFile WarnProps;
	public static PropertiesFile Warns;
	public static PropertiesFile lang;
	
	private Warnlistener Warnlistener = new Warnlistener();
	
	static String name = "Warn";
	static String maker = "WWOL";
	static String ver = "2.1";
	static String config = "plugins" + File.separator + "config" + File.separator + "Warn" + File.separator;
	static String pre = Colors.Blue + "[Warn] " + Colors.Gold;
	static String spre = "[Warn] ";
	String updatrUrl = "http://dl.dropbox.com/u/12112730/Warn.updatr";
	String updatrFileUrl = "http://dl.dropbox.com/u/12112730/Warn.jar";
	String updatrNotes = "Going International! Now with custom message support, it's a new era of Warn. Code cleanup.";
	static String mapformat;

	boolean loginmsg;
	boolean useupdatr = true;
	
	static int maxwarns = 3;

	// Lets group all the language based variables together.
	static String kickmsg;
	static String loginstring;
	static String onwarned;
	static String loginkick;
	static String publickickmsg;
	static String onwarn;
	static String mywarns;
	static String theirwarns;
	static String onwarnremove;
	static String onwarnremoved;
	static String warners;
	static String canwarntrue;
	static String canwarnfalse;
	static String totalwarns;
	static String warnsatzero;
	
	public void initalize() {
		log.info(spre + name + " by " + maker + " Ver:" + ver + " initalized!");
	}

	public void enable() {
		log.info(spre + name + " by " + maker + " Ver:" + ver + " enabled!");
		etc.getLoader().addListener(PluginLoader.Hook.LOGIN, Warnlistener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, Warnlistener, this, PluginListener.Priority.MEDIUM);
		WarnPropsLoad();
		if (useupdatr)WarnUpdatrLoad();
		etc.getInstance().addCommand("Warn Plugin", " - <> = required, [] = optional");
		etc.getInstance().addCommand("/warn <player>", " - Warns a player.");
		etc.getInstance().addCommand("/mywarns", " - Checks your own warn level.");
		etc.getInstance().addCommand("/theirwarns <player>", " - Check another player warn level.");
		etc.getInstance().addCommand("/removewarn <player>", " - Removes a warn from a player.");
		etc.getInstance().addCommand("/warners", " - Shows a list of players that can warn.");
		etc.getInstance().addCommand("/canwarn <player>", " - Tells you if the given player can warn.");
		etc.getInstance().addCommand("/totalwarns", " - Show the total warns given on the server.");
		etc.getInstance().addCommand("/warnmap", " - Show a graphical display of you warn count.");
		etc.getLoader().addCustomListener(CustomHook.WarnListener);
	}

	public void disable() {
		log.info(spre + name + " by " + maker + " Ver:" + ver + " disabled!");
		etc.getInstance().removeCommand("/warn");
		etc.getInstance().removeCommand("/mywarns");
		etc.getInstance().removeCommand("/removewarn");
		etc.getInstance().removeCommand("/warners");
		etc.getInstance().removeCommand("/theirwarns");
		etc.getInstance().removeCommand("/canwarn");
		etc.getInstance().removeCommand("/totalwarns");
		etc.getInstance().removeCommand("Warn Plugin");
		etc.getInstance().removeCommand("/warnmap");
	}

	public void WarnPropsLoad() {
		// setting the properties files to point at the txt files and setting the maxwarns int.
		try {
			File configDir = new File(config);
			if (!configDir.exists())configDir.mkdirs();
			WarnProps = new PropertiesFile(config + "Warn.properties");
			Warns = new PropertiesFile(config + "Warns.txt");
			lang = new PropertiesFile(config + "lang.txt");
			
			maxwarns = WarnProps.getInt("Max-Warns", 3);
			loginmsg = WarnProps.getBoolean("LoginMsg", true);
			useupdatr = WarnProps.getBoolean("Use-Updatr", true);
			mapformat = WarnProps.getString("mapformat", "|| ");
			
			
			// Setting language based stuff.
			kickmsg = lang.getString("onkick-msg", "You have too many warnings!");
			loginstring = lang.getString("onlogin-msg", "You have %warns / %maxwarns warnings."); 
			onwarned = lang.getString("onwarned-msg", "You have been warned! You have %warns / %maxwarns warnings.");
			loginkick = lang.getString("onloginkick-msg", "You have too many warnings!");
			publickickmsg = lang.getString("onkickpublic-msg", "%player was kicked for having too many warnings!");
			onwarn = lang.getString("onwarn-msg", "You warned %player !");
			mywarns = lang.getString("mywarns-msg", "You have %warns / %maxwarns warnings.");
			theirwarns = lang.getString("theirwarns-msg", "%player has %warns / %maxwarns warnings.");
			onwarnremove = lang.getString("onwarnremove-msg", "Removed warn from %player .");
			onwarnremoved = lang.getString("onwarnremoved-msg", "You now have %warns / %maxwarns warnings.");
			warners = lang.getString("warners-msg", "The following people can issue warns;");
			canwarntrue = lang.getString("canwarntrue-msg", " %player CAN issue warns.");
			canwarnfalse = lang.getString("canwarnfalse-msg", " %player CANNOT issue warns.");
			totalwarns = lang.getString("totalwarns-msg", "There is %totalwarns warns on the server.");
			warnsatzero = lang.getString("warnsatzero-msg", "You may not give negative warns.");

		} catch (Exception e) {log.severe(spre + "Creation of config folder AND/OR Properties file went wrong! " + e);}
		
		// Adding comments on the syntax of the warns.txt file.
		try {
			File warns = new File(config + "Warns.txt");
			if (!warns.exists()) {
				warns.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(warns));
				writer.write("#This file contains all player warns.");
				writer.write("#They are in the format:");
				writer.write("#string:int");
				writer.write("#This is becasue");
				writer.write("#player-name:warns");
				writer.close();
			}
		} catch (Exception e) {log.severe(spre + "Creation of Warns file went wrong! " + e + "/r/n"); e.printStackTrace();}
	}// warnpropsload

	public static String formatmsg(String s, Player p){
		s = s.replaceAll("%player", p.getName());
		s = s.replaceAll("%maxwarns", String.valueOf(maxwarns));
		s = s.replaceAll("%warns", String.valueOf(Warns.getInt(p.getName())));
		s = s.replaceAll("%totalwarns", String.valueOf(Warns.getInt("Total-Warns")));
		s = s.replaceAll("&", "§");
		return s;
	}// format string
	
	public void WarnUpdatrLoad() {
		try {
			File updatrDir = new File("Updatr");
			if (!updatrDir.exists())updatrDir.mkdirs();
			if (updatrDir.exists()) {
				File updatrFile = new File("Updatr" + File.separator + name + ".updatr");
				if (!updatrFile.exists()) {
					updatrFile.createNewFile();
					BufferedWriter writer = new BufferedWriter(new FileWriter(updatrFile));
					writer.write("name = " + name);
					writer.newLine();
					writer.write("version = " + ver);
					writer.newLine();
					writer.write("url = " + updatrUrl);
					writer.newLine();
					writer.write("file = " + updatrFileUrl);
					writer.newLine();
					writer.write("notes = " + updatrNotes);
					writer.newLine();
					writer.close();
				}
			}
		} catch (Exception e) {log.severe(spre + "Creation of Updatr file went wrong! " + e);}
	}// end updatrload

	public class Warnlistener extends PluginListener {

		public void onLogin(Player p) {
			if (!Warns.containsKey(p.getName())) Warns.setInt(p.getName(), 0); // check if the player has a key holding there warn count
			if (Warns.getInt(p.getName()) < 0) Warns.setInt(p.getName(), 0);
			if (Warns.getInt(p.getName()) >= maxwarns) {
				p.kick(spre + formatmsg(loginkick, p));
				if (!publickickmsg.equalsIgnoreCase(""))etc.getServer().messageAll(pre + formatmsg(publickickmsg, p));
			}// max warns check
			if (loginmsg)p.sendMessage(pre + formatmsg(loginstring, p));
		}// login

		public boolean onCommand(Player p, String[] s) {
			if (s[0].equalsIgnoreCase("/warn") && p.canUseCommand("/warn")) {
				try {
					Player warnplayer = etc.getServer().matchPlayer(s[1]);
					int warnsint = Warns.getInt(warnplayer.getName());
					Warns.setInt(warnplayer.getName(), warnsint + 1);
					if (Warns.getInt(warnplayer.getName()) >= maxwarns)warnplayer.kick(spre + formatmsg(kickmsg, warnplayer));
					p.sendMessage(pre + formatmsg(onwarn, warnplayer));
					warnplayer.sendMessage(pre + formatmsg(onwarned, warnplayer));
					int totalwarns = Warns.getInt("Total-Warns");
					Warns.setInt("Total-Warns", totalwarns + 1);
					Warns.save();
				} catch (Exception e) {p.sendMessage(pre + "The correct syntax is /warn <player>");}
				return true;
			}// warn cmd
			
			else if (s[0].equalsIgnoreCase("/mywarns") && p.canUseCommand("/mywarns")) {
				p.sendMessage(pre + formatmsg(mywarns, p));
				return true;
			}// mywarns

			else if (s[0].equalsIgnoreCase("/theirwarns") && p.canUseCommand("/theirwarns")) {
				try{
				Player warnplayer = etc.getServer().matchPlayer(s[1]);
				p.sendMessage(pre + formatmsg(theirwarns, warnplayer));
				return true;
				}catch(Exception e){p.sendMessage(pre + "The correct syntax is /theirwarns <player>");}
			}// their warns

			else if (s[0].equalsIgnoreCase("/removewarn") && p.canUseCommand("/removewarn")) {
				try {
					Player warnplayer = etc.getServer().matchPlayer(s[1]);
					int warnsint = Warns.getInt(warnplayer.getName());
					if (warnsint == 0)p.sendMessage(pre + formatmsg(warnsatzero, p));
					if (!(warnsint == 0)){
					Warns.setInt(warnplayer.getName(), warnsint - 1);
					p.sendMessage(pre + formatmsg(onwarnremove, warnplayer));
					warnplayer.sendMessage(pre + formatmsg(onwarnremoved, warnplayer));
					int totalwarns = Warns.getInt("Total-Warns");
					Warns.setInt("Total-Warns", totalwarns - 1);
					Warns.save();}
				} catch (Exception e) {p.sendMessage(pre + "The correct syntax is /removewarn <player>");}
				return true;
			}// remove warn

			else if (s[0].equalsIgnoreCase("/warners") && p.canUseCommand("/warners")) {
				p.sendMessage(pre + formatmsg(warners, p));
				for (Player tmp : etc.getServer().getPlayerList()) {
					if (tmp.canUseCommand("/warn")) {
						p.sendMessage(pre + tmp.getName() + ",");
					}// if
				}// for
				return true;
			}// warners

			else if (s[0].equalsIgnoreCase("/canwarn") && p.canUseCommand("/canwarn")) {
				try {
					Player canwarn = etc.getServer().matchPlayer(s[1]);
					//Try this for offline players, 
					//Player canwarn = etc.getDataSource().getPlayer(s[1]);
					if (canwarn.canUseCommand("/warn")) {p.sendMessage(pre + formatmsg(canwarntrue, canwarn));} 
					else {p.sendMessage(pre + formatmsg(canwarnfalse, canwarn));}
					
				} catch (Exception e) {p.sendMessage(pre + "The correct syntax is /canwarn <player>");}
				return true;
			}// can warn

			else if (s[0].equalsIgnoreCase("/totalwarns") && p.canUseCommand("/totalwarns")) {
				p.sendMessage(pre + formatmsg(totalwarns, p));
				return true;
			}// total
			
			else if (s[0].equalsIgnoreCase("/warnmap") && p.canUseCommand("/warnmap")) {
				int j = Warns.getInt(p.getName());
				int loopnum = 0;
				StringBuilder sb = new StringBuilder();
				
				for (int i = 0; i < maxwarns; i++){
					if (loopnum < j) sb.append(Colors.Red + mapformat); 
					else sb.append(Colors.Green + mapformat);
					loopnum++;
				}
				p.sendMessage(sb.toString());
				return true;
			}// warnmap

			return false;
		}// onCommand
	}// listener
}// plugin