package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.util.ReflectUtil

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
abstract class BaseReflectAdapter<T : BaseReflectAdapter<T>> {
    @JvmField protected var fromClasses: MutableList<Class<*>> = mutableListOf()
    @JvmField protected var catchException: Boolean = false
    @JvmField protected var classLoader: ClassLoader? = ReflectUtil.getDefaultClassLoad()
    @JvmField protected var lazyMode = false

    /**
     * 设置ClassLoad
     */
    fun classLoader(cl: ClassLoader?) : T {
        classLoader = cl
        return this as T
    }

    /**
     * 设置 懒人模式
     * Field 无效
     */
    fun lazyMode(lazyMode: Boolean) : T {
        this.lazyMode = lazyMode
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
    fun fromClass(vararg classStr: String) : T = (this as T).apply {
        classStr.forEach {
            try {
                fromClasses.add(ReflectUtil.loadClass(it, classLoader))
            } catch (_: Throwable){}
        }
    }

    fun fromClassStr(classStrs: Collection<String>) : T = (this as T).apply {
        classStrs.forEach {
            try {
                fromClasses.add(ReflectUtil.loadClass(it, classLoader))
            } catch (_: Throwable){}
        }
    }

    /**
     * 设置来源class
     */
    fun fromClass(vararg clazz: Class<*>) : T = (this as T).apply{
        fromClasses.addAll(clazz)
    }

    fun fromClass(clazz: Collection<Class<*>>) : T = (this as T).apply{
        fromClasses.addAll(clazz)
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