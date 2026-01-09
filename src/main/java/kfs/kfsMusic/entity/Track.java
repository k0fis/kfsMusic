package kfs.kfsMusic.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "track",
        indexes = {
                @Index(name = "idx_track_note", columnList = "note")
        }
)
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(nullable = false)
    private String title;

    @Column(name = "track_no")
    private Integer trackNo;

    private String genre;

    private Integer duration;

    @Column(nullable = false, unique = true)
    private String path;

    private String note;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(Integer trackNo) {
        this.trackNo = trackNo;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
