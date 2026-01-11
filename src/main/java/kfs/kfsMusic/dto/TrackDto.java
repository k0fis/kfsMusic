package kfs.kfsMusic.dto;

import lombok.Data;
import java.util.List;

@Data
public class TrackDto {
    private Long id;
    private String title;
    private Integer trackNo;
    private String genre;
    private Integer duration;
    private String path;
    private String comment;
    private String note;
    private AlbumDto album;
    private List<TagDto> tags;
}