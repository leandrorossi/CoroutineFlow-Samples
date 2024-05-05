package com.example.coroutineflow_course.funtions

import android.text.Html
import android.text.Spanned

fun fromHtml(source: String): Spanned {
    return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
}