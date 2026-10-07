package com.glootie.meeseeks

import androidx.lifecycle.ViewModel
import com.glootie.meeseeks.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    characterRepository: CharacterRepository
) : ViewModel() {

    val isFirstPageLoaded: StateFlow<Boolean> = characterRepository.isFirstPageLoaded

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchActive(isActive: Boolean) {
        _isSearchActive.value = isActive
    }
}
