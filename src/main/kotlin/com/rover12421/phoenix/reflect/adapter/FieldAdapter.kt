package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.exception.ReflectException
import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Field

class FieldAdapter : BaseReflectAdapter() {
    var name: MutableList<String> = mutableListOf()
    var type: Class<*>? = null
    var fromObject: Any? = null
    var field: Field? = null

    fun name(vararg value: String) : FieldAdapter  {
        name.addAll(value)
        return this
    }

    fun type(t: Class<*>) : FieldAdapter {
        type = t
        return this
    }

    fun type(typeStr: String) : FieldAdapter {
        try {
            type(ReflectUtil.loadClass(typeStr, true, classLoader))
        } catch (e: Throwable) {
            exception(e)
        }
        return this
    }

    fun fromObject(obj: Any) : FieldAdapter {
        fromObject = obj
        return this
    }

    override fun checkArgs() {
        if (fromClass .isEmpty()) {
            if (fromObject == null) {
                throw ReflectException("没有设置Field来源Class")
            }
            fromClass.add(fromObject!!::class.java)
        }

        if (name.isEmpty()) {
            throw ReflectException("没有设置Field的名字")
        }

        checkField()
    }

    private fun checkField() {
        if (field != null) {
            return
        }

        var exception: Throwable? = null
        fromClass.firstOrNull{ clazz ->
            name.firstOrNull { fieldName ->
                try {
                    field = ReflectUtil.findFieldInClass(clazz, fieldName, true, type)
                    true
                } catch (e: Throwable) {
                    exception = e
                    false
                }
            } != null
        }

        if (field == null) {
            exception(exception)
        }
    }

    fun setValue(value: Any?) : FieldAdapter {
        checkArgs()

        try {
            field!!.set(fromObject, value)
        } catch (e: Throwable) {
            exception(e)
        }
        return this
    }

    fun setValue(obj: Any, value: Any?) : FieldAdapter {
        fromObject = obj
        return setValue(value)
    }

    fun <T> getValue() : T {
        checkArgs()
        return try {
            field!!.get(fromObject) as T
        } catch (e: Throwable) {
            exception(e)
            null as T
        }
    }

    fun <T> getValue(obj: Any) : T {
        fromObject = obj
        return getValue() as T
    }
}

fun field(init: FieldAdapter.() -> Unit): FieldAdapter = FieldAdapter().apply(init)
