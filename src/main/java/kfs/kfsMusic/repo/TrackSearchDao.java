package kfs.kfsMusic.repo;

import kfs.kfsMusic.dto.TrackSearchResult;

import java.util.List;

public interface TrackSearchDao {

    List<TrackSearchResult> searchNormalize(String query, int limit);
    List<TrackSearchResult> search(String query, int limit);
}
