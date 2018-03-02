package com.rover12421.phoenix.reflect.wrapper

abstract class ReflectWrapper(@JvmField var from: Any, @JvmField var fromClass: Class<*> = from::class.java) {
    fun modifyFrom(any: Any) : ReflectWrapper {
        from = any
        return this
    }
}