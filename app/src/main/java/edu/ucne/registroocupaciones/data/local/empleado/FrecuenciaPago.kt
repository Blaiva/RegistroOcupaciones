package edu.ucne.registroocupaciones.data.local.empleado

enum class FrecuenciaPago(val descripcion: String, val dias: Double) {
    SEMANAL("Semanal",5.5),
    QUINCENAL("Quincenal", 11.91),
    MENSUAL("Mensual", 23.83)
}