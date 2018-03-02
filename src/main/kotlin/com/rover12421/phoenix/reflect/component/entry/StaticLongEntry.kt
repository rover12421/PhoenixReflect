package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class StaticLongEntry(field: Field): FieldEntry(field) {
    fun setValue(value: Long) {
        field.setLong(null, value)
    }

    fun getValue() : Long = field.getLong(null)
}