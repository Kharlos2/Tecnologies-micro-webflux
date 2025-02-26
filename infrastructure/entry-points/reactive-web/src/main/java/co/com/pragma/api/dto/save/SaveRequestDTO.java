package co.com.pragma.api.dto.save;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveRequestDTO {

    private String name;
    private String description;

}
