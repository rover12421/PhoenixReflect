package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class StaticIntEntry(field: Field): FieldEntry(field) {
    fun setValue(value: Int) {
        field.setInt(null, value)
    }

    fun getValue() : Int = field.getInt(null)
}