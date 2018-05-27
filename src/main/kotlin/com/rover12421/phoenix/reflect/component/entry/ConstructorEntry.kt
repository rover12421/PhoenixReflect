package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Constructor

class ConstructorEntry<T>(@JvmField val constructor: Constructor<T>) : AbsEntry<Constructor<T>, T>(constructor) {
    fun newInstance(vararg args: Any? = emptyArray()): T {
        return constructor.newInstance(*args)
    }
}