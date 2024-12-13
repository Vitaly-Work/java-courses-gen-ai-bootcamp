package com.epam.training.gen.ai.service.plugins;

import java.time.LocalDate;
import java.time.Period;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgeCalculatorPlugin {

    @DefineKernelFunction(name = "calculate_age", description = "Calculate age from given birth year, month, day")
    public String calculateAge(
        @KernelFunctionParameter(name = "year", description = "Year of birth as number") int year,
        @KernelFunctionParameter(name = "month", description = "Month of birth as number") int month,
        @KernelFunctionParameter(name = "day", description = "Day of birth as number") int day) {

        log.info("Function 'calculate_age' called with arguments: {}, {}, {}", year, month, day);

        final var birthDate = LocalDate.of(year, month, day);
        final var period = Period.between(birthDate, LocalDate.now());

        return String.format(
            "Calculated age: %d years, %d months, and %d days",
            period.getYears(),
            period.getMonths(),
            period.getDays()
        );
    }
}
