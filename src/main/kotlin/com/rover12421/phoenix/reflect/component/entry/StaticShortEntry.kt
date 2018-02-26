package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class StaticShortEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(value: Short) {
        fieldWrapper.setShort(value)
    }

    fun getValue() : Short = fieldWrapper.getShort()
}