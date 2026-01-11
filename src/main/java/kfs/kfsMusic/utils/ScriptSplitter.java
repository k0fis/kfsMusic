package kfs.kfsMusic.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ScriptSplitter {

    public static Stream<String> parseClasspath(
            String classpathLocation,
            String delimiterLine
    ) {
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(classpathLocation);

        if (is == null) {
            throw new IllegalArgumentException(
                    "SQL script not found on classpath: " + classpathLocation
            );
        }

        return parse(is, delimiterLine);
    }

    public static Stream<String> parse(InputStream input, String delimiterLine) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();

                if (trimmed.equals(delimiterLine)) {
                    flush(statements, current);
                    continue;
                }

                if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                    continue;
                }

                current.append(line).append('\n');
            }

            flush(statements, current);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse SQL script", e);
        }

        return statements.stream();
    }

    private static void flush(List<String> statements, StringBuilder current) {
        String sql = current.toString().trim();
        if (!sql.isEmpty()) {
            statements.add(sql);
        }
        current.setLength(0);
    }
}
