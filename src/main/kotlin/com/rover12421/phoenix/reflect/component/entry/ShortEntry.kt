package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class ShortEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(from: Any, value: Short) {
        fieldWrapper.setShort(from, value)
    }

    fun getValue(from: Any) : Short = fieldWrapper.getShort(from)
}