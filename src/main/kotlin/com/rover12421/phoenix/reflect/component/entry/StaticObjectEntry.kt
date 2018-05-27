package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

@Suppress("UNCHECKED_CAST")
class StaticObjectEntry<T>(field: Field): FieldEntry<T>(field) {
    fun setValue(value: Any?) {
        field.set(null, value)
    }

    fun getValue() : T = field.get(null) as T
}