package com.petraccia.elisabetta.CretaceousPark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NonBookableDTO {
    private Long id;
    private String name;
    private Long typeServiceId;
}
