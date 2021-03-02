package ru.xunto.roleplaychat.languages.common;

import ru.pol.languages.Language;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.MessageState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LanguageEndpoint extends PrefixMatchEndpoint {
    private final Language language;

    LanguageEndpoint(RoleplayChatCore core, Language language, String... prefixes) {
        super(core, prefixes);
        this.language = language;
    }

    public static LanguageEndpoint fromLanguage(RoleplayChatCore core, Language language) {
        List<String> prefixes = new ArrayList<>(generatePrefixes(language.getName()));
        for (String alias : language.getAlias()) {
            prefixes.addAll(generatePrefixes(alias));
        }

        return new LanguageEndpoint(core, language, prefixes.toArray(new String[0]));
    }

    private static List<String> generatePrefixes(String string) {
        List<String> prefixes = new ArrayList<>();

        String prefix = ":";

        for (char c : string.toCharArray()) {
            prefix += c;
            prefixes.add(prefix);
        }

        return prefixes;
    }

    @Override
    public boolean matchEndpoint(Request request, Environment environment) {
        return LanguagesCommon.canSpeak(request.getRequester(), language) && super
                .matchEndpoint(request, environment);
    }

    @Override
    public String getName() {
        return String.format("язык (%s)", language.getName());
    }

    public void processEndpoint(Request request, Environment environment, Flow fork) {
        environment.setProcessed(true);

        Environment translatedEnvironment = environment.clone();
        MessageState translatedState = translatedEnvironment.getState();

        // Fill new state with translated message
        String text = translatedState.getValue(Environment.TEXT);
        translatedState.setValue(Environment.TEXT, language.translatePhrase(text));
        fork.fork(translatedEnvironment);

        // Fill label
        MessageState state = environment.getState();

        state.setValue(Environment.TEXT, text);
        state.setValue(Environment.LABEL, language.getName());

        Map<String, TextColor> colors = environment.getColors();
        colors.put("default", TextColor.GRAY);

        Set<ISpeaker> recipientsOfTranslation = environment.getRecipients();
        recipientsOfTranslation.removeIf(r -> !LanguagesCommon.canUnderstand(r, language));
    }
}
