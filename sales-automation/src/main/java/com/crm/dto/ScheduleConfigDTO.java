package com.crm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ScheduleConfigDTO {

    private Long id;
    private String taskName;
    @NotBlank(message = "Cron expression cannot be blank")
    private String cronExpression;

}
