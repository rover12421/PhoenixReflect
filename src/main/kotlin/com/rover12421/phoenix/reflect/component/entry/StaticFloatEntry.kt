package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class StaticFloatEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(value: Float) {
        fieldWrapper.setFloat(value)
    }

    fun getValue() : Float = fieldWrapper.getFloat()
}