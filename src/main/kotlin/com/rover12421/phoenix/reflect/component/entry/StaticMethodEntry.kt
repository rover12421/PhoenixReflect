package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.MethodWrapper

class StaticMethodEntry<out T>(val methodWrapper: MethodWrapper) {
    fun invoke(vararg args: Any?) : T? {
        return methodWrapper.invoke(args)
    }
}