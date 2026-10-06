package com.glootie.meeseeks.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes : NavKey {
    @Serializable
    data object CharacterList : Routes

    @Serializable
    data class CharacterDetails(val id: Int) : Routes

    @Serializable
    data class LocationDetails(val id: Int) : Routes
}
