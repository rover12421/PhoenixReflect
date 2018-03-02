package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class FloatEntry(field: Field): FieldEntry(field) {
    fun setValue(from: Any, value: Float) {
        field.setFloat(from, value)
    }

    fun getValue(from: Any) : Float = field.getFloat(from)
}