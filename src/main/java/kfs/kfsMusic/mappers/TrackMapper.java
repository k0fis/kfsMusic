package kfs.kfsMusic.mappers;

import kfs.kfsMusic.dto.TrackDto;
import kfs.kfsMusic.entity.Track;
import kfs.kfsMusic.utils.KfsMap;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackMapper extends KfsMap<Track, TrackDto> {

}
