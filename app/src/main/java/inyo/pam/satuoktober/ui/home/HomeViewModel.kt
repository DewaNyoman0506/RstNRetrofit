package inyo.pam.satuoktober.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import inyo.pam.satuoktober.data.repository.StudentRepository

class HomeViewModel(
    private val repository: StudentRepository
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadStudents()
    }

    fun loadStudents() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        repository.getStudents { result ->
            result
                .onSuccess { data ->
                    uiState = uiState.copy(students = data, isLoading = false)
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        errorMessage = error.message ?: "Gagal mengambil data",
                        isLoading = false
                    )
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        uiState = uiState.copy(searchQuery = query)
    }

    fun searchStudents() {
        val query = uiState.searchQuery
        if (query.isBlank()) {
            loadStudents()
        } else {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            repository.searchStudents(query) { result ->
                result
                    .onSuccess { data ->
                        uiState = uiState.copy(students = data, isLoading = false)
                    }
                    .onFailure { error ->
                        uiState = uiState.copy(
                            errorMessage = error.message ?: "Gagal mencari data",
                            isLoading = false
                        )
                    }
            }
        }
    }
}
