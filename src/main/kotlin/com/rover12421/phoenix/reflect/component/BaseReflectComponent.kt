package com.rover12421.phoenix.reflect.component

abstract class BaseReflectComponent {
    init {
        val clazz = this::class.java
        clazz.declaredFields
    }
}