package kfs.kfsMusic.repo;

import kfs.kfsMusic.dto.TrackSearchResult;
import kfs.kfsMusic.utils.FtsQueryNormalizer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrackSearchDao {

    private final JdbcTemplate jdbcTemplate;

    public TrackSearchDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TrackSearchResult> searchNormalize(String query, int limit) {
        String ftsQuery = FtsQueryNormalizer.normalize(query);
        if (ftsQuery.isEmpty()) {
            return List.of();
        }
        return search(ftsQuery, limit);
    }

    public List<TrackSearchResult> search(String query, int limit) {
        String sql = """
            SELECT
              t.id,
              t.title,
              a.name AS artist,
              al.title AS album,
              t.genre,
              t.note,
              bm25(track_fts) AS score
            FROM track t
            JOIN album al ON al.id = t.album_id
            JOIN artist a ON a.id = al.artist_id
            JOIN track_fts fts ON fts.rowid = t.id
            WHERE track_fts MATCH ?
            ORDER BY score
            LIMIT ?
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new TrackSearchResult(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getString("genre"),
                        rs.getString("note"),
                        rs.getDouble("score")
                ),
                query,
                limit
        );
    }
}

