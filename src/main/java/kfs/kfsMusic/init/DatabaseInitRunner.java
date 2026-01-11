package kfs.kfsMusic.init;

import kfs.kfsMusic.utils.ScriptSplitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("sqlite")
@Component
public class DatabaseInitRunner implements ApplicationRunner {

    private final JdbcTemplate jdbc;

    public DatabaseInitRunner(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {

        ScriptSplitter
                .parseClasspath("db/schema-sqlite.sql", "--- kfsSplit ---")
                .peek(s->log.debug("schema sql: \n{}\n", s))
                .forEach(jdbc::execute);
    }
}

