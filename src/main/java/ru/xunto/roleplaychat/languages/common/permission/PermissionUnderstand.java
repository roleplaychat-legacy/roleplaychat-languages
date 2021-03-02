package ru.xunto.roleplaychat.languages.common.permission;

import ru.pol.languages.Language;
import ru.xunto.roleplaychat.api.IPermission;

public class PermissionUnderstand implements IPermission {
    private Language language;

    public PermissionUnderstand(Language language) {
        this.language = language;
    }

    public static String getPermissionName(Language language) {
        return String.format("lang.%s.understand", language.getName());
    }

    @Override
    public String getName() {
        return String.format("lang.%s.understand", language.getName());
    }

    @Override
    public String getDescription() {
        return String.format("Ability to speak %s.", language.getName());
    }
}
