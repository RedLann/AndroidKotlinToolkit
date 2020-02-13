package com.github.redlann.ui_extended

import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation

fun Fragment.navigate(directions: NavDirections) =
    Navigation.findNavController(view!!).navigate(directions)

fun Fragment.navigateUpTo(@IdRes resId: Int) =
    navigate(resId, NavOptions.Builder().setPopUpTo(resId, true).build())

private fun Fragment.navigate(@IdRes resId: Int, @Nullable navOptions: NavOptions) =
    Navigation.findNavController(view!!).navigate(resId, null, navOptions)
