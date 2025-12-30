package org.example.moviesplatform.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActorFilter { // DirectorFilter də eyni ola bilər
    private String name;

    private Integer birthYearFrom;
    private Integer birthYearTo;
}
