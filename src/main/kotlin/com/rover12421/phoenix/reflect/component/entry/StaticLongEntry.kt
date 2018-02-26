package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class StaticLongEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(value: Long) {
        fieldWrapper.setLong(value)
    }

    fun getValue() : Long = fieldWrapper.getLong()
}