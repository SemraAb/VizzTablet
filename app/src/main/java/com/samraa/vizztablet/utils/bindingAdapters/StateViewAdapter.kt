package com.samraa.vizztablet.utils.bindingAdapters

import androidx.databinding.BindingAdapter
import com.samraa.data.enums.UiState
import com.samraa.vizztablet.utils.StateView
import com.samraa.vizztablet.utils.ViewState

@BindingAdapter(value = ["sv_viewState", "isSwipeRefresh"], requireAll = false)
fun StateView.viewState(uiState: UiState?, isSwipeRefresh: Boolean = false) {
    setViewState(
        when (uiState) {
            UiState.SUCCESS -> ViewState.CONTENT
            UiState.LOADING -> if (isSwipeRefresh) getViewState() else ViewState.LOADING
            UiState.ERROR -> ViewState.ERROR
            UiState.EMPTY -> ViewState.EMPTY
            else -> ViewState.CONTENT
        }
    )
}