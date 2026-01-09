package kfs.kfsMusic;

import jakarta.transaction.Transactional;
import kfs.kfsMusic.dto.TrackSearchResult;
import kfs.kfsMusic.entity.Album;
import kfs.kfsMusic.entity.Artist;
import kfs.kfsMusic.entity.Track;
import kfs.kfsMusic.repo.AlbumRepository;
import kfs.kfsMusic.repo.ArtistRepository;
import kfs.kfsMusic.repo.TrackRepository;
import kfs.kfsMusic.repo.TrackSearchDao;
import kfs.kfsMusic.utils.FtsQueryNormalizer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class KfsMusicApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TrackSearchDao searchDao;

    @BeforeEach
    void setup() {
        trackRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();

        // insert artist, album, track
        Artist artist = new Artist();
        artist.setName("Radiohead");
        artist = artistRepository.saveAndFlush(artist);

        Album album = new Album();
        album.setTitle("OK Computer");
        album.setArtist(artist);
        album = albumRepository.saveAndFlush(album);

        Track track = new Track();
        track.setAlbum(album);
        track.setTitle("Paranoid Android");
        track.setPath("/music/radiohead/paranoid.mp3");
        track.setNote("favorite live");
        trackRepository.saveAndFlush(track);
    }

    @Test
    void testFulltextSearch() {
        List<TrackSearchResult> results =
                searchDao.search(FtsQueryNormalizer.normalize("paranoid"), 10);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).title()).isEqualTo("Paranoid Android");
    }

    @Test
    void testNormalizedSearch() {
        String userQuery = "paranoid (live)";
        List<TrackSearchResult> results =
                searchDao.search(FtsQueryNormalizer.normalize(userQuery), 10);

        assertThat(results).hasSize(1);
    }

    @Test
    void testNoResults() {
        List<TrackSearchResult> results =
                searchDao.search(FtsQueryNormalizer.normalize("nonexistent"), 10);

        assertThat(results).isEmpty();
    }
}
