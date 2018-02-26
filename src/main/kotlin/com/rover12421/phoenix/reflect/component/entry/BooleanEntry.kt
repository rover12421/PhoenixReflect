package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class BooleanEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(from: Any, value: Boolean) {
        fieldWrapper.setBoolean(from, value)
    }

    fun getValue(from: Any) : Boolean = fieldWrapper.getBoolean(from)
}