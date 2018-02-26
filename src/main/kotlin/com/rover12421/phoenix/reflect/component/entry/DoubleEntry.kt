package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class DoubleEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(from: Any, value: Double) {
        fieldWrapper.setDouble(from, value)
    }

    fun getValue(from: Any) : Double = fieldWrapper.getDouble(from)
}