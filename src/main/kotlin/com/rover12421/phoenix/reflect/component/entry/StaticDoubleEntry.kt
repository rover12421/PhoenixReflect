package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

class StaticDoubleEntry(field: Field): FieldEntry<Double>(field) {
    fun setValue(value: Double) {
        field.setDouble(null, value)
    }

    fun getValue() : Double = field.getDouble(null)
}