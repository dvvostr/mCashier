package ru.studiq.mcashier.model.classes.hw

class CustomHardwareError(code: Int, message: String?) {
    val code: Int = code
    val text: String? = message
}