package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.MethodWrapper

class StaticMethodEntry(val methodWrapper: MethodWrapper) {
    fun invoke(vararg args: Any?) : Any? {
        return methodWrapper.invoke(args)
    }
}