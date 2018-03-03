package com.rover12421.phoenix.reflect.component

import com.rover12421.phoenix.reflect.adapter.constructor
import com.rover12421.phoenix.reflect.adapter.field
import com.rover12421.phoenix.reflect.adapter.method
import com.rover12421.phoenix.reflect.component.entry.ConstructorEntry
import com.rover12421.phoenix.reflect.component.entry.FieldEntry
import com.rover12421.phoenix.reflect.component.entry.MethodEntry
import com.rover12421.phoenix.reflect.component.entry.StaticMethodEntry

abstract class BaseReflectComponent {
    init {
        val clazz = this::class.java
        val fromClass = ComponentUtil.getFromClassInClass(clazz)
        clazz.declaredFields.forEach { field ->
            val filedType = field.type
            val ctor = ComponentUtil.EntryConstructor[filedType]
            if (ctor != null) {
                fromClass.addAll(0, ComponentUtil.getFromClassInField(field))

                val entryNames = ComponentUtil.getEntryNames(field)
                val entryType = ComponentUtil.getEntryType(field)
                val parameterTypes = ComponentUtil.getParameterTypes(field).toTypedArray()

                val obj = when {
                    FieldEntry::class.java.isAssignableFrom(filedType) -> {
                        val f = field {
                            name(*entryNames)
                            type(entryType)
                            fromClass(*fromClass.toTypedArray())
                        }.toField()
                        ctor.newInstance(f)
                    }
                    filedType == MethodEntry::class.java ||
                            filedType == StaticMethodEntry::class.java -> {
                        val m = method {
                            name(*entryNames)
                            returnType(entryType)
                            fromClass(*fromClass.toTypedArray())
                            parameterTypes(*parameterTypes)
                        }.toMethod()
                        ctor.newInstance(m)
                    }
                    filedType == ConstructorEntry::class.java -> {
                        val c = constructor {
                            fromClass(*fromClass.toTypedArray())
                            parameterTypes(*parameterTypes)
                        }.toConstructor()
                        ctor.newInstance(c)
                    }
                    else -> null
                }
                field.set(this, obj)
            }
        }
    }
}