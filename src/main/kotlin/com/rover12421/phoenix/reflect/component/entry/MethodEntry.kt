package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
class MethodEntry<T>(override val from: Method) : AbsEntry<Method, T>(from) {
    fun invoke(obj: Any, vararg args: Any?) : T {
        return from.invoke(obj, *args) as T
    }
}