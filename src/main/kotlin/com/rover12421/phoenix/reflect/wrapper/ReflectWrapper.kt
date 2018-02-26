package com.rover12421.phoenix.reflect.wrapper

abstract class ReflectWrapper(var from: Any, var fromClass: Class<*> = from::class.java) {
    fun modifyFrom(any: Any) : ReflectWrapper {
        from = any
        return this
    }
}