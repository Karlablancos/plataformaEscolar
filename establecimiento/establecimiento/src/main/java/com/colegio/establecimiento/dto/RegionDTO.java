package com.colegio.establecimiento.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegionDTO {

    private Integer idRegion;
    private String nombreRegion;
    private List<ComunaDTO> comunas;
}
