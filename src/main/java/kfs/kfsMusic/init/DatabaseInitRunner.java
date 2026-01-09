package kfs.kfsMusic.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitRunner implements ApplicationRunner {

    private final JdbcTemplate jdbc;

    public DatabaseInitRunner(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (true)
            return;

        // FTS5 tabulka
        jdbc.execute("""
            CREATE VIRTUAL TABLE IF NOT EXISTS track_fts USING fts5(
                title, artist, album, genre, note,
                content='track',
                content_rowid='id'
            );
        """);

        // DROP triggers if exists
        jdbc.execute("DROP TRIGGER IF EXISTS track_ai;");
        jdbc.execute("DROP TRIGGER IF EXISTS track_au;");
        jdbc.execute("DROP TRIGGER IF EXISTS track_ad;");

        // AFTER DELETE trigger
        jdbc.execute("""
            CREATE TRIGGER track_ad
            AFTER DELETE ON track
            BEGIN
              DELETE FROM track_fts WHERE rowid = OLD.id;
            END;
        """);

        // AFTER INSERT trigger
        jdbc.execute("""
            CREATE TRIGGER track_ai
            AFTER INSERT ON track
            BEGIN
              INSERT INTO track_fts(rowid, title, artist, album, genre, note)
              VALUES (
                NEW.id,
                NEW.title,
                (SELECT name FROM artist WHERE id = (SELECT artist_id FROM album WHERE id = NEW.album_id)),
                (SELECT title FROM album WHERE id = NEW.album_id),
                NEW.genre,
                NEW.note
              );
            END;
        """);

        // AFTER UPDATE trigger
        jdbc.execute("""
            CREATE TRIGGER track_au
            AFTER UPDATE ON track
            BEGIN
              DELETE FROM track_fts WHERE rowid = OLD.id;

              INSERT INTO track_fts(rowid, title, artist, album, genre, note)
              VALUES (
                NEW.id,
                NEW.title,
                (SELECT name FROM artist WHERE id = (SELECT artist_id FROM album WHERE id = NEW.album_id)),
                (SELECT title FROM album WHERE id = NEW.album_id),
                NEW.genre,
                NEW.note
              );
            END;
        """);
    }
}

