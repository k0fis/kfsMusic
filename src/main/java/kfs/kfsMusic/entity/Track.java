package kfs.kfsMusic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "track", indexes = {@Index(name = "idx_track_note", columnList = "note") })
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

    private String comment;

    private String note;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Tag> tags;

}
