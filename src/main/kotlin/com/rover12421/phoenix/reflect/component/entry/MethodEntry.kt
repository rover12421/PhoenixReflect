package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Method

class MethodEntry(override val from: Method) : AbsEntry<Method>(from) {
    fun invoke(obj: Any, vararg args: Any?) : Any? {
        return from.invoke(obj, *args)
    }
}