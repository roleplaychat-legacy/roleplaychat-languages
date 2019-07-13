package ru.xunto.roleplaychat.dices;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.Logger;
import ru.pol.languages.Language;
import ru.pol.languages.LanguageManager;
import ru.xunto.roleplaychat.forge.RoleplayChat;

import java.io.FileNotFoundException;

@Mod(modid = RoleplayChatLanguages.MODID, name = RoleplayChatLanguages.NAME, version = RoleplayChatLanguages.VERSION, acceptableRemoteVersions = "*", dependencies = "required-after:roleplaychat")
public class RoleplayChatLanguages {
    public static final String MODID = "@MODID@";
    public static final String NAME = "@MODID@";
    public static final String VERSION = "@VERSION@";

    private static Logger logger;

    public static String getSpeakPermission(Language language) {
        return String.format("lang.%s.speak", language.getName());
    }

    public static String getUnderstandPermission(Language language) {
        return String.format("lang.%s.understand", language.getName());
    }

    private static void registerPermission(Language language) {
        String languageName = language.getName();

        String nodeSpeak = RoleplayChatLanguages.getSpeakPermission(language);
        String nodeUnderstand = RoleplayChatLanguages.getUnderstandPermission(language);

        PermissionAPI.registerNode(nodeSpeak, DefaultPermissionLevel.OP,
            String.format("Ability to speak language: %s", languageName));
        PermissionAPI.registerNode(nodeUnderstand, DefaultPermissionLevel.OP,
            String.format("Ability to understand language: %s", languageName));
    }

    @Mod.EventHandler public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler public void startServer(FMLServerStartingEvent event) {
        LanguageManager manager;
        try {
            manager = new LanguageManager("./config/languages");
            for (Language language : manager.getLanguages()) {
                logger.info(String.format("Language %s loaded", language.getName()));
                RoleplayChat.chat.register(LanguageEndpoint.fromLanguage(language));
                RoleplayChatLanguages.registerPermission(language);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
