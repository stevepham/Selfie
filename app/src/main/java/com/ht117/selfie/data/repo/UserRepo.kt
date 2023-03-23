package com.ht117.selfie.data.repo

import com.ht117.selfie.data.State
import com.ht117.selfie.data.source.local.UserLocal
import com.ht117.selfie.data.source.remote.UserRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

interface IUserRepo {
    fun isLoggedIn(): Flow<State<Boolean>>
    fun login(user: String, pwd: String): Flow<State<Boolean>>
    suspend fun setLoggedIn()
    fun logout(): Flow<State<Boolean>>
}

class UserRepoImpl(private val local: UserLocal,
                   private val remote: UserRemote): IUserRepo {

    override fun isLoggedIn() = local.isUserLoggedIn()
        .map { State.Result(it) }

    override fun login(user: String, pwd: String): Flow<State<Boolean>> {
        return remote.login(user, pwd)
    }

    override suspend fun setLoggedIn() {
        local.setLoggedIn()
    }

    override fun logout() = flow {
        local.logout()
        emit(State.Result(true))
    }
}
