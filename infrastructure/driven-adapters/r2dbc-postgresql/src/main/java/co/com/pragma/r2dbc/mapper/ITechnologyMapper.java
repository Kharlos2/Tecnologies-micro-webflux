package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.technology.Technology;
import co.com.pragma.r2dbc.entities.TechnologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITechnologyMapper {

    TechnologyEntity toEntity(Technology technologyModel);

    Technology toModel(TechnologyEntity technologyEntity);

}
