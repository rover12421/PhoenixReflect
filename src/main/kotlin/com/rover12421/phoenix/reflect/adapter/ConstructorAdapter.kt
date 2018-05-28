package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Constructor

class ConstructorAdapter : BaseReflectAdapter<ConstructorAdapter>() {
    private var parameterTypes: MutableList<Class<*>?> = mutableListOf()
    private val invokeArgs: MutableList<Any?> = mutableListOf()

    private var constructor: Constructor<*>? = null

    fun parameterTypes(vararg cls: Class<*>?) : ConstructorAdapter = parameterTypes(false, *cls)
    /**
     * 设置构造方法的参数类型
     * [append] 追加模式
     */
    fun parameterTypes(append: Boolean, vararg cls: Class<*>?) : ConstructorAdapter {
        if (!append) {
            parameterTypes.clear()
        }
        parameterTypes.addAll(cls)
        return this
    }

    fun parameterTypes(vararg classStr: String) : ConstructorAdapter = parameterTypes(true, *classStr)
    fun parameterTypes(append: Boolean, vararg classStr: String) : ConstructorAdapter {
        if (!append) {
            parameterTypes.clear()
        }
        classStr.forEach {
            parameterTypes.add(ReflectUtil.loadClass(it, classLoader))
        }

        return this
    }

    fun invokeArgs(vararg args: Any?) : ConstructorAdapter  = invokeArgs(false, args)
    /**
     * 设置构造方法所需参数
     * [append] 追加模式
     */
    fun invokeArgs(append: Boolean, vararg args: Any?) : ConstructorAdapter {
        if (!append) {
            invokeArgs.clear()
        }
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

    /**
     * 获取 Constructor 对象
     */
    fun toConstructor() : Constructor<*>? {
        checkConstruct()
        return constructor
    }

    /**
     * 获取构造方法的新实例
     */
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
