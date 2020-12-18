package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Constructor

@Suppress("MemberVisibilityCanBePrivate", "unused")
class ConstructorAdapter : BaseReflectAdapter<ConstructorAdapter>() {
    private var parameterTypes: MutableList<Class<*>?> = mutableListOf()

    private var ctor: Constructor<*>? = null

    /**
     * 设置构造方法的参数类型
     */
    fun parameterTypes(vararg cls: Class<*>?) : ConstructorAdapter = this.apply {
        parameterTypes.clear()
        parameterTypes.addAll(cls)
    }

    fun parameterTypes(cls: Collection<Class<*>?>) : ConstructorAdapter = this.apply {
        parameterTypes.clear()
        parameterTypes.addAll(cls)
    }

    fun parameterTypes(vararg classStr: String) : ConstructorAdapter = this.apply{
        parameterTypes.clear()
        classStr.forEach {
            parameterTypes.add(ReflectUtil.loadClass(it, classLoader))
        }
    }

    fun parameterTypesByStr(classStr: Collection<String>) : ConstructorAdapter = this.apply {
        parameterTypes.clear()
        classStr.forEach {
            parameterTypes.add(ReflectUtil.loadClass(it, classLoader))
        }
    }

    override fun checkArgs() {

    }

    private fun checkConstruct() {
        if (ctor != null) {
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
            try {
                ctor = ReflectUtil.findConstructorInClass(clazz, parameterTypes.toTypedArray(), null, lazyMode)
                true
            } catch (e: Throwable) {
                exception = e
                false
            }
        }

        if (ctor == null) {
            exception(exception)
        }
    }

    /**
     * 获取 Constructor 对象
     */
    fun toConstructor() : Constructor<*>? {
        checkConstruct()
        return ctor
    }

    /**
     * 获取构造方法的新实例
     */
    fun newInstance(vararg args: Any?) : Any? {
        return try {
            toConstructor()!!.newInstance(*args)
        } catch (e: Throwable) {
            exception(e)
            null
        }
    }
}

fun constructor(init: ConstructorAdapter.() -> Unit): ConstructorAdapter = ConstructorAdapter().apply(init)