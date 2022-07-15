package com.ebenezer.gana.datacomm.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText{
    data class DynamicString(val value:String):UiText()
    class StringResource(
        @StringRes val resId:Int
    ):UiText()

    fun asString(context: Context):String{
        return when(this){
            is StringResource -> context.resources.getString(resId)
            is DynamicString -> value
        }
    }
}
