package com.example.practiceapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practiceapp.CoffeeDetailUi
import com.example.practiceapp.CoffeeRowItemUi
import com.example.practiceapp.data.remote.CoffeeDto
import com.example.practiceapp.RetrofitClient
import com.example.practiceapp.SingleLiveEvent
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"
class HomeViewModel : ViewModel() {
    private val coffeeRowItemUi: MutableLiveData<List<CoffeeRowItemUi>> by lazy {
        MutableLiveData<List<CoffeeRowItemUi>>()
    }

    private val coffeeSelectedEvent: SingleLiveEvent<CoffeeDetailUi> by lazy {
        SingleLiveEvent<CoffeeDetailUi>()
    }

    fun getCoffeeListLiveData() : LiveData<List<CoffeeRowItemUi>> {
        return coffeeRowItemUi
    }

    fun getCoffeeSelectedEventLiveData() : LiveData<CoffeeDetailUi> {
        return coffeeSelectedEvent
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                val responseList: List<CoffeeDto>? =
                    RetrofitClient.coffeeApiService.getCoffees()
                Log.d(TAG, "LALA123")
                responseList?.let { coffeeResponseList ->
                    val coffeeRowItemUiList: MutableList<CoffeeRowItemUi> = mutableListOf()
                    val uniqueCoffeeResponseList = coffeeResponseList.distinctBy { it.title!!.uppercase() }
                    for (coffeeResponse: CoffeeDto in uniqueCoffeeResponseList) {
                        val coffeeRowItemUi = CoffeeRowItemUi(coffeeResponse.title, coffeeResponse.image)
                        coffeeRowItemUiList.add(coffeeRowItemUi)
                    }
                    coffeeRowItemUiList.let {
                        coffeeRowItemUi.value = coffeeRowItemUiList
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "EXCEPTION HAPPENED: " + e.message)
            }
        }
    }

    fun coffeeSelected(coffeeName: String) {
        for (coffeeRowItemUi: CoffeeRowItemUi in coffeeRowItemUi.value!!) {
            if (coffeeRowItemUi.title == coffeeName) {
                coffeeSelectedEvent.value = CoffeeDetailUi(coffeeRowItemUi.title, coffeeRowItemUi.image)
            }
        }
    }
}