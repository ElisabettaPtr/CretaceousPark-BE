package com.petraccia.elisabetta.CretaceousPark.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayDTO {
    private LocalDate date;
    private String localName;
    private String name;
}
