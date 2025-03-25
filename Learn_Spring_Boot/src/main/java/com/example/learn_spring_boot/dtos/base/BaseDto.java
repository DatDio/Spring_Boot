package com.example.learn_spring_boot.dtos.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class BaseDto {
    private Long id;
}
