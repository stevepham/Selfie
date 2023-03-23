package com.ht117.selfie.data

sealed class State<out T> {
    object Loading: State<Nothing>()
    data class Result<T>(val data: T): State<T>()
    data class Failed(val err: Throwable): State<Nothing>()
}
