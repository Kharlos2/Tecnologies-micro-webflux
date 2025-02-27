package co.com.pragma.r2dbc.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("public.capacity_technology")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TechnologyCapacityEntity {

    @Id
    private Long id;
    private Long technologyId;
    private Long capacityId;

    public TechnologyCapacityEntity(Long technologyId, Long capacityId) {
        this.technologyId = technologyId;
        this.capacityId = capacityId;
    }
}
