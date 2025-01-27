package com.samraa.vizztablet.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected open val viewModel: BaseViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicks()
        subscribeToObservables()
        navigateObserve()


    }

    open fun setClicks() {}
    open fun subscribeToObservables() {}
    open fun navigateObserve() {}
}