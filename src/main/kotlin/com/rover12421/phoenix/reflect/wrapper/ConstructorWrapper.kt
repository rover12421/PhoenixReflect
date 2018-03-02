package com.rover12421.phoenix.reflect.wrapper

import java.lang.reflect.Constructor

class ConstructorWrapper(val constructor: Constructor<*>, from: Any, fromClass: Class<*> = from::class.java) : ReflectWrapper(from, fromClass) {
    fun newInstance(vararg args: Any): Any? {
        return constructor.newInstance(*args)
    }

    fun newInstanceWrapper(vararg args: Any): ObjectWrapper {
        return ObjectWrapper.fromObject(newInstance(args)!!)
    }
}