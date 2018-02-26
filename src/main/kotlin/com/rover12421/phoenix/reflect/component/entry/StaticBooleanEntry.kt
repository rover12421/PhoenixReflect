package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

class StaticBooleanEntry(fieldWrapper: FieldWrapper): FieldEntry(fieldWrapper) {
    fun setValue(value: Boolean) {
        fieldWrapper.setBoolean(value)
    }

    fun getValue() : Boolean = fieldWrapper.getBoolean()
}