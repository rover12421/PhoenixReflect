package com.rover12421.phoenix.reflect.wrapper

import java.lang.reflect.Method

class MethodWrapper(val method: Method, from: Any, fromClass: Class<*> = from::class.java) : ReflectWrapper(from, fromClass) {
    fun <T> invoke(any: Any, vararg args: Any?) : T {
        return method.invoke(any, args) as T
    }

    fun <T> invoke(vararg args: Any?) : T {
        return method.invoke(from, args) as T
    }

    fun invokeWrapper(any: Any, vararg args: Any?) : ObjectWrapper {
        return ObjectWrapper.fromObject(invoke(any, args))
    }

    fun invokeWrapper(vararg args: Any?) : ObjectWrapper {
        return ObjectWrapper.fromObject(invoke(from, args))
    }
}