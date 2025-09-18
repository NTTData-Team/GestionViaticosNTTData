package com.nttdata.viatico_ms.client.dto;

import java.util.List;

public record GastoBatchCreateDTO(List<GastoItemCreateDTO> items) {}
