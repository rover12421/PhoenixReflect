package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class LongEntry(field: Field): FieldEntry<Long>(field) {
    fun setValue(from: Any, value: Long) {
        field.setLong(from, value)
    }

    fun getValue(from: Any) : Long = field.getLong(from)
}