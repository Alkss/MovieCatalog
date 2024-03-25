package com.alkss.moviecatalog.feature_movielist.presentation.util

/**
 * Returns the next element in the list after the specified element.
 * If the specified element is the last element in the list, null is returned.
 *
 * @param element The element to find the next element for.
 * @return The next element in the list, or null if the specified element is the last element.
 */
fun <T> List<T>.getNext(element: T): T? {
    val index = this.indexOf(element)
    return if (index in 0 until this.lastIndex) this[index + 1] else null
}

fun Int.offsetToPage(): Int{
    return (this/20) + 1
}