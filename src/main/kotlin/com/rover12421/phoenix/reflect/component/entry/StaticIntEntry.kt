package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class StaticIntEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(value: Int) {
        fieldWrapper.setInt(value)
    }

    fun getValue() : Int = fieldWrapper.getInt()
}