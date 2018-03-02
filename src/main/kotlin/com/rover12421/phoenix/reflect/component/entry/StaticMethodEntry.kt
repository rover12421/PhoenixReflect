package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Method

class StaticMethodEntry(@JvmField val method: Method) {
    fun invoke(vararg args: Any?) : Any? {
        return method.invoke(null, *args)
    }
}