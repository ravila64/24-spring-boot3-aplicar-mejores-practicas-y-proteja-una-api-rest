package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosRegistroMedico(
        // mensaje = se coloca la variable que esta en validation.messages.properties
        @NotBlank(message="{nombre.obligatorio}")
        String nombre,
        @NotBlank(message="{email.obligatorio}")
        @Email(message = "{email.invalido}")
        String email,
        @NotBlank(message="{telefono.obligatorio}") String telefono,
        @NotBlank(message="{documento.invalido}") @Pattern(regexp = "\\d{7,9}") String documento,
        @NotNull(message="{especialidad.obligatorio}") Especialidad especialidad,
        @NotNull(message="{direccion.obligatorio}") @Valid DatosDireccion direccion) {
}
