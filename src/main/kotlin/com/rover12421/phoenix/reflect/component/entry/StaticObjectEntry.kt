package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class StaticObjectEntry(field: Field): FieldEntry(field) {
    fun setValue(value: Any?) {
        field.set(null, value)
    }

    fun getValue() : Any? = field.get(null)
}