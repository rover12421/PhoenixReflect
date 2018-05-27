package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

@Suppress("UNCHECKED_CAST")
class ObjectEntry<T>(field: Field): FieldEntry<T>(field) {
    fun setValue(from: Any, value: Any) {
        field.set(from, value)
        arrayListOf("")
    }

    fun getValue(from: Any): T = field.get(from) as T
}