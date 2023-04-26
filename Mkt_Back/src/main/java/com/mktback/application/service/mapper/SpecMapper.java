package com.mktback.application.service.mapper;

import com.mktback.application.domain.Spec;
import com.mktback.application.service.dto.SpecDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Spec} and its DTO {@link SpecDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecMapper extends EntityMapper<SpecDTO, Spec> {}
