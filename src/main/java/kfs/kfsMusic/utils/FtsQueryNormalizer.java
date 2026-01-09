package kfs.kfsMusic.utils;

import java.util.Locale;
import java.util.regex.Pattern;

public final class FtsQueryNormalizer {

    private static final Pattern INVALID_CHARS =
            Pattern.compile("[\"'\\\\*:+\\-()\\[\\]{}<>~]");

    private static final Pattern WHITESPACE =
            Pattern.compile("\\s+");

    private FtsQueryNormalizer() {
    }

    public static String normalize(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        // lower-case (SQLite FTS je case-insensitive, ale sjednotíme)
        String q = input.toLowerCase(Locale.ROOT);

        // nahradit problematické znaky mezerou
        q = INVALID_CHARS.matcher(q).replaceAll(" ");

        // normalizace whitespace
        q = WHITESPACE.matcher(q).replaceAll(" ").trim();

        if (q.isEmpty()) {
            return "";
        }

        // rozdělit na tokeny
        String[] tokens = q.split(" ");

        // složit explicitní AND dotaz
        return String.join(" AND ", tokens);
    }
}

