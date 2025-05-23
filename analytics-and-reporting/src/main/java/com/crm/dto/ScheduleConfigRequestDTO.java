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
public class ScheduleConfigRequestDTO {

//    @Schema(name = "id", description = "Autogenerated value, should not be added.", defaultValue = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED, hidden = true)
    private Long id;
//    @Schema(name = "taskName", description = "There is only one task, i.e. SendNotification", defaultValue = "SendNotification", requiredMode = Schema.RequiredMode.NOT_REQUIRED, hidden = true)
    private String taskName;
    @NotBlank(message = "Cron expression cannot be blank")
//    @Schema(name = "cronExpression", description = "Should be a valid Cron Expression", defaultValue = "* * * * * *",requiredMode = Schema.RequiredMode.REQUIRED)
    private String cronExpression;

}