package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.exception.ReflectException
import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Field

class FieldAdapter : BaseReflectAdapter() {
    private val fieldNames: MutableList<String> = mutableListOf()
    private var type: Class<*>? = null
    private var fromObject: Any? = null
    private var field: Field? = null

    fun name(vararg value: String) : FieldAdapter  {
        fieldNames.addAll(value)
        return this
    }

    fun type(t: Class<*>) : FieldAdapter {
        type = t
        return this
    }

    fun type(typeStr: String) : FieldAdapter {
        try {
            type(ReflectUtil.loadClass(typeStr, classLoader))
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

        if (fieldNames.isEmpty()) {
            throw ReflectException("没有设置Field的名字")
        }
    }

    private fun checkField() {
        if (field != null) {
            return
        }

        checkArgs()

        var exception: Throwable? = null
        fromClass.firstOrNull{ clazz ->
            fieldNames.firstOrNull { fieldName ->
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

    fun toField() : Field? {
        checkField()
        return field
    }

    fun setValue(value: Any?) : FieldAdapter {
        try {
            toField()?.set(fromObject, value)
        } catch (e: Throwable) {
            exception(e)
        }
        return this
    }

    fun setValue(obj: Any, value: Any?) : FieldAdapter {
        fromObject(obj)
        return setValue(value)
    }

    fun getValue() : Any? {
        return try {
            toField()?.get(fromObject)
        } catch (e: Throwable) {
            exception(e)
            null
        }
    }

    fun getValue(obj: Any) : Any? {
        fromObject(obj)
        return getValue()
    }
}

fun field(init: FieldAdapter.() -> Unit): FieldAdapter = FieldAdapter().apply(init)
