package co.com.pragma.model.technology.models;

import java.util.List;

public class CapacityWithTechnologies {

    private Long capacityId ;
    private List<Long> technologiesIds;

    public CapacityWithTechnologies(Long capacityId, List<Long> technologiesIds) {
        this.capacityId = capacityId;
        this.technologiesIds = technologiesIds;
    }

    public CapacityWithTechnologies() {
    }

    public Long getCapacityId() {
        return capacityId;
    }

    public void setCapacityId(Long capacityId) {
        this.capacityId = capacityId;
    }

    public List<Long> getTechnologiesIds() {
        return technologiesIds;
    }

    public void setTechnologiesIds(List<Long> technologiesIds) {
        this.technologiesIds = technologiesIds;
    }
}
