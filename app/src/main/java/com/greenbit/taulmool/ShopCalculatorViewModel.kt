package com.greenbit.taulmool

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

data class CalculatorState(
    val pricePerUnit: String = "",
    val quantity: String = "",
    val totalAmount: String = "",
    val calculationType: CalculationType = CalculationType.QUANTITY_TO_PRICE,
    val result: String = "",
    val calculationDetails: String = "",
    val isCalculating: Boolean = false
)

enum class CalculationType {
    QUANTITY_TO_PRICE, // Customer asks for X kg, calculate total price
    PRICE_TO_QUANTITY  // Customer asks for items worth X rupees, calculate quantity
}

class ShopCalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorState())
    val uiState: StateFlow<CalculatorState> = _uiState.asStateFlow()

    fun updatePricePerUnit(price: String) {
        _uiState.value = _uiState.value.copy(pricePerUnit = price, result = "")
    }

    fun updateQuantity(quantity: String) {
        _uiState.value = _uiState.value.copy(quantity = quantity, result = "")
    }

    fun updateTotalAmount(amount: String) {
        _uiState.value = _uiState.value.copy(totalAmount = amount, result = "")
    }

    fun setCalculationType(type: CalculationType) {
        _uiState.value = _uiState.value.copy(
            calculationType = type,
            result = "",
            quantity = "",
            totalAmount = ""
        )
    }

    fun calculate() {
        val currentState = _uiState.value

        _uiState.value = currentState.copy(isCalculating = true)

        try {
            val pricePerUnit = currentState.pricePerUnit.toDoubleOrNull()

            if (pricePerUnit == null || pricePerUnit <= 0) {
                _uiState.value = currentState.copy(
                    result = "Please enter a valid price per unit",
                    isCalculating = false
                )
                return
            }

            val result = when (currentState.calculationType) {
                CalculationType.QUANTITY_TO_PRICE -> {
                    val quantity = currentState.quantity.toDoubleOrNull()
                    if (quantity == null || quantity <= 0) {
                        "Please enter a valid quantity"
                    } else {
                        val totalPrice = quantity * pricePerUnit
                        "Total Price: ₹${String.format(Locale.getDefault(), "%.2f", totalPrice)}\n" +
                        "Calculation: ${quantity}kg × ₹${pricePerUnit} = ₹${String.format(Locale.getDefault(), "%.2f", totalPrice)}"
                    }
                }

                CalculationType.PRICE_TO_QUANTITY -> {
                    val totalAmount = currentState.totalAmount.toDoubleOrNull()
                    if (totalAmount == null || totalAmount <= 0) {
                        "Please enter a valid amount"
                    } else {
                        val quantityKg = totalAmount / pricePerUnit
                        val quantityGrams = quantityKg * 1000

                        val quantityDisplay = if (quantityKg < 1.0) {
                            "${String.format(Locale.getDefault(), "%.0f", quantityGrams)}g (${String.format(Locale.getDefault(), "%.3f", quantityKg)}kg)"
                        } else {
                            "${String.format(Locale.getDefault(), "%.2f", quantityKg)}kg (${String.format(Locale.getDefault(), "%.0f", quantityGrams)}g)"
                        }

                        "Quantity: $quantityDisplay\n" +
                        "Calculation: ₹${totalAmount} ÷ ₹${pricePerUnit} per kg = ${String.format(Locale.getDefault(), "%.3f", quantityKg)}kg"
                    }
                }
            }

            _uiState.value = currentState.copy(result = result, isCalculating = false)

        } catch (_: Exception) {
            _uiState.value = currentState.copy(
                result = "Error in calculation. Please check your inputs.",
                isCalculating = false
            )
        }
    }

    fun clearAll() {
        _uiState.value = CalculatorState()
    }
}
