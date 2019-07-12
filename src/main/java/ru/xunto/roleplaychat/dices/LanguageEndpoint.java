package ru.xunto.roleplaychat.dices;

import net.minecraft.util.text.TextFormatting;
import ru.pol.languages.Language;
import ru.xunto.roleplaychat.forge.RoleplayChat;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LanguageEndpoint extends PrefixMatchEndpoint {
    private final Language language;

    LanguageEndpoint(Language language, String... prefixes) throws EmptyPrefixException {
        super(prefixes);
        this.language = language;
    }

    public static LanguageEndpoint fromLanguage(Language language) {
        List<String> prefixes = new ArrayList<>();
        String prefix = ":";

        for (char c : language.getName().toCharArray()) {
            prefix += c;
            prefixes.add(prefix);
        }

        Collections.reverse(prefixes);

        try {
            return new LanguageEndpoint(language, prefixes.toArray(new String[0]));
        } catch (EmptyPrefixException e) {
            return null;
        }
    }

    @Override public String getName() {
        return String.format("язык (%s)", language.getName());
    }

    @Override public void processEndpoint(Request request, Environment environment) {
        Environment translatedEnvironment = environment.clone();
        JTwigState translatedState = translatedEnvironment.getState();

        // Fill new state with translated message
        String text = translatedState.getValue(Environment.TEXT);
        translatedState.setValue(Environment.TEXT, language.translatePhrase(text));
        translatedEnvironment.setProcessed(true);
        RoleplayChat.chat.process(request, translatedEnvironment);

        // Fill label
        JTwigState state = environment.getState();

        state.setValue(Environment.TEXT, text);
        state.setValue(Environment.LABEL, language.getName());

        Map<String, TextFormatting> colors = environment.getColors();
        colors.put("default", TextFormatting.GRAY);
    }
}
