package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class IntEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(from: Any, value: Int) {
        fieldWrapper.setInt(from, value)
    }

    fun getValue(from: Any) : Int = fieldWrapper.getInt(from)
}