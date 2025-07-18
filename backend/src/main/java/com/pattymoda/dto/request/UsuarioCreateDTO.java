package com.pattymoda.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UsuarioCreateDTO {
    
    @Size(max = 20, message = "El código de empleado no puede exceder 20 caracteres")
    private String codigoEmpleado;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&].*$", 
             message = "La contraseña debe contener al menos una minúscula, una mayúscula, un número y un carácter especial")
    private String password;
    
    @NotNull(message = "El rol es obligatorio")
    private Long rolId;
    
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "El teléfono debe tener un formato válido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String direccion;
    
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;
    
    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private LocalDate fechaIngreso;
    
    @DecimalMin(value = "0.0", message = "El salario base no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El salario base debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal salarioBase;
    
    @DecimalMin(value = "0.0", message = "El porcentaje de comisión no puede ser negativo")
    @DecimalMax(value = "100.0", message = "El porcentaje de comisión no puede exceder 100%")
    @Digits(integer = 3, fraction = 2, message = "El porcentaje de comisión debe tener máximo 3 dígitos enteros y 2 decimales")
    private BigDecimal comisionPorcentaje = BigDecimal.ZERO;
    
    @Size(max = 255, message = "La URL de foto de perfil no puede exceder 255 caracteres")
    private String fotoPerfil;
    
    private Boolean activo = true;
}