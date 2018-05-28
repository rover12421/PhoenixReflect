package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.util.ReflectUtil

@Suppress("UNCHECKED_CAST")
abstract class BaseReflectAdapter<T : BaseReflectAdapter<T>> {
    @JvmField protected var fromClass: MutableList<Class<*>> = mutableListOf()
    @JvmField protected var catchException: Boolean = false
    @JvmField protected var classLoader: ClassLoader = ReflectUtil.DefaultClassLoad

    /**
     * 设置ClassLoad
     */
    fun classLoader(cl: ClassLoader) : T {
        classLoader = cl
        return this as T
    }

    /**
     * 是否抛出异常
     * 默认 false， 抛出异常
     */
    fun catchException(catchException: Boolean) : T {
        this.catchException = catchException
        return this as T
    }

    /**
     * 设置来源class，使用字符串模式
     */
    fun fromClass(vararg classStr: String, append: Boolean = true) : T {
        if (!append) {
            fromClass.clear()
        }
        classStr.forEach {
            try {
                fromClass.add(ReflectUtil.loadClass(it, classLoader))
            } catch (_: Throwable){}
        }

        return this as T
    }

    /**
     * 设置来源class
     */
    fun fromClass(vararg clazz: Class<*>, append: Boolean = true) : T {
        if (!append) {
            fromClass.clear()
        }
        fromClass.addAll(clazz)
        return this as T
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