package me.samsey.ffa.fileapi;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigAccessor {

	private String fileName;
	private JavaPlugin plugin;
	private File configFile;
	private FileConfiguration fileConfiguration;

	public ConfigAccessor(JavaPlugin plugin, String fileName) {
		if (plugin == null) {
			throw new IllegalArgumentException("plugin cannot be null");
		}

		this.plugin = plugin;
		this.fileName = fileName;

		this.configFile = new File(plugin.getDataFolder(), fileName);
	}

	public void reloadConfig() {
		this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);

		InputStream defConfigStream = this.plugin.getResource(this.fileName);

		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
			this.fileConfiguration.setDefaults(defConfig);
		}
	}

	public FileConfiguration getConfig() {
		if (this.fileConfiguration == null) {
			this.reloadConfig();
		}
		return this.fileConfiguration;
	}

	public boolean exists() {
		return this.configFile.exists();
	}

	public void deleteConfig() {
		this.configFile.delete();
	}

	public void saveConfig() {
		if (this.fileConfiguration == null || this.configFile == null) {
			return;
		}

		try {
			this.getConfig().save(this.configFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
		}
	}

	public void saveDefaultConfig() {
		if (!this.configFile.exists()) {
			this.plugin.saveResource(this.fileName, false);
		}
	}

	public File getFile() {
		return this.configFile;
	}
}