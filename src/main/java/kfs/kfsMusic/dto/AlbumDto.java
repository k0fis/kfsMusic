package kfs.kfsMusic.dto;

import kfs.kfsMusic.entity.Artist;
import lombok.Data;

@Data
public class AlbumDto {
    private Long id;

    private ArtistDto artist;

    private String title;

    private Integer year;

}
