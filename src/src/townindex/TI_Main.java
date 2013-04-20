package src.townindex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class TI_Main extends JavaPlugin {
	Map<String, TI_Data> tlist = new HashMap<String, TI_Data>();
	List<String> townnames = new ArrayList<String>();

	private FileConfiguration towns = null;
	private File townsFile = null;
	private FileConfiguration tlistf = null;
	private File tlistFile = null;

	@Override
	public void onEnable() {
		System.out.println("Town Index Enabled!");
		File file = new File(getDataFolder() + File.separator + "config.yml");

		if (!file.exists()) {
			getLogger().info("Generating config.yml...");

			getConfig().addDefault("townadd", "&2Town added!");
			getConfig().addDefault("townpresent", "&4Town already exists.");
			getConfig().addDefault("tiremove", "&2Town removed!");
			getConfig().addDefault("tina", "&4Town does not exist.");
			getConfig().addDefault("tihelp", "&6Use command as /ti <info|list|edit>.");
			getConfig().addDefault("tiadminhelp", "&6Admins use command as /ti <add|remove>");
			getConfig().addDefault("listname", "&6Towns of Cubeville!");
			getConfig().addDefault("tipermission", "&4You do not have permission to use this command.");
			getConfig().addDefault("tilistsize", "&4There are not that many towns on Cubeville!");
			getConfig().addDefault("wing1", "gold");
			getConfig().addDefault("wing2", "lapis");
			getConfig().addDefault("wing3", "iron");
			getConfig().addDefault("wing4", "diamond");
			getConfig().addDefault("tiedit", "&aEdit to the town has been made successfully!");
			getConfig().addDefault("tiedithelpbasic", "&6Use as /ti edit <town> <plots|location|wing|vmayor>.");
			getConfig().addDefault("tiedithelpadmin", "&6Admins use /ti edit <mayor|status>");

			getConfig().options().copyDefaults(true);
		}
		ConfigurationSerialization.registerClass(TI_Data.class, "TI_Data");
		townnames.addAll(tlist.keySet());
		Collections.sort(townnames);
		this.townnames = getTowns().getStringList("townnames");
		reloadTowns();
		reloadTlist();
	}

	@Override
	public void onDisable() {
		getTowns().set("townnames", this.townnames);
		saveTowns();
		saveConfig();
		saveTlist();
	}

	public void reloadTlist() {
		if (this.tlistFile == null) {
			this.tlistFile = new File(getDataFolder(), "tlist.yml");
		}
		this.tlistf = YamlConfiguration.loadConfiguration(this.tlistFile);
		for (String key : tlistf.getKeys(false)) {
			TI_Data data = (TI_Data) tlistf.get(key);
			if (data == null)
				continue;
			tlist.put(key, data);
		}
	}

	public FileConfiguration getTlist() {
		if (this.tlistf == null) {
			reloadTlist();
		}
		return this.tlistf;
	}

	public void saveTlist() {
		if ((this.tlistf == null) || (this.tlistFile == null))
			return;
		try {
			for (Map.Entry<String, TI_Data> entry : tlist.entrySet()) {
				tlistf.set(entry.getKey(), entry.getValue());
			}
			getTlist().save(this.tlistFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + this.tlistFile, ex);
		}
	}

	public void reloadTowns() {
		if (this.townsFile == null) {
			this.townsFile = new File(getDataFolder(), "towns.yml");
		}
		this.towns = YamlConfiguration.loadConfiguration(this.townsFile);
	}

	public FileConfiguration getTowns() {
		if (this.towns == null) {
			reloadTowns();
		}
		return this.towns;
	}

	public void saveTowns() {
		if ((this.towns == null) || (this.townsFile == null))
			return;
		try {
			getTowns().save(this.townsFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + this.townsFile, ex);
		}
	}

	public String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String tipermission = getConfig().getString("tipermission");
		String tihelp = getConfig().getString("tihelp");
		String tiadminhelp = getConfig().getString("tiadminhelp");
		String tilistsize = getConfig().getString("tilistsize");
		String tina = getConfig().getString("tina");
		String wing1 = getConfig().getString("wing1");
		String wing2 = getConfig().getString("wing2");
		String wing3 = getConfig().getString("wing3");
		String wing4 = getConfig().getString("wing4");
		String tiedit = getConfig().getString("tiedit");
		String tiedithelpbasic = getConfig().getString("tiedithelpbasic");
		String tiedithelpadmin = getConfig().getString("tiedithelpadmin");
		tiedit = (color(tiedit));
		int listnum;
		if (cmd.getName().equalsIgnoreCase("ti")) {
			if (args.length == 0) {
				sender.sendMessage(color(tihelp));
				if (sender.hasPermission("ti.admin.help"))
					sender.sendMessage(color(tiadminhelp));
			} else if (args.length == 1) {
				if (args[0].toLowerCase().equals("help")) {
					if (sender.hasPermission("ti.help")) {
						sender.sendMessage(color(tihelp));
						if (sender.hasPermission("ti.admin.help"))
							sender.sendMessage(color(tiadminhelp));
					} else {
						sender.sendMessage(color(tipermission));
					}
					return true;
				} else if (args[0].toLowerCase().equals("list")) {
					if (sender.hasPermission("ti.list")) {
						try {
							listnum = 1;
							String listname = getConfig().getString("listname");
							int townnamessize = townnames.size();
							sender.sendMessage(color(listname));
							int startfrom = (listnum * 10) - 10;
							for (int i = startfrom; i < startfrom + 10; i++) {
								if (townnames.size() == 0) {
									sender.sendMessage(color("&aNo towns available."));
									break;
								}
								int sidenum = (i + 1);
								if (i > townnames.size())
									break;
								if (i == townnamessize)
									break;
								sender.sendMessage(color("&6" + sidenum + ": &2" + townnames.get(i)));
							}
						} catch (NumberFormatException e) {
							sender.sendMessage(color("&4Argument must be a whole number!"));
						}
					} else {
						sender.sendMessage(color(tipermission));
					}
				} else {
					sender.sendMessage(color(tihelp));
					if (sender.hasPermission("ti.admin.help"))
						sender.sendMessage(color(tiadminhelp));
				}
			} else if (args.length == 2) {
				String townname = args[1];
				if (args[0].toLowerCase().equals("add")) {
					if (sender.hasPermission("ti.admin.add")) {
						if ((this.tlist.containsKey(townname)) || (this.townnames.contains(townname))) {
							String townpresent = getConfig().getString("townpresent");
							sender.sendMessage(color(townpresent));
						} else {
							this.townnames.add(townname);
							TI_Data newtown = new TI_Data(townname);
							tlist.put(townname.toLowerCase(), newtown);
							String townadd = getConfig().getString("townadd");
							sender.sendMessage(color(townadd));
							Collections.sort(townnames);
							reloadTlist();
						}
					} else {
						sender.sendMessage(color(tipermission));
					}
					return true;
				} else if (args[0].toLowerCase().equals("remove")) {
					if (sender.hasPermission("ti.admin.remove")) {
						if (this.tlist.containsKey(townname)) {
							this.tlist.remove(townname);
							sender.sendMessage(color("&aTown data deleted."));
						}
						if (this.townnames.contains(townname)) {
							this.townnames.remove(townname);
							sender.sendMessage(color("&aTown removed from list."));
						}
					} else {
						sender.sendMessage(color(tipermission));
					}
					return true;
				} else if (args[0].toLowerCase().equals("list")) {
					if (sender.hasPermission("ti.list")) {
						try {
							listnum = Integer.parseInt(args[1]);
							int startfrom = (listnum * 10) - 10;
							if (startfrom > townnames.size()) {
								sender.sendMessage(color(tilistsize));
							} else {
								String listname = getConfig().getString("listname");
								int townnamessize = townnames.size();
								sender.sendMessage(color(listname));
								for (int i = startfrom; i < startfrom + 10; i++) {
									int sidenum = (i + 1);
									if (i > townnames.size())
										break;
									sender.sendMessage(color("&6" + sidenum + ": &2" + townnames.get(i)));
									if (i == townnamessize)
										break;
								}
							}
						} catch (NumberFormatException e) {
							sender.sendMessage(color("&4Argument must be a whole number!"));
						}
					} else {
						sender.sendMessage(color(tipermission));
					}
				} else if (args[0].toLowerCase().equals("info")) {
					if (sender.hasPermission("ti.info")) {
						if (args.length == 2) {
							TI_Data data = tlist.get(args[1].toLowerCase());
							if (data == null) {
								sender.sendMessage(color(tina));
								return true;
							} else {
								Vector location = data.getLocation();
								sender.sendMessage(color("&a " + data.getTName()));
								sender.sendMessage(color("&6- " + "Status: &a" + data.getTStatus()));
								sender.sendMessage(color("&6- " + "Mayor: &a" + data.getMayor()));
								sender.sendMessage(color("&6- " + "Vice Mayor: &a" + data.getVMayor()));
								sender.sendMessage(color("&6- " + "Portal Wing: &a" + data.getWing()));
								if (location != null)
									sender.sendMessage(color("&6- " + "Location: &a X- " + location.getBlockX() + ", Y- " + location.getBlockY() + ", Z- " + location.getBlockZ()));
								else
									sender.sendMessage(color("&6- " + "Location: &anull"));
								sender.sendMessage(color("&6- " + "Plots Available: &a" + data.getPAvail()));
								sender.sendMessage(color("&6- " + "MOTD: &a" + data.getMOTD()));
								sender.sendMessage(color("&6- " + "Rating: &a" + data.getRating() + "%"));
							}
						}
					} else {
						sender.sendMessage(tipermission);
					}
				}
			} else if (args[0].toLowerCase().equals("edit")) {
				TI_Data data = tlist.get(args[1].toLowerCase());
				if (data == null) {
					sender.sendMessage(color(tina));
					return true;
				}
				if (args[2].toLowerCase().equals("mayor")) {
					if (args.length == 4) {
						if (!(sender.hasPermission("ti.admin.edit"))) {
							sender.sendMessage(color(tipermission));
							return true;
						}
						sender.sendMessage(tiedit);
						data.setMayor(args[3]);
					} else {
						sender.sendMessage(color("&cUse as /ti edit <town> mayor <mayor>"));
						return true;
					}
				} else if (args[2].toLowerCase().equals("vmayor")) {
					if (args.length == 4) {
						if (!((sender.hasPermission("ti.basic.edit.vmayor") && (sender.getName() == data.getMayor())) || sender.hasPermission("ti.admin.edit"))) {
							sender.sendMessage(color(tipermission));
							return true;
						}
						sender.sendMessage(tiedit);
						data.setVMayor(args[3]);
					} else {
						sender.sendMessage(color("&cUse as /ti edit <town> vmayor <vmayor>"));
						return true;
					}
				} else if (args[2].toLowerCase().equals("wing")) {
					if (args.length == 4) {
						if (!((sender.hasPermission("ti.basic.edit.wing") && (sender.getName() == data.getMayor())) || sender.hasPermission("ti.admin.edit"))) {
							sender.sendMessage(color(tipermission));
							return true;
						}
						if ((args[3].equalsIgnoreCase(wing1)) || (args[3].equalsIgnoreCase(wing2)) || (args[3].equalsIgnoreCase(wing2)) || (args[3].equalsIgnoreCase(wing4))) {
							sender.sendMessage(tiedit);
							data.setWing(args[3]);
						} else {
							sender.sendMessage(color("&4Argument needs to be: " + wing1 + ", " + wing2 + ", " + wing3 + ", or " + wing4 + "."));
							return true;
						}
					} else {
						sender.sendMessage(color("&cUse as /ti edit <town> wing <wing>"));
						return true;
					}
				} else if (args[2].toLowerCase().equals("location")) {
					if (args.length == 3) {
						if (!((sender.hasPermission("ti.basic.edit.location") && (sender.getName() == data.getMayor())) || sender.hasPermission("ti.admin.edit"))) {
							sender.sendMessage(color(tipermission));
							return true;
						}
						if (!(sender instanceof Player)) {
							sender.sendMessage(color("&cMust use command as a player!"));
							return true;
						}
						sender.sendMessage(tiedit);
						Player psender = (Player) sender;
						Vector vec = psender.getLocation().toVector();
						data.setLocation(vec);
					} else {
						sender.sendMessage(color("&cUse as /ti edit <town> location"));
						return true;
					}
				} else if (args[2].toLowerCase().equals("plots")) {
					if (args.length == 4) {
						if (!((sender.hasPermission("ti.basic.edit.plots") && (sender.getName() == data.getMayor())) || sender.hasPermission("ti.admin.edit"))) {
							sender.sendMessage(color(tipermission));
							return true;
						}
						if ((args[3].equalsIgnoreCase("yes")) || (args[3].equalsIgnoreCase("no")) || (args[3].equalsIgnoreCase("possibly"))) {
							sender.sendMessage(tiedit);
							data.setPAvail(args[3]);
						} else {
							sender.sendMessage(color("&4Argument must be: Yes, No, or Possibly."));
						}
					} else {
						sender.sendMessage(color("&cUse as /ti edit <town> plots <status>"));
						return true;
					}
				} else if (args[2].toLowerCase().equals("status")) {
					if (args.length == 4) {
						if (!(sender.hasPermission("ti.admin.edit"))) {
							sender.sendMessage(color(tipermission));
							return true;
						}
						if ((args[3].equalsIgnoreCase("Village")) || (args[3].equalsIgnoreCase("Town")) || (args[3].equalsIgnoreCase("City"))) {
							sender.sendMessage(tiedit);
							data.setTStatus(args[3]);
						} else {
							sender.sendMessage(color("&4Argument must be: Village, Town, or City."));
						}
					} else {
						sender.sendMessage(color("&cUse as /ti edit <town> status <status>"));
						return true;
					}
				} else if (args[2].toLowerCase().equals("motd")) {
					if (!((sender.hasPermission("ti.basic.edit.motd") && (sender.getName() == data.getMayor())) || sender.hasPermission("ti.admin.edit"))) {
						sender.sendMessage(color(tipermission));
						return true;
					}
					StringBuilder buffer = new StringBuilder();
					for (int i = 3; i < args.length; i++) {
						buffer.append(' ').append(args[i]);
					}
					String motd = buffer.toString();
					data.setMOTD(motd);
					sender.sendMessage(tiedit);
				} else if (args[2].equalsIgnoreCase("rating")) {
					if (!sender.hasPermission("ti.admin.edit"))
						return true;
					try {
						Double.parseDouble(args[3]);
					} catch (NumberFormatException e) {
						sender.sendMessage(color("&cRating must be a number!"));
						return true;
					}
					if ((Double.parseDouble(args[3]) > 100)) {
						sender.sendMessage(color("&cNumber must be below 100!"));
						return true;
					}
					sender.sendMessage(tiedit);
					data.setRating(Double.parseDouble(args[3]));
				} else {
					if (!(sender.hasPermission("ti.admin.edit"))) {
						sender.sendMessage(color(tiedithelpbasic));
					} else {
						sender.sendMessage(color(tiedithelpbasic));
						sender.sendMessage(color(tiedithelpadmin));
					}
				}
			} else {
				sender.sendMessage(color(tihelp));
				if (sender.hasPermission("ti.admin.help"))
					sender.sendMessage(color(tiadminhelp));
			}
		} else {
			sender.sendMessage(color(tina));
		}
		return true;
	}
}
