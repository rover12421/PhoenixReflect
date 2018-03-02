package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.MethodWrapper

class MethodEntry(override val from: MethodWrapper) : AbsEntry<MethodWrapper>(from) {
    fun invoke(obj: Any, vararg args: Any?) : Any? {
        return from.invoke(obj, args)
    }
}