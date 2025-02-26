package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.save.SaveRequestDTO;
import co.com.pragma.api.dto.save.SaveResponseDTO;
import co.com.pragma.model.technology.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ISaveMapper {

    Technology requestToModel(SaveRequestDTO saveRequestDTO);

    SaveResponseDTO modelToResponse(Technology technologyModel);

}
