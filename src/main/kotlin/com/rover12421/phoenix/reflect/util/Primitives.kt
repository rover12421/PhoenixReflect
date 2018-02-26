package com.rover12421.phoenix.reflect.util

object Primitives {
    private val PRIMITIVE_TO_WRAPPER_TYPE = mapOf(
            java.lang.Boolean.TYPE      to java.lang.Boolean::class.java,
            java.lang.Byte.TYPE         to java.lang.Byte::class.java,
            java.lang.Character.TYPE    to java.lang.Character::class.java,
            java.lang.Double.TYPE       to java.lang.Double::class.java,
            java.lang.Float.TYPE        to java.lang.Float::class.java,
            java.lang.Integer.TYPE      to java.lang.Integer::class.java,
            java.lang.Long.TYPE         to java.lang.Long::class.java,
            java.lang.Short.TYPE        to java.lang.Short::class.java,
            java.lang.Void.TYPE         to java.lang.Void::class.java
            )

    private val WRAPPER_TO_PRIMITIVE_TYPE = mapOf(
            java.lang.Boolean::class.java   to java.lang.Boolean.TYPE,
            java.lang.Byte::class.java      to java.lang.Byte.TYPE,
            java.lang.Character::class.java to java.lang.Character.TYPE,
            java.lang.Double::class.java    to java.lang.Double.TYPE,
            java.lang.Float::class.java     to java.lang.Float.TYPE,
            java.lang.Integer::class.java   to java.lang.Integer.TYPE,
            java.lang.Long::class.java      to java.lang.Long.TYPE,
            java.lang.Short::class.java     to java.lang.Short.TYPE,
            java.lang.Void::class.java      to java.lang.Void.TYPE
    )

    fun isWrapperType(type: Class<*>): Boolean {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(type)
    }

    fun wrap(type: Class<*>): Class<*> {
        return PRIMITIVE_TO_WRAPPER_TYPE[type] ?: type
    }

    fun unwrap(type: Class<*>): Class<*> {
        return WRAPPER_TO_PRIMITIVE_TYPE[type] ?: type
    }
}