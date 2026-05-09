package edu.ucne.registroocupaciones.data.mapper

import edu.ucne.registroocupaciones.data.local.OcupacionEntity
import edu.ucne.registroocupaciones.domain.model.Ocupacion

fun OcupacionEntity.toDomain() = Ocupacion(
    OcupacionId = OcupacionId,
    Descripcion = Descripcion,
    Sueldo = Sueldo
)

fun Ocupacion.toEntity() = OcupacionEntity(
    OcupacionId = OcupacionId,
    Descripcion = Descripcion,
    Sueldo = Sueldo
)
