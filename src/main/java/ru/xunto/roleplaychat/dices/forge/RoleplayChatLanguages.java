package ru.xunto.roleplaychat.dices.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.dices.common.LanguagesCommon;

import java.io.FileNotFoundException;

@Mod(modid = RoleplayChatLanguages.MODID, name = RoleplayChatLanguages.NAME, version = RoleplayChatLanguages.VERSION, acceptableRemoteVersions = "*", dependencies = "required-after:roleplaychat")
public class RoleplayChatLanguages {
    public static final String MODID = "@MODID@";
    public static final String NAME = "@MODID@";
    public static final String VERSION = "@VERSION@";

    private static Logger logger;

    @Mod.EventHandler public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        try {
            LanguagesCommon.init(RoleplayChatCore.instance);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
