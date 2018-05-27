package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class DoubleEntry(field: Field): FieldEntry<Double>(field) {
    fun setValue(from: Any, value: Double) {
        field.setDouble(from, value)
    }

    fun getValue(from: Any) : Double = field.getDouble(from)
}