package edu.ucne.registroocupaciones.data.mapper.horaextra

import edu.ucne.registroocupaciones.data.local.horaextra.HoraExtraEntity
import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra

fun HoraExtraEntity.toDomain() = HoraExtra(
    horaExtraId = horaExtraId,
    empleadoId = empleadoId,
    fecha = fecha,
    cantidadHoras = cantidadHoras,
    tipo = tipo,
    recargo = recargo
)

fun HoraExtra.toEntity() = HoraExtraEntity(
    horaExtraId = horaExtraId,
    empleadoId = empleadoId,
    fecha = fecha,
    cantidadHoras = cantidadHoras,
    tipo = tipo,
    recargo = recargo
)