package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Constructor

class ConstructorEntry(@JvmField val constructor: Constructor<*>) : AbsEntry<Constructor<*>>(constructor) {
    fun newInstance(vararg args: Any? = emptyArray()): Any? {
        return constructor.newInstance(args)
    }
}