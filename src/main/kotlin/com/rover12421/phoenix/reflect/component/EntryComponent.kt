package com.rover12421.phoenix.reflect.component

abstract class EntryComponent {
    init {
        val clazz = this::class.java
        clazz.declaredFields
    }
}