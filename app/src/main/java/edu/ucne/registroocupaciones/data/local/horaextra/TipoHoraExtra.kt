package edu.ucne.registroocupaciones.data.local.horaextra

enum class TipoHoraExtra(val descripcion: String, val porcentajeRecargo: Double) {
    DIURNO("Diurno", 1.35),
    NOCTURNO("Nocturno", 1.5),
    DIA_LIBRE_FERIADO("Dia libre o feriado", 2.0),
    ALTO_VOLUMEN("Alto Volumen", 2.0)
}