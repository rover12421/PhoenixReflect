package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class ObjectEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(from: Any, value: Any) {
        fieldWrapper.setValue(from, value)
    }

    fun getValue(from: Any): Any? = fieldWrapper.getValue(from)
}