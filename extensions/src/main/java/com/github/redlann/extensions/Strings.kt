package com.github.redlann.extensions

inline fun <R> String?.notEmptyOrNull(block: (String) -> R): R? {
    if (this == null || this.isEmpty()) return null
    return block(this)
}