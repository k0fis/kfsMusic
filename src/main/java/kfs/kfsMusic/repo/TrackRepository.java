package kfs.kfsMusic.repo;

import kfs.kfsMusic.entity.Album;
import kfs.kfsMusic.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findByPath(String path);

    List<Track> findByAlbum(Album album);
}
