package com.example.practiceapp.data.remote

import com.example.practiceapp.domain.model.Coffee

data class CoffeeDto(
    var title: String? = null,
    var description: String? = null,
    var image: String? = null
) {
    fun toCoffee(): Coffee {
        return Coffee(
            title = title,
            description = description,
            image = image
        )
    }
}