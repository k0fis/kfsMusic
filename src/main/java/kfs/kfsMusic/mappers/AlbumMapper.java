package kfs.kfsMusic.mappers;

import kfs.kfsMusic.dto.AlbumDto;
import kfs.kfsMusic.entity.Album;
import kfs.kfsMusic.utils.KfsMap;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumMapper extends KfsMap<Album, AlbumDto> {
}
