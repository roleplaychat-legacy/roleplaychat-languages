package ru.xunto.roleplaychat.languages.common.permission;

import ru.pol.languages.Language;
import ru.xunto.roleplaychat.api.IPermission;

public class PermissionSpeak implements IPermission {
    private Language language;

    public PermissionSpeak(Language language) {
        this.language = language;
    }

    public static String getPermissionName(Language language) {
        return String.format("lang.%s.speak", language.getName());
    }

    @Override
    public String getName() {
        return PermissionSpeak.getPermissionName(language);
    }

    @Override
    public String getDescription() {
        return String.format("Ability to speak %s.", language.getName());
    }
}
