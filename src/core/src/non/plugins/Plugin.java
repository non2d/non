package non.plugins;

import non.Non;
import non.plugins.internal._init;
import java.util.HashMap;
import java.util.Set;

public abstract class Plugin {
    public String author()         { return "Unknown"; }
    public String license()        { return "Public Domain"; }
    public String description()    { return "Not specified."; }
    public String[] dependencies() { return null; }
    
    public Plugin() { load(this); }
    public void updatePluginBefore() { }
    public void updatePluginAfter() { }
    public void loadPlugin() { }
    public void unloadPlugin() { }
    
    private static HashMap<String, Plugin> plugins = new HashMap<String, Plugin>();
    
    public static void load(Plugin plugin) {
        String name = plugin.getClass().getSimpleName();
        String subStr = name.substring(0, 1);
        name = name.replaceFirst(subStr, subStr.toLowerCase());
        
        Non.log("Name", name);
        Non.log("Author", plugin.author());
        Non.log("License", plugin.license());
        Non.log("Description", plugin.description());
        
        String[] depArray = plugin.dependencies();

        if (depArray != null) {
            String dependencies = "";
            for(String dependency: depArray) dependencies += dependency + ", ";
            Non.log("Dependencies", dependencies);   
        } else {
            Non.log("Dependencies", "None");
        }
        
        plugins.put(name, plugin);
        Non.script.put(name, plugin);
    }
    
    public static void loadAll() {
        Non.log("PluginManager", "Loading plugins...");
        new _init();
        for(Plugin plugin: plugins.values()) plugin.loadPlugin();
    }
    
    public static Plugin get(String name) {
        return plugins.get(name);
    }
    
    public static void updateBefore() { for(Plugin plugin: plugins.values()) plugin.updatePluginBefore(); }
    public static void updateAfter() { for(Plugin plugin: plugins.values()) plugin.updatePluginAfter(); }
    public static void dispose() { for(Plugin plugin: plugins.values()) plugin.unloadPlugin(); }
}