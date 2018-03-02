package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class StaticFloatEntry(field: Field): FieldEntry(field) {
    fun setValue(value: Float) {
        field.setFloat(null, value)
    }

    fun getValue() : Float = field.getFloat(null)
}