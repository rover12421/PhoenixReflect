package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
class StaticMethodEntry<T>(@JvmField val method: Method) : AbsEntry<Method, T>(method) {
    fun invoke(vararg args: Any?) : T {
        return method.invoke(null, *args) as T
    }
}