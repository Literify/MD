package com.literify.util

import android.content.Context

class DefaultStringProvider(private val context: Context) : StringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}

interface StringProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any): String
}