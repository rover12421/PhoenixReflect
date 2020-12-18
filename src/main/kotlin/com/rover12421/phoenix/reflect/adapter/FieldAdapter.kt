package com.rover12421.phoenix.reflect.adapter

import com.rover12421.phoenix.reflect.exception.ReflectException
import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Field

@Suppress("MemberVisibilityCanBePrivate")
class FieldAdapter : BaseReflectAdapter<FieldAdapter>() {
    private val fieldNames: MutableSet<String> = mutableSetOf()
    private var type: Class<*>? = null
    private var field: Field? = null

    fun fieldName(vararg value: String) : FieldAdapter = this.apply { fieldNames.addAll(value) }
    fun fieldName(value: Collection<String>) : FieldAdapter = this.apply { fieldNames.addAll(value) }

    fun type(t: Class<*>?) : FieldAdapter {
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

    override fun checkArgs() {
        if (fromClasses .isEmpty()) {
            throw ReflectException("没有设置Field来源Class")
        }

        if (fieldNames.isEmpty()) {
            throw ReflectException("没有设置Field的名字")
        }
    }

    private fun checkField() {
        if (field != null) {
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

    fun setValue(value: Any?) : FieldAdapter = setValue(null, value)
    fun setValue(obj: Any?, value: Any?) : FieldAdapter {
        try {
            toField()!!.set(obj, value)
        } catch (e: Throwable) {
            exception(e)
        }
        return this
    }

    fun getValue(obj: Any? = null) : Any? {
        return try {
            toField()!!.get(obj)
        } catch (e: Throwable) {
            exception(e)
            null
        }
    }
}

fun field(init: FieldAdapter.() -> Unit): FieldAdapter = FieldAdapter().apply(init)