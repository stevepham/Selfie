package com.ht117.selfie.data

import com.ht117.selfie.data.repo.IResourceRepo
import com.ht117.selfie.data.repo.IUserRepo
import com.ht117.selfie.data.repo.ResourceRepoImpl
import com.ht117.selfie.data.repo.UserRepoImpl
import com.ht117.selfie.data.source.local.ResourceLocal
import com.ht117.selfie.data.source.local.UserLocal
import com.ht117.selfie.data.source.remote.Client
import com.ht117.selfie.data.source.remote.UserRemote
import org.koin.dsl.module

val dataModule = module {
    single { Client.getClient() }
    single { UserLocal(get()) }
    single { UserRemote(get()) }
    single<IUserRepo> { UserRepoImpl(get(), get()) }

    single { ResourceLocal(get()) }
    single<IResourceRepo> { ResourceRepoImpl(get()) }
}
