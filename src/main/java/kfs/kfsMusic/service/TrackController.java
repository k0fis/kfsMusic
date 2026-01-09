package kfs.kfsMusic.service;

import kfs.kfsMusic.dto.TrackSearchResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackService service;

    public TrackController(TrackService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public List<TrackSearchResult> search(@RequestParam String q) {
        return service.search(q);
    }
}
