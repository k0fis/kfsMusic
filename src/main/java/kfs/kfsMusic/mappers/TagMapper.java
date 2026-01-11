package kfs.kfsMusic.mappers;

import kfs.kfsMusic.dto.TagDto;
import kfs.kfsMusic.entity.Tag;
import kfs.kfsMusic.utils.KfsMap;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper extends KfsMap<Tag, TagDto> {
}
