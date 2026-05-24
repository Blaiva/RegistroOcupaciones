package edu.ucne.registroocupaciones.presentation.list.horaextra

sealed class HoraExtraListUiEvent {
    object Load: HoraExtraListUiEvent()
    object Refresh: HoraExtraListUiEvent()
    data class ShowMessage(val message: String): HoraExtraListUiEvent()
    object ClearMessage: HoraExtraListUiEvent()
    object CreateNew: HoraExtraListUiEvent()
    data class Edit(val id: Int): HoraExtraListUiEvent()
}