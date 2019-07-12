package ru.xunto.roleplaychat.dices;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import ru.pol.languages.Language;
import ru.pol.languages.LanguageManager;
import ru.xunto.roleplaychat.forge.RoleplayChat;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

@Mod(modid = RoleplayChatLanguages.MODID, name = RoleplayChatLanguages.NAME, version = RoleplayChatLanguages.VERSION, acceptableRemoteVersions = "*", dependencies = "required-after:roleplaychat")
public class RoleplayChatLanguages {
    public static final String MODID = "@MODID@";
    public static final String NAME = "@MODID@";
    public static final String VERSION = "@VERSION@";

    @Mod.EventHandler public void startServer(FMLServerStartingEvent event) {
        LanguageManager manager = null;
        try {
            manager = new LanguageManager("./config/languages");
            for (Language language : manager.getLanguages()) {
                System.out.println(language.getName());
                RoleplayChat.chat.register(LanguageEndpoint.fromLanguage(language));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
