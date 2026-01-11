package kfs.kfsMusic.mappers;

import kfs.kfsMusic.dto.ArtistDto;
import kfs.kfsMusic.entity.Artist;
import kfs.kfsMusic.utils.KfsMap;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper extends KfsMap<Artist, ArtistDto> {
}
