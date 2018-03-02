package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class IntEntry(field: Field): FieldEntry(field) {
    fun setValue(from: Any, value: Int) {
        field.setInt(from, value)
    }

    fun getValue(from: Any) : Int = field.getInt(from)
}