package edu.ucne.registroocupaciones.domain.usecase.horaextra

import edu.ucne.registroocupaciones.domain.usecase.empleado.ValidationResult
import java.time.LocalDate

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validarEmpleado(empleadoId: String): ValidationResult {
    return when{
        empleadoId.isBlank() -> ValidationResult(false, "El empleado es obligatorio")
        else -> ValidationResult(true)
    }
}

fun validarFecha(fecha: LocalDate): ValidationResult{
    return when{
        fecha.isAfter(LocalDate.now()) -> ValidationResult(false, "La fecha de ingreso debe ser menor o igual a la actual")
        else -> ValidationResult(true)
    }
}

fun validarCantidadHoras(cantidadHoras: String): ValidationResult{
    return when{
        cantidadHoras.isBlank() -> ValidationResult(false, "La cantidad de horas es obligatoria")
        cantidadHoras.toIntOrNull() == null -> ValidationResult(false, "La cantidad de horas debe ser un numero")
        cantidadHoras.toInt() <= 0 -> ValidationResult(false, "La cantidad de horas debe ser mayor que 0")
        else -> ValidationResult(true)
    }
}

fun validarTipoHoraExtra(tipoHoraExtra: String): ValidationResult{
    return when{
        tipoHoraExtra.isBlank() -> ValidationResult(false, "El tipo de hora extra es obligatoria")
        else -> ValidationResult(true)
    }
}