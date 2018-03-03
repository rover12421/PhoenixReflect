package com.rover12421.phoenix.reflect.component

import com.rover12421.phoenix.reflect.component.annotation.*
import com.rover12421.phoenix.reflect.component.entry.*
import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Field

object ComponentUtil {
    private val AllEntryClass = arrayOf(
            BooleanEntry::class.java,
            DoubleEntry::class.java,
            FloatEntry::class.java,
            IntEntry::class.java,
            LongEntry::class.java,
            ObjectEntry::class.java,
            ShortEntry::class.java,

            StaticBooleanEntry::class.java,
            StaticDoubleEntry::class.java,
            StaticFloatEntry::class.java,
            StaticIntEntry::class.java,
            StaticLongEntry::class.java,
            StaticObjectEntry::class.java,
            StaticShortEntry::class.java,

            ConstructorEntry::class.java,

            MethodEntry::class.java,
            StaticMethodEntry::class.java
    )

    @JvmField val EntryConstructor = AllEntryClass.associateBy({it}, {it.constructors[0]})

    /**
     * 获取节点名
     */
    fun getEntryNames(field: Field): Array<String> {
        val names = mutableListOf<String>()
        val annotation = field.getAnnotation(EntryNames::class.java)
        if (annotation != null) {
            names.addAll(annotation.value)
        } else {
            names.add(field.name)
        }
        return names.toTypedArray()
    }

    /**
     * 从字段[field]注解上获取来源Class(FromClass)
     */
    fun getFromClassInField(field: Field, classLoader: ClassLoader = ReflectUtil.DefaultClassLoad): MutableList<Class<*>> {
        val cls = mutableListOf<Class<*>>()
        field.getAnnotation(FromClassByClass::class.java)?.apply {
            cls.addAll(value.map { it.java })
        }

        field.getAnnotation(FromClassByString::class.java)?.value?.forEach {
            try {
                cls.add(ReflectUtil.loadClass(it, classLoader))
            } catch (_: Throwable) {}
        }

        return cls
    }

    /**
     * 从定义类[defineClass]注解上获取来源Class(FromClass)
     */
    fun getFromClassInClass(defineClass: Class<*>? = null, classLoader: ClassLoader = ReflectUtil.DefaultClassLoad): MutableList<Class<*>> {
        val cls = mutableListOf<Class<*>>()
        if (defineClass != null) {
            defineClass.getAnnotation(FromClassByClass::class.java)?.apply {
                cls.addAll(value.map { it.java })
            }

            defineClass.getAnnotation(FromClassByString::class.java)?.value?.forEach {
                try {
                    cls.add(ReflectUtil.loadClass(it, classLoader))
                } catch (_: Throwable) {}
            }
        }

        return cls
    }

    /**
     * 获取节点类型
     */
    fun getEntryType(field: Field, classLoader: ClassLoader = ReflectUtil.DefaultClassLoad) : Class<*>? {
        val entryType = when(field.type) {
            BooleanEntry::class.java    -> Boolean::class.java
            DoubleEntry::class.java     -> Double::class.java
            FloatEntry::class.java      -> Float::class.java
            IntEntry::class.java        -> Int::class.java
            LongEntry::class.java       -> Long::class.java
            ShortEntry::class.java      -> Short::class.java

            StaticBooleanEntry::class.java  -> Boolean::class.java
            StaticDoubleEntry::class.java   -> Double::class.java
            StaticFloatEntry::class.java    -> Float::class.java
            StaticIntEntry::class.java      -> Int::class.java
            StaticLongEntry::class.java     -> Long::class.java
            StaticShortEntry::class.java    -> Short::class.java

            else -> null
        }
        return entryType ?:
                field.getAnnotation(EntryTypeByClass::class.java)?.value?.java
                ?:
                field.getAnnotation(EntryTypeByString::class.java)?.value?.let {
                    ReflectUtil.loadClass(it, classLoader)
                }
    }

    fun getParameterTypes(field: Field, classLoader: ClassLoader = ReflectUtil.DefaultClassLoad) : MutableList<Class<*>?> {
        val cls = mutableListOf<Class<*>?>()
        field.getAnnotation(ParameterTypesByClass::class.java)?.apply {
            cls.addAll(value.map { it.java })
        }

        field.getAnnotation(ParameterTypesByString::class.java)?.value?.forEach {
            try {
                cls.add(ReflectUtil.loadClass(it, classLoader))
            } catch (_: Throwable) {
                cls.add(null)
            }
        }
        return cls
    }
}