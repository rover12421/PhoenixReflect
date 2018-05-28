package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.exception.ReflectException
import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Method

class MethodAdaoter : BaseReflectAdapter<MethodAdaoter>() {
    private val methodNames: MutableList<String> = mutableListOf()
    private var returnType: Class<*>? = null
    private var fromObject: Any? = null
    private var parameterTypes: MutableList<Class<*>?> = mutableListOf()
    private val invokeArgs: MutableList<Any?> = mutableListOf()

    private var method: Method? = null

    fun methodName(vararg value: String) : MethodAdaoter = methodName(false, *value)
    fun methodName(append: Boolean, vararg value: String) : MethodAdaoter  {
        if (!append) {
            methodNames.clear()
        }
        methodNames.addAll(value)
        return this
    }

    fun returnType(t: Class<*>?) : MethodAdaoter {
        returnType = t
        return this
    }

    fun parameterTypes(vararg cls: Class<*>?) : MethodAdaoter = parameterTypes(false, *cls)
    fun parameterTypes(append: Boolean, vararg cls: Class<*>?) : MethodAdaoter {
        if (!append) {
            parameterTypes.clear()
        }
        parameterTypes.addAll(cls)
        return this
    }

    fun parameterTypes(vararg classStr: String) : MethodAdaoter = parameterTypes(false, *classStr)
    fun parameterTypes(append: Boolean, vararg classStr: String) : MethodAdaoter {
        if (!append) {
            parameterTypes.clear()
        }
        classStr.forEach {
            parameterTypes.add(ReflectUtil.loadClass(it, classLoader))
        }
        return this
    }

    fun invokeArgs(vararg args: Any?) : MethodAdaoter = invokeArgs(false, *args)
    fun invokeArgs(append: Boolean, vararg args: Any?) : MethodAdaoter {
        if (!append) {
            invokeArgs.clear()
        }
        invokeArgs.addAll(args)
        return this
    }

    fun returnType(typeStr: String) : MethodAdaoter {
        try {
            returnType(ReflectUtil.loadClass(typeStr, classLoader))
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