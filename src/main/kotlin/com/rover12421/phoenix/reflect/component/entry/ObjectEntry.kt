package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class ObjectEntry(field: Field): FieldEntry(field) {
    fun setValue(from: Any, value: Any) {
        field.set(from, value)
    }

    fun getValue(from: Any): Any? = field.get(from)
}