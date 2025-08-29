package com.reto.usuarios.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPasswordRequest {
    
    @NotBlank(message = "La nueva Password es obligatoria")
    @Size(min = 8, message = "La Password debe tener al menos 8 caracteres")
    private String nuevaPassword;
}
