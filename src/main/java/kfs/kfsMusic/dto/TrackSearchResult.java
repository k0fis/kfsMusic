package kfs.kfsMusic.dto;

public record TrackSearchResult(
        Long id,
        TrackDto track,
        double score
) {}
