package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class LongEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(from: Any, value: Long) {
        fieldWrapper.setLong(from, value)
    }

    fun getValue(from: Any) : Long = fieldWrapper.getLong(from)
}