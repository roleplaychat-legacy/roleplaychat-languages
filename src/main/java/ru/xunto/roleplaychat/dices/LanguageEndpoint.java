package ru.xunto.roleplaychat.dices;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import ru.pol.languages.Language;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.PrefixMatchEndpoint;
import ru.xunto.roleplaychat.framework.api.Request;
import ru.xunto.roleplaychat.framework.jtwig.JTwigState;
import ru.xunto.roleplaychat.framework.middleware_flow.Flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LanguageEndpoint extends PrefixMatchEndpoint {
    private final Language language;

    LanguageEndpoint(Language language, String... prefixes) throws EmptyPrefixException {
        super(prefixes);
        this.language = language;
    }

    public static LanguageEndpoint fromLanguage(Language language) {
        List<String> prefixes = new ArrayList<>(generatePrefixes(language.getName()));
        for (String alias : language.getAlias()) {
            prefixes.addAll(generatePrefixes(alias));
        }

        try {
            return new LanguageEndpoint(language, prefixes.toArray(new String[0]));
        } catch (EmptyPrefixException e) {
            return null;
        }
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

    @Override public boolean matchEndpoint(Request request, Environment environment) {
        return RoleplayChatLanguages.canSpeak(request.getRequester(), language) && super
            .matchEndpoint(request, environment);
    }

    @Override public String getName() {
        return String.format("язык (%s)", language.getName());
    }

    public void processEndpoint(Request request, Environment environment, Flow fork) {
        environment.setProcessed(true);

        Environment translatedEnvironment = environment.clone();
        JTwigState translatedState = translatedEnvironment.getState();

        // Fill new state with translated message
        String text = translatedState.getValue(Environment.TEXT);
        translatedState.setValue(Environment.TEXT, language.translatePhrase(text));
        fork.fork(translatedEnvironment);

        // Fill label
        JTwigState state = environment.getState();

        state.setValue(Environment.TEXT, text);
        state.setValue(Environment.LABEL, language.getName());

        Map<String, TextFormatting> colors = environment.getColors();
        colors.put("default", TextFormatting.GRAY);

        Set<EntityPlayer> recipientsOfTranslation = environment.getRecipients();
        recipientsOfTranslation.removeIf(r -> !RoleplayChatLanguages.canUnderstand(r, language));
    }
}
