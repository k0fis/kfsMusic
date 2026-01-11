package kfs.kfsMusic.utils;

import java.util.List;

public interface KfsMap<E,D> {
    D entityToDto(E carer);

    List<D> entityToDto(List<E> carers);

    E dtoToEntity(D carersDto);

    List<E> dtoToEntity(List<D> lst);
}
