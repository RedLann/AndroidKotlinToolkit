package com.github.redlann.ui_extended

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

fun <T : ViewDataBinding> Fragment.bindingInflate(
    inflater: LayoutInflater,
    layout: Int,
    container: ViewGroup?
): T {
    return DataBindingUtil.inflate(inflater, layout, container, false)
}

fun <T : ViewDataBinding> Activity.bindingInflate(layout: Int): T {
    return DataBindingUtil.setContentView(this, layout)
}