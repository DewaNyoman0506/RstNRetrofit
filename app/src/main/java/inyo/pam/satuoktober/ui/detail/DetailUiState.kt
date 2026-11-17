package inyo.pam.satuoktober.ui.detail

import inyo.pam.satuoktober.data.model.Student

data class DetailUiState(
    val student: Student? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)