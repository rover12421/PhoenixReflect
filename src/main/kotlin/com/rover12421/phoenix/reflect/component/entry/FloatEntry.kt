package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class FloatEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(from: Any, value: Float) {
        fieldWrapper.setFloat(from, value)
    }

    fun getValue(from: Any) : Float = fieldWrapper.getFloat(from)
}