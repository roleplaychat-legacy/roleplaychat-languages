package ru.xunto.roleplaychat.dices.common.permission;

import ru.pol.languages.Language;
import ru.xunto.roleplaychat.api.IPermission;

public class PermissionLanguage implements IPermission {
    private Language language;

    public PermissionLanguage(Language language) {
        this.language = language;
    }

    public static String getPermissionName(Language language) {
        return String.format("lang.%s", language.getName());
    }

    @Override
    public String getName() {
        return String.format("lang.%s", language.getName());
    }

    @Override
    public String getDescription() {
        return String.format("Ability to speak and understand %s.", language.getName());
    }
}
