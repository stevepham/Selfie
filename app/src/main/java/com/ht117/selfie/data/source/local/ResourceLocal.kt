package com.ht117.selfie.data.source.local

import android.content.Context
import android.provider.MediaStore
import com.ht117.selfie.data.State
import kotlinx.coroutines.flow.flow

data class MyImage(val path: String, val name: String)

class ResourceLocal(private val context: Context) {

    fun getResources() = flow<State<List<MyImage>>> {
        emit(State.Loading)
        val resolver = context.contentResolver
        val projections = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val imgSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val cursor = resolver.query(imageUri, projections, null, null, imgSortOrder)
        val images = mutableListOf<MyImage>()
        cursor?.use {
            while (it.moveToNext() && images.size <= 3) {
                val path = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val name = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(MyImage(path, name))
            }
            it.close()
        }
        emit(State.Result(images))
    }
}
