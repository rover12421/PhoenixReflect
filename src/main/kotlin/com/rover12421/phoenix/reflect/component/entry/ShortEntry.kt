package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class ShortEntry(field: Field): FieldEntry<Short>(field) {
    fun setValue(from: Any, value: Short) {
        field.setShort(from, value)
    }

    fun getValue(from: Any) : Short = field.getShort(from)
}