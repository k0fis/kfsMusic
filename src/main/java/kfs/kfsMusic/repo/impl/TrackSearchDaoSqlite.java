package kfs.kfsMusic.repo.impl;

import kfs.kfsMusic.dto.TrackDto;
import kfs.kfsMusic.dto.TrackSearchResult;
import kfs.kfsMusic.entity.Track;
import kfs.kfsMusic.mappers.TrackMapper;
import kfs.kfsMusic.repo.TrackRepository;
import kfs.kfsMusic.repo.TrackSearchDao;
import kfs.kfsMusic.utils.FtsQueryNormalizer;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TrackSearchDaoSqlite implements TrackSearchDao {

    private final JdbcTemplate jdbcTemplate;

    private final TrackMapper trackMapper;

    private final TrackRepository trackRepository;

    public List<TrackSearchResult> searchNormalize(String query, int limit) {
        String ftsQuery = FtsQueryNormalizer.normalize(query);
        if (ftsQuery.isEmpty()) {
            return List.of();
        }
        return search(ftsQuery, limit);
    }

    public List<TrackSearchResult> search(String query, int limit) {
        String sql = """
            SELECT t.id, bm25(track_fts) AS score
            FROM track t
            JOIN track_fts fts ON fts.rowid = t.id
            WHERE track_fts MATCH ?
            ORDER BY score
            LIMIT ?
        """;

        List<IdScore> idScores = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new IdScore(
                        rs.getLong("id"),
                        rs.getDouble("score")
                ),
                query,
                limit
        );

        if (idScores.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> ids = idScores.stream()
                .map(IdScore::id)
                .toList();

        List<Track> tracks = trackRepository.findAllWithTagsByIdIn(ids);

        Map<Long, TrackDto> dtos = tracks.stream()
                .collect(Collectors.toMap(
                        Track::getId,
                        trackMapper::entityToDto
                ));

        return idScores.stream()
                .map(is -> new TrackSearchResult(
                        is.id(),
                        dtos.get(is.id()),
                        is.score()
                ))
                .toList();
    }

    private record IdScore(Long id, double score) {}
}

