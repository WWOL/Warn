public class CustomHook{ 
	static PluginInterface WarnListener = new CustomListener();

		public static class CustomListener implements PluginInterface{ //this sets the class for your custom listener
			public String getName(){ //this is what get the name of your CustomListener
		            return "WarnListener"; //this is the name of your CustomListener
		       }
			public int getNumParameters() { //This gets the number of paramerters needed to use your CustomListener
			      return 2; //This is the number of paramerters needed to use your CustomListener
			}
			public String checkParameters(Object[] os) { //This is the code to check that a plugin is calling the right paramerters for your listener
				if ((os.length < 1) || (os.length > getNumParameters())) {
					return "Invalid amount of parameters.";
				}
				return null;
			}
			public Object run(Object[] os) { //This is the code to your listener
				String Command = (String) os[0]; //this can be anything you want to check
				Player player = (Player) os[1]; //The player to warn.
				if(Command.equalsIgnoreCase("WL_Warn")){
						int warnsint = Warn.Warns.getInt(player.getName());
						Warn.Warns.setInt(player.getName(), warnsint + 1);
						if (Warn.Warns.getInt(player.getName()) >= Warn.maxwarns)player.kick(Warn.spre + Warn.formatmsg(Warn.kickmsg, player));
						player.sendMessage(Warn.pre + Warn.formatmsg(Warn.onwarned, player));
						int totalwarns = Warn.Warns.getInt("Total-Warns");
						Warn.Warns.setInt("Total-Warns", totalwarns + 1);
						Warn.Warns.save();
					return null; 
				}//WL_Warn
				
				else if(Command.equalsIgnoreCase("WL_MaxWarns")){
					return Warn.WarnProps.getInt("Max-Warns", 3); 
			}//WL_MaxWarns
				
				else if(Command.equalsIgnoreCase("WL_TotalWarns")){
					return Warn.Warns.getInt("Total-Warns"); 
			}//WL_TotalWarns
				
				else if(Command.equalsIgnoreCase("WL_PlayerWarns")){
					return Warn.Warns.getInt(player.getName()); 
			}//WL_TotalWarns
				
				return null;
			}//run
		}//Listener
	}//class