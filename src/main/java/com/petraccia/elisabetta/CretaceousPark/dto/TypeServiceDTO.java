package com.petraccia.elisabetta.CretaceousPark.dto;

import com.petraccia.elisabetta.CretaceousPark.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeServiceDTO {
    private Long id;
    private AvailabilityStatus type;
    private List<Long> bookableServiceIds;
    private List<Long> nonBookableServiceIds;
}

