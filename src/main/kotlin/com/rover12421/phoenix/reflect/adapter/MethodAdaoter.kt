package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.exception.ReflectException
import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Method

@Suppress("MemberVisibilityCanBePrivate", "unused")
class MethodAdapter : BaseReflectAdapter<MethodAdapter>() {
    private val methodNames: MutableSet<String> = mutableSetOf()
    private var returnType: Class<*>? = null
    private var parameterTypes: MutableList<Class<*>?> = mutableListOf()

    private var method: Method? = null

    fun methodName(vararg value: String) : MethodAdapter = this.apply { methodNames.addAll(value) }
    fun methodName(value: Collection<String>) : MethodAdapter = this.apply { methodNames.addAll(value) }

    fun returnType(t: Class<*>?) : MethodAdapter {
        returnType = t
        return this
    }

    fun parameterTypes(vararg cls: Class<*>?) : MethodAdapter = this.apply{
        parameterTypes.clear()
        parameterTypes.addAll(cls)
    }

    fun parameterTypes(cls: Collection<Class<*>?>) : MethodAdapter = this.apply{
        parameterTypes.clear()
        parameterTypes.addAll(cls)
    }


    fun parameterTypes(vararg classStr: String) : MethodAdapter = this.apply {
        parameterTypes.clear()
        classStr.forEach {
            parameterTypes.add(ReflectUtil.loadClass(it, classLoader))
        }
    }

    fun returnType(typeStr: String) : MethodAdapter {
        try {
            returnType(ReflectUtil.loadClass(typeStr, classLoader))
        } catch (e: Throwable) {
            exception(e)
        }
        return this
    }

    override fun checkArgs() {
        if (fromClasses .isEmpty()) {
            throw ReflectException("没有设置 Method 来源 Class")
        }

        if (methodNames.isEmpty()) {
            throw ReflectException("没有设置 Method 的名字")
        }
    }

    private fun checkMethod() {
        if (method != null) {
            return
        }


        try {
            checkArgs()
        } catch (e: Throwable) {
            exception(e)
            return
        }

        var exception: Throwable? = null
        fromClasses.firstOrNull{ clazz ->
            methodNames.firstOrNull { methodName ->
                try {
                    method = ReflectUtil.findMethodInClass(clazz, methodName, parameterTypes.toTypedArray(), null, true, returnType, lazyMode)
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

    fun invokeStatic(vararg args: Any?) : Any? {
        return try {
            toMethod()!!.invoke(null, *args)
        } catch (e: Throwable) {
            exception(e)
            null
        }
    }
    fun invoke(obj: Any?, vararg args: Any?) : Any? {
        return try {
            toMethod()!!.invoke(obj, *args)
        } catch (e: Throwable) {
            exception(e)
            null
        }
    }
}

fun method(init: MethodAdapter.() -> Unit): MethodAdapter = MethodAdapter().apply(init)