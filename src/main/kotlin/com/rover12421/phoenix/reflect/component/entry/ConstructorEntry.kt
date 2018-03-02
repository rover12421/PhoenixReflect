package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.ConstructorWrapper
import com.rover12421.phoenix.reflect.wrapper.ObjectWrapper

class ConstructorEntry(val constructorWrapper: ConstructorWrapper) : AbsEntry<ConstructorWrapper>(constructorWrapper) {
    fun newInstance(vararg args: Any? = emptyArray()): Any? {
        return constructorWrapper.newInstance(args)
    }

    fun newInstanceWrapper(vararg args: Any? = emptyArray()): ObjectWrapper {
        return constructorWrapper.newInstanceWrapper(args)
    }
}