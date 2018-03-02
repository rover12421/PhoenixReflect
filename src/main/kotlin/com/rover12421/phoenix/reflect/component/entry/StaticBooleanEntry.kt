package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class StaticBooleanEntry(field: Field): FieldEntry(field) {
    fun setValue(value: Boolean) {
        field.setBoolean(null, value)
    }

    fun getValue() : Boolean = field.getBoolean(null)
}