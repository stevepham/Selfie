package com.ht117.selfie.data.repo

import com.ht117.selfie.data.State
import com.ht117.selfie.data.source.local.MyImage
import com.ht117.selfie.data.source.local.ResourceLocal
import kotlinx.coroutines.flow.Flow

interface IResourceRepo {
    fun getImages(): Flow<State<List<MyImage>>>
}

class ResourceRepoImpl(private val local: ResourceLocal): IResourceRepo {
    override fun getImages() = local.getResources()
}
