package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class StaticShortEntry(field: Field): FieldEntry<Short>(field) {
    fun setValue(value: Short) {
        field.setShort(null, value)
    }

    fun getValue() : Short = field.getShort(null)
}