package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.util.ReflectUtil

abstract class BaseReflectAdapter {
    @JvmField var fromClass: MutableList<Class<*>> = mutableListOf()
    @JvmField var catchException: Boolean = true
    @JvmField var classLoader: ClassLoader = ReflectUtil.DefaultClassLoad

    fun classLoader(cl: ClassLoader) : BaseReflectAdapter {
        classLoader = cl
        return this
    }

    fun fromClass(vararg classStr: String) : BaseReflectAdapter {
        classStr.forEach {
            try {
                fromClass.add(ReflectUtil.loadClass(it, classLoader))
            } catch (_: Throwable){}
        }

        return this
    }

    fun fromClass(vararg clazz: Class<*>) : BaseReflectAdapter {
        fromClass.addAll(clazz)
        return this
    }

    protected fun exception(e: Throwable? = null) {
        if (!catchException && e != null) {
            /**
             * 不铺获异常，就抛出异常
             */
            throw e
        }
    }

    protected abstract fun checkArgs()
}