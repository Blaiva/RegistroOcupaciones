package edu.ucne.registroocupaciones.domain.usecase.horaextra

import edu.ucne.registroocupaciones.data.local.empleado.FrecuenciaPago
import edu.ucne.registroocupaciones.data.local.horaextra.TipoHoraExtra

fun calcularRecargoHoraExtra(
    sueldo: Double,
    frecuenciaPago: FrecuenciaPago,
    tipoHoraExtra: TipoHoraExtra,
    cantidadHoras: Int,
    esPuestoDireccion: Boolean
): Double {
    if (esPuestoDireccion){
        return 0.0
    }

    val salarioDiario = sueldo / frecuenciaPago.dias
    val valorHoraOrdinaria = salarioDiario / 8.0
    val montoTotal = valorHoraOrdinaria * tipoHoraExtra.porcentajeRecargo * cantidadHoras

    return Math.round(montoTotal * 100) / 100.0
}