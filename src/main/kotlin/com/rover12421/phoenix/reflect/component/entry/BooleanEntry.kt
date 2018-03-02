package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class BooleanEntry(field: Field): FieldEntry(field) {
    fun setValue(from: Any, value: Boolean) {
        field.setBoolean(from, value)
    }

    fun getValue(from: Any) : Boolean = field.getBoolean(from)
}