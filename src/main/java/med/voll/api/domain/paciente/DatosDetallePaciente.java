package med.voll.api.domain.paciente;

import med.voll.api.domain.direccion.Direccion;

public record DatosDetallePaciente(
      String nombre,
      String email,
      String telefono,
      String documento,
      Direccion direccion) {

    public DatosDetallePaciente(Paciente paciente) {
        this(paciente.getNombre(),
              paciente.getEmail(),
              paciente.getTelefono(),
              paciente.getDocumento(),
              paciente.getDireccion());
    }
}
