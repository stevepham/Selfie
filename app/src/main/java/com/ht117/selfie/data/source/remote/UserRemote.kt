package com.ht117.selfie.data.source.remote

import android.util.Log
import com.ht117.selfie.data.State
import com.ht117.selfie.ext.postRequest
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val user: String,
    val pwd: String
)

class UserRemote(private val client: HttpClient) {
    fun login(user: String, pwd: String) = flow {
        postRequest(client, MOCK_URL, Account(user, pwd))
    }.catch {
        emit(State.Failed(it))
    }

    companion object {
        private const val MOCK_URL = "https://run.mocky.io/v3/0b03f86f-1061-411d-8fbf-20ec5cb573f2"
    }
}
