package co.com.pragma.api.dto.capacity;

import lombok.Data;

import java.util.List;

@Data
public class CheckTechnologiesRequestDTO {

    private List<Long> technologiesIds;

}
