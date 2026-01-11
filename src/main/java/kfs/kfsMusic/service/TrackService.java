package kfs.kfsMusic.service;

import kfs.kfsMusic.dto.TrackSearchResult;
import kfs.kfsMusic.entity.Track;
import kfs.kfsMusic.repo.TrackRepository;
import kfs.kfsMusic.repo.impl.TrackSearchDaoSqlite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final TrackSearchDaoSqlite searchDao;

    public TrackService(
            TrackRepository trackRepository,
            TrackSearchDaoSqlite searchDao
    ) {
        this.trackRepository = trackRepository;
        this.searchDao = searchDao;
    }

    public List<TrackSearchResult> search(String q) {
        return searchDao.search(q, 50);
    }

    public Track save(Track track) {
        return trackRepository.save(track);
    }
}

