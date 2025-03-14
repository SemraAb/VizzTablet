package com.samraa.vizztablet.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.samraa.vizztablet.R
import com.samraa.vizztablet.utils.ViewState.Companion.findById
import com.samraa.vizztablet.utils.extension.getResourceId
import com.samraa.vizztablet.utils.extension.isVisibleElseInVisible

class StateView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private var viewState = ViewState.CONTENT
    private var childVisibility = SvChildVisibility.INVISIBLE
    private var loadingViewId = 0
    private var errorViewId = 0
    private var emptyViewId = 0
    private var refreshLayoutId = 0
    private var ignoreReferencedIds = mutableListOf<Int>()

    private var retryViewId = 0
    private var onRetryClickListener: OnClickListener? = null

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed)
            layoutsCreated()
    }

    init {
        val t = context.obtainStyledAttributes(attrs, R.styleable.StateView)

        viewState = findById(t.getInt(R.styleable.StateView_sv_viewState, ViewState.CONTENT.id))
        childVisibility = SvChildVisibility.findById(
            t.getInt(
                R.styleable.StateView_sv_child_visibility,
                SvChildVisibility.INVISIBLE.id
            )
        )
        loadingViewId = t.getResourceId(R.styleable.StateView_sv_loadingViewId, R.id.svLoadingView)
        errorViewId = t.getResourceId(R.styleable.StateView_sv_errorViewId, R.id.svErrorView)
        emptyViewId = t.getResourceId(R.styleable.StateView_sv_emptyViewId, R.id.svEmptyView)
        retryViewId = t.getResourceId(R.styleable.StateView_sv_retryViewId, R.id.svRetryBtn)
//        refreshLayoutId =
//            t.getResourceId(R.styleable.StateView_sv_refreshLayoutId, R.id.refreshLayout)

        t.getString(R.styleable.StateView_sv_ignore_referenced_ids)?.let { string ->
            ignoreReferencedIds =
                string.split(",").map { context.getResourceId(it.trim()) }.toMutableList()
        }

        t.recycle()
    }

    private fun layoutsCreated() {
        initContentGroup()

        setViewState(viewState)
    }

    private fun initContentGroup() {
        for (i in 0..childCount)
            getChildAt(i)?.let {
                if (it.id != loadingViewId || it.id != errorViewId || it.id != emptyViewId) {
                    if (it.id == -1)
                        it.id = View.generateViewId()
                }

                if (it.id == retryViewId)
                    it.setOnClickListener(onRetryClickListener)
            }
    }

    fun setViewState(vs: ViewState) {
        viewState = vs

        val hasEmptyView = findViewById<View?>(emptyViewId) != null

        if (viewState == ViewState.EMPTY && !hasEmptyView)
            viewState = ViewState.CONTENT

        for (i in 0 until childCount) {
            getChildAt(i).apply {
                if (!ignoreReferencedIds.contains(this.id)) {
                    when (this.id) {
                        loadingViewId ->
                            this.isVisible = viewState == ViewState.LOADING
                        errorViewId ->
                            this.isVisible = viewState == ViewState.ERROR
                        emptyViewId ->
                            this.isVisible = viewState == ViewState.EMPTY
                        refreshLayoutId ->
                            this.isVisible =
                                viewState == ViewState.EMPTY || viewState == ViewState.CONTENT //show SwipeRefreshLayout when list is empty
                        else -> {
                            if (this.tag == null || this.isVisible)
                                this.tag = this.isVisible

                            if (childVisibility == SvChildVisibility.GONE)
                                this.isVisible = viewState == ViewState.CONTENT && this.tag == true
                            else
                                this.isVisibleElseInVisible(viewState == ViewState.CONTENT && this.tag == true)
                        }
                    }
                }
            }
        }
    }

    fun getViewState() = viewState

    fun setOnRetryClick(listener: OnClickListener?) {
        onRetryClickListener = listener
    }
}


enum class ViewState(val id: Int) {
    CONTENT(0),
    LOADING(1),
    ERROR(2),
    EMPTY(3);

    companion object {
        fun findById(id: Int) = entries.find { it.id == id } ?: CONTENT
    }
}

enum class SvChildVisibility(val id: Int) {
    GONE(0),
    INVISIBLE(1);

    companion object {
        fun findById(id: Int) = entries.find { it.id == id } ?: INVISIBLE
    }
}