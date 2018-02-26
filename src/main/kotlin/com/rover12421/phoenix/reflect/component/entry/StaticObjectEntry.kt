package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class StaticObjectEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(value: Double) {
        fieldWrapper.setDouble(value)
    }

    fun getValue() : Double = fieldWrapper.getDouble()
}