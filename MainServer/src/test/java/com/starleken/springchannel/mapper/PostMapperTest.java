package com.starleken.springchannel.mapper;

import com.starleken.springchannel.core.utils.dtoUtils.PostDtoUtils;
import com.starleken.springchannel.core.utils.entityUtils.PostEntityUtils;
import com.starleken.springchannel.dto.post.PostCreateDto;
import com.starleken.springchannel.dto.post.PostFullDto;
import com.starleken.springchannel.entity.PostEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.starleken.springchannel.core.equals.PostEqualsUtils.*;
import static com.starleken.springchannel.core.utils.dtoUtils.PostDtoUtils.*;
import static com.starleken.springchannel.core.utils.entityUtils.PostEntityUtils.*;

@ExtendWith(MockitoExtension.class)
public class PostMapperTest {

    private PostMapper mapper = Mappers.getMapper(PostMapper.class);

    @Test
    void mapToDto_happyPath() {
        //given
        PostEntity toMap = generatePostWithId();

        //when
        PostFullDto mapped = mapper.mapToDto(toMap);

        //then
        Assertions.assertNotNull(mapped);
        EqualEntityAndFullDto(toMap, mapped);
    }

    @Test
    void mapToEntityFromCreateDto_happyPath() {
        //given
        PostCreateDto toMap = generateCreateDto(1L);

        //when
        PostEntity mapped = mapper.mapToEntity(toMap);

        //then
        Assertions.assertNotNull(mapped);
        EqualEntityAndCreateDto(mapped, toMap);
    }
}
