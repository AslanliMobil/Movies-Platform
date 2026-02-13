package org.example.moviesplatform.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class DirectorFilter {
    private String name;
    private String biography;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDateTo;

    // Ölüm tarixinə görə filtr sahələri
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deathDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deathDateTo;
}