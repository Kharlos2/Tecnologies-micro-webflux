package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.capacity.CheckTechnologiesRequestDTO;
import co.com.pragma.api.dto.save.SaveRequestDTO;
import co.com.pragma.api.dto.save.SaveResponseDTO;
import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITechnologiesMapper {

    Technology requestToModel(SaveRequestDTO saveRequestDTO);

    SaveResponseDTO modelToResponse(Technology technologyModel);

    CapacityWithTechnologies checkToModel (CheckTechnologiesRequestDTO checkTechnologiesRequestDTO);

}
