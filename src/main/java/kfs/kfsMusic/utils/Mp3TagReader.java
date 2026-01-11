package kfs.kfsMusic.utils;


import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;

import java.nio.file.Path;

public class Mp3TagReader {

    public record TrackData (
            String title,
    String artist,
    String album,
    String genre,
    String comment,
            String path
    ){

    }

    public static TrackData read(Path mp3Path) {
        try {
            AudioFile f = AudioFileIO.read(mp3Path.toFile());
            Tag tag = f.getTag();

            String title = tag.getFirst(org.jaudiotagger.tag.FieldKey.TITLE);
            String artist = tag.getFirst(org.jaudiotagger.tag.FieldKey.ARTIST);
            String album = tag.getFirst(org.jaudiotagger.tag.FieldKey.ALBUM);
            String genre = tag.getFirst(org.jaudiotagger.tag.FieldKey.GENRE);
            String comment = tag.getFirst(org.jaudiotagger.tag.FieldKey.COMMENT);

            return new TrackData(title, artist, album, genre, comment, mp3Path.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error reading MP3: " + mp3Path, e);
        }
    }
}
