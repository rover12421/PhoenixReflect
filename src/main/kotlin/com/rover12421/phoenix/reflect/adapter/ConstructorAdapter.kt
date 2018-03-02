package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Constructor

class ConstructorAdapter : BaseReflectAdapter() {
    private var parameterTypes: MutableList<Class<*>?> = mutableListOf()
    private val invokeArgs: MutableList<Any?> = mutableListOf()

    private var constructor: Constructor<*>? = null

    fun parameterTypes(vararg cls: Class<*>?) : ConstructorAdapter {
        parameterTypes.addAll(cls)
        return this
    }

    fun invokeArgs(vararg args: Any?) : ConstructorAdapter {
        invokeArgs.addAll(args)
        return this
    }

    override fun checkArgs() {

    }

    private fun checkConstruct() {
        if (constructor != null) {
            return
        }

        checkArgs()

        var exception: Throwable? = null
        fromClass.firstOrNull{ clazz ->
            try {
                constructor = ReflectUtil.findConstructorInClass(clazz, parameterTypes.toTypedArray(), invokeArgs.toTypedArray())
                true
            } catch (e: Throwable) {
                exception = e
                false
            }
        }

        if (constructor == null) {
            exception(exception)
        }
    }

    fun toConstructor() : Constructor<*>? {
        checkConstruct()
        return constructor
    }

    fun newInstance() : Any? {
        return try {
            toConstructor()?.newInstance(*invokeArgs.toTypedArray())
        } catch (e: Throwable) {
            exception(e)
            null
        }
    }
}

fun constructor(init: ConstructorAdapter.() -> Unit): ConstructorAdapter = ConstructorAdapter().apply(init)
