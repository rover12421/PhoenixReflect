package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.ConstructorWrapper
import com.rover12421.phoenix.reflect.wrapper.ObjectWrapper

class ConstructorEntry<T:Any>(val constructorWrapper: ConstructorWrapper<T>) : AbsEntry<ConstructorWrapper<T>>(constructorWrapper) {
    fun newInstance(vararg args: Any? = emptyArray()): T {
        return constructorWrapper.newInstance(args)
    }

    fun newInstanceWrapper(vararg args: Any? = emptyArray()): ObjectWrapper {
        return constructorWrapper.newInstanceWrapper(args)
    }
}