package com.example.demo.jpa;

import com.example.demo.SampleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SampleNameTypeDTO {

    private String name;

    private SampleType type;
}
