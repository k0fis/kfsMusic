package kfs.kfsMusic.dto;

public record TrackSearchResult(
        Long id,
        String title,
        String artist,
        String album,
        String genre,
        String note,
        double score
) {}
