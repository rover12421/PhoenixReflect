package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.exception.ReflectException
import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Method

class MethodAdaoter : BaseReflectAdapter() {
    private val methodNames: MutableList<String> = mutableListOf()
    private var returnType: Class<*>? = null
    private var fromObject: Any? = null
    private var parameterTypes: MutableList<Class<*>?> = mutableListOf()
    private val invokeArgs: MutableList<Any?> = mutableListOf()

    private var method: Method? = null

    fun name(vararg value: String) : MethodAdaoter  {
        methodNames.addAll(value)
        return this
    }

    fun returnType(t: Class<*>) : MethodAdaoter {
        returnType = t
        return this
    }

    fun returnType(typeStr: String) : MethodAdaoter {
        try {
            returnType(ReflectUtil.loadClass(typeStr, true, classLoader))
        } catch (e: Throwable) {
            exception(e)
        }
        return this
    }

    fun fromObject(obj: Any) : MethodAdaoter {
        fromObject = obj
        return this
    }

    override fun checkArgs() {
        if (fromClass .isEmpty()) {
            if (fromObject == null) {
                throw ReflectException("没有设置Method来源Class")
            }
            fromClass.add(fromObject!!::class.java)
        }

        if (methodNames.isEmpty()) {
            throw ReflectException("没有设置Methodd的名字")
        }
    }

    private fun checkMethod() {
        if (method != null) {
            return
        }

        checkArgs()

        var exception: Throwable? = null
        fromClass.firstOrNull{ clazz ->
            methodNames.firstOrNull { methodName ->
                try {
                    method = ReflectUtil.findMethodInClass(clazz, methodName, parameterTypes.toTypedArray(), invokeArgs.toTypedArray(), true, returnType)
                    true
                } catch (e: Throwable) {
                    exception = e
                    false
                }
            } != null
        }

        if (method == null) {
            exception(exception)
        }
    }

    fun toMethod() : Method? {
        checkMethod()
        return method
    }

    fun invoke() : Any? {
        return try {
            toMethod()?.invoke(fromObject, *invokeArgs.toTypedArray())
        } catch (e: Throwable) {
            exception(e)
            null
        }
    }
}

fun method(init: MethodAdaoter.() -> Unit): MethodAdaoter = MethodAdaoter().apply(init)