package edu.ucne.registroocupaciones.data.mapper.ocupacion

import edu.ucne.registroocupaciones.data.local.ocupacion.OcupacionEntity
import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion

fun OcupacionEntity.toDomain() = Ocupacion(
    ocupacionId = ocupacionId,
    descripcion = descripcion,
    sueldo = sueldo
)

fun Ocupacion.toEntity() = OcupacionEntity(
    ocupacionId = ocupacionId,
    descripcion = descripcion,
    sueldo = sueldo
)
