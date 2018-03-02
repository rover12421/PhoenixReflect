package com.rover12421.phoenix.reflect.wrapper

import java.lang.reflect.Method

class MethodWrapper(val method: Method, from: Any, fromClass: Class<*> = from::class.java) : ReflectWrapper(from, fromClass) {
    fun invoke(any: Any, vararg args: Any?) : Any? {
        return method.invoke(any, args)
    }

    fun invoke(vararg args: Any?) : Any? {
        return method.invoke(from, args)
    }

    fun invokeWrapper(any: Any, vararg args: Any?) : ObjectWrapper {
        return ObjectWrapper.fromObject(invoke(any, args)!!)
    }

    fun invokeWrapper(vararg args: Any?) : ObjectWrapper {
        return ObjectWrapper.fromObject(invoke(from, args)!!)
    }
}