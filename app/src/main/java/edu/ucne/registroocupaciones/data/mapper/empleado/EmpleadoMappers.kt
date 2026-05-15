package edu.ucne.registroocupaciones.data.mapper.empleado

import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoEntity
import edu.ucne.registroocupaciones.domain.model.empleado.Empleado

fun EmpleadoEntity.toDomain() = Empleado(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo
)

fun Empleado.toEntity() = EmpleadoEntity(
    empleadoId = empleadoId,
    fechaIngreso = fechaIngreso,
    nombres = nombres,
    sexo = sexo,
    sueldo = sueldo
)