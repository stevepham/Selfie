package com.ht117.selfie.data.source.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Client {
    const val Host = ""
    private val jsonConfig = Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }

    fun getClient() = HttpClient(OkHttp) {
        followRedirects = true
        install(ContentNegotiation) {
            json(jsonConfig)
        }
    }
}
