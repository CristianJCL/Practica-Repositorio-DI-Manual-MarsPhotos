/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.data

import com.example.marsphotos.network.MarsApiService
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

/** Expone las dependencias que utiliza la aplicación. */
interface AppContainer {
    val marsPhotosRepository: MarsPhotosRepository
}

/** Construye una única configuración de Retrofit y comparte el Repository. */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com/"

    /** Configura el cliente HTTP y convierte el JSON recibido en objetos Kotlin. */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /** Crea el servicio REST al utilizarlo por primera vez. */
    private val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }

    /** Comparte la instancia del Repository que consulta el servicio de Marte. */
    override val marsPhotosRepository: MarsPhotosRepository by lazy {
        NetworkMarsPhotosRepository(retrofitService)
    }
}
