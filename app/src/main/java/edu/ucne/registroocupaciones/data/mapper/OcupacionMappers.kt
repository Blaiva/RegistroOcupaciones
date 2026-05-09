package edu.ucne.registroocupaciones.data.mapper

import edu.ucne.registroocupaciones.data.local.OcupacionEntity
import edu.ucne.registroocupaciones.domain.model.Ocupacion

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
