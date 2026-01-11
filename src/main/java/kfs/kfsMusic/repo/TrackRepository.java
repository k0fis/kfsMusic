package kfs.kfsMusic.repo;

import kfs.kfsMusic.entity.Album;
import kfs.kfsMusic.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findByPath(String path);

    List<Track> findByAlbum(Album album);

    @Query("SELECT t FROM Track t LEFT JOIN FETCH t.tags WHERE t.id IN :ids")
    List<Track> findAllWithTagsByIdIn(@Param("ids") List<Long> ids);

}
