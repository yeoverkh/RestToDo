package ua.yehor.rest.todo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record TaskDTO(
        @NotNull
        @Length(min = 4, max = 25, message = "{errors.task.name.length}")
        @Pattern(regexp = "[a-zA-Z ]+", message = "{errors.task.name.pattern}")
        String name,

        @NotNull
        @Length(min = 5, max = 255, message = "{errors.task.description.length}")
        @Pattern(regexp = "[a-zA-Z0-9.,:; ]+", message = "{errors.task.description.pattern}")
        String description,

        @NotNull LocalDateTime endDateTime) {
}
