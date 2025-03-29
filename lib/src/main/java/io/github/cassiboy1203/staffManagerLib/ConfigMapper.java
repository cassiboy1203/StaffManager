package io.github.cassiboy1203.staffManagerLib;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

public abstract class ConfigMapper <T> {
    protected FileConfiguration config;

    public ConfigMapper(){

    }

    public T map(){
    }

    public abstract List<T> mapList();

    public abstract T mapSection(ConfigurationSection section);
}
