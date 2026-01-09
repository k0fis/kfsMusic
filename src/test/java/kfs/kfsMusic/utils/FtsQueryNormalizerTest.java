package kfs.kfsMusic.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FtsQueryNormalizerTest {

    @Test
    void testSimpleQuery() {
        String input = "radiohead";
        String normalized = FtsQueryNormalizer.normalize(input);
        assertThat(normalized).isEqualTo("radiohead");
    }

    @Test
    void testMultipleWords() {
        String input = "radiohead live";
        String normalized = FtsQueryNormalizer.normalize(input);
        assertThat(normalized).isEqualTo("radiohead AND live");
    }

    @Test
    void testSpecialCharacters() {
        String input = "radiohead (live) 1997-remaster";
        String normalized = FtsQueryNormalizer.normalize(input);
        assertThat(normalized).isEqualTo("radiohead AND live AND 1997 AND remaster");
    }

    @Test
    void testQuotesAndStars() {
        String input = "\"ok computer\"*";
        String normalized = FtsQueryNormalizer.normalize(input);
        assertThat(normalized).isEqualTo("ok AND computer");
    }

    @Test
    void testEmptyOrNull() {
        assertThat(FtsQueryNormalizer.normalize(null)).isEqualTo("");
        assertThat(FtsQueryNormalizer.normalize("")).isEqualTo("");
        assertThat(FtsQueryNormalizer.normalize("    ")).isEqualTo("");
    }

    @Test
    void testWhitespaceNormalization() {
        String input = "  radiohead   live  ";
        String normalized = FtsQueryNormalizer.normalize(input);
        assertThat(normalized).isEqualTo("radiohead AND live");
    }
}

