package kfs.kfsMusic.repo;

import kfs.kfsMusic.entity.Album;
import kfs.kfsMusic.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByArtist(Artist artist);

    Optional<Album> findByArtistAndTitle(Artist artist, String title);
}

