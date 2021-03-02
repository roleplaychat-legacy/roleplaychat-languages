package ru.xunto.roleplaychat.languages.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.pol.languages.Language;
import ru.pol.languages.LanguageManager;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.languages.common.permission.PermissionLanguage;
import ru.xunto.roleplaychat.languages.common.permission.PermissionSpeak;
import ru.xunto.roleplaychat.languages.common.permission.PermissionUnderstand;

import java.io.FileNotFoundException;

public class LanguagesCommon {
    private static Logger logger = LogManager.getLogger(LanguagesCommon.class);

    public static boolean canSpeak(ISpeaker speaker, Language language) {
        String generalPermission = PermissionLanguage.getPermissionName(language);
        String speakPermission = PermissionSpeak.getPermissionName(language);

        return speaker.hasPermission(generalPermission) || speaker.hasPermission(speakPermission);
    }

    public static boolean canUnderstand(ISpeaker speaker, Language language) {
        String generalPermission = PermissionLanguage.getPermissionName(language);
        String understandPermission = PermissionUnderstand.getPermissionName(language);

        return speaker.hasPermission(generalPermission) || speaker.hasPermission(understandPermission);
    }

    public static void init(RoleplayChatCore core) throws FileNotFoundException {
        LanguageManager manager = new LanguageManager("./config/languages");

        for (Language language : manager.getLanguages()) {
            core.register(LanguageEndpoint.fromLanguage(RoleplayChatCore.instance, language));
            core.register(new PermissionLanguage(language));
            core.register(new PermissionSpeak(language));
            core.register(new PermissionUnderstand(language));
            logger.info(String.format("Language %s is loaded.", language.getName()));
        }
    }
}
