package com.ht117.selfie.ext

import com.ht117.selfie.data.State
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.serialization.Serializable

@Serializable
data class Response(val data: Boolean)

suspend inline fun <reified R> FlowCollector<State<Boolean>>.postRequest(
    client: HttpClient,
    url: String,
    data: R
) {
    emit(State.Loading)
    val response = client.post(url) {
        contentType(ContentType.Application.Json)
        setBody(data)
    }
    if (response.status == HttpStatusCode.OK) {
        val body = response.body<Response>()
        if (body.data) {
            emit(State.Result(true))
        } else {
            emit(State.Failed(Throwable()))
        }
    } else {
        emit(State.Failed(Throwable()))
    }
}
