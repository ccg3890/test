package com.mktback.application.service.mapper;

import com.mktback.application.domain.Category;
import com.mktback.application.domain.User;
import com.mktback.application.service.dto.CategoryDTO;
import com.mktback.application.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    CategoryDTO toDto(Category s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
