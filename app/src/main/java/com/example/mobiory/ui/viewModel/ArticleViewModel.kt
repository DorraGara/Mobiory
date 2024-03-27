package com.example.mobiory.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.graphics.Bitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel  @Inject constructor(): ViewModel() {
    private val _articleTitle = MutableLiveData<String>()
    val articleTitle: LiveData<String> = _articleTitle

    private val _articleText = MutableLiveData<String>()
    val articleText: LiveData<String> = _articleText

    private val _articleImageBitmap = MutableLiveData<Bitmap?>()
    val articleImageBitmap: LiveData<Bitmap?> = _articleImageBitmap

    fun setArticleData(title: String, text: String, bitmap: Bitmap?) {
        _articleTitle.value = title
        _articleText.value = text
        _articleImageBitmap.value = bitmap
    }
}
