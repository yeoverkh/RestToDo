package ua.yehor.rest.todo.dto;

import jakarta.validation.constraints.NotNull;

public record LocalizedUserDTO(@NotNull String locale, @NotNull String login) {
}
