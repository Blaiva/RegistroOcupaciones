package edu.ucne.registroocupaciones.domain.usecase.ocupacion

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validarDescripcion(descripcion: String, ocupacionesExistentes: List<String>): ValidationResult{
    return when{
        descripcion.isBlank() -> ValidationResult(false, "La descripcion es requerida")
        descripcion.length < 3 -> ValidationResult(false, "Minimo 3 caracteres")
        ocupacionesExistentes.any{it.equals(descripcion.trim(), ignoreCase = true)} ->
            ValidationResult(false, "Esta ocupacion ya esta registrada")
        else -> ValidationResult(true)
    }
}

fun validarSueldo(sueldo: String): ValidationResult{
    return when{
        sueldo.isBlank() -> ValidationResult(false, "El sueldo es obligatorio")
        sueldo.toDoubleOrNull() == null -> ValidationResult(false, "El sueldo debe ser un numero")
        sueldo.toDouble() <= 0 -> ValidationResult(false, "El sueldo debe ser mayor a 0")
        else -> ValidationResult(true)
    }
}
