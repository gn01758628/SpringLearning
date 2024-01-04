package com.example.demo.jpa;

import com.example.demo.SampleType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;

@Data
public class Sample {

    private Long id;
    private String name;
    private BigDecimal amount;
    private SampleType type;
    private LocalDateTime t;
    private LocalDate d;
    private YearMonth ym;
    private Year y;

}
