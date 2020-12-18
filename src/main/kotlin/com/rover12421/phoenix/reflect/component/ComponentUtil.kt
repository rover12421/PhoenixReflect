package com.rover12421.phoenix.reflect.component

import com.rover12421.phoenix.reflect.adapter.constructor
import com.rover12421.phoenix.reflect.adapter.field
import com.rover12421.phoenix.reflect.adapter.method
import com.rover12421.phoenix.reflect.component.annotation.*
import com.rover12421.phoenix.reflect.component.entry.*
import com.rover12421.phoenix.reflect.util.ReflectUtil
import java.lang.reflect.Field

object ComponentUtil {
    @JvmStatic private val AllEntryClass = arrayOf(
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

    @JvmField val entryConstructorMap = AllEntryClass.associateBy({it}, {it.constructors[0]})

    /**
     * 获取节点名
     */
    @JvmStatic
    fun getEntryNames(field: Field): Collection<String> {
        return field.getAnnotation(EntryNames::class.java)?.value?.toSet()?: setOf(field.name)
    }

    @JvmStatic
    fun isLazyMode(field: Field) = field.isAnnotationPresent(LazyMode::class.java)

    /**
     * 从字段[field]注解上获取来源Class(FromClass)
     */
    @JvmStatic
    fun getFromClassInField(field: Field, classLoader: ClassLoader? = ReflectUtil.getDefaultClassLoad()): MutableList<Class<*>> {
        val cls = mutableListOf<Class<*>>()
        field.getAnnotation(FromClassByClass::class.java)?.value?.forEach { cls.add(it.java) }

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
    @JvmStatic
    fun getFromClassInClass(defineClass: Class<*>, classLoader: ClassLoader? = ReflectUtil.getDefaultClassLoad()): MutableList<Class<*>> {
        val cls = mutableListOf<Class<*>>()
        try {
            defineClass.getAnnotation(FromClassByClass::class.java)?.value?.forEach { cls.add(it.java) }
        } catch (_: Throwable){}

        defineClass.getAnnotation(FromClassByString::class.java)?.value?.forEach {
            try {
                cls.add(ReflectUtil.loadClass(it, classLoader))
            } catch (_: Throwable) {}
        }

        return cls
    }

    /**
     * 获取节点类型
     */
    @JvmStatic
    fun getEntryType(field: Field, classLoader: ClassLoader? = ReflectUtil.getDefaultClassLoad()) : Class<*>? {
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

    @JvmStatic
    fun getParameterTypes(field: Field, classLoader: ClassLoader? = ReflectUtil.getDefaultClassLoad()) : MutableList<Class<*>?> {
        val cls = mutableListOf<Class<*>?>()
        field.getAnnotation(ParameterTypesByClass::class.java)?.value?.forEach { cls.add(it.java) }

        if (cls.isEmpty()) {
            /**
             * [ParameterTypesByClass] 和 [ParameterTypesByString] 同时使用
             * [ParameterTypesByString] 无效
             */
            field.getAnnotation(ParameterTypesByString::class.java)?.value?.forEach {
                try {
                    cls.add(ReflectUtil.loadClass(it, classLoader))
                } catch (_: Throwable) {
                    cls.add(null)
                }
            }
        }
        return cls
    }

    fun initComponent(initObj: Any) {
        val clazz = initObj::class.java
        val fromClasses = getFromClassInClass(clazz)
        clazz.declaredFields.forEach { field ->
            val filedType = field.type
            val ctor = entryConstructorMap[filedType]
            if (ctor != null) {
                val classes = getFromClassInField(field)
                classes.addAll(fromClasses)

                if (classes.isEmpty()) {
                    /**
                     * 没有来源 Class 说明找不到类，没有必要继续下去了
                     */
                    return@forEach
                }

                val entryNames = getEntryNames(field)
                val entryType = getEntryType(field)
                val parameterTypes = getParameterTypes(field)
                val lazyMode = isLazyMode(field)

                val obj = when {
                    FieldEntry::class.java.isAssignableFrom(filedType) -> {
                        val f = field {
                            fieldName(entryNames)
                            type(entryType)
                            fromClass(classes)
                            catchException(true)
                        }.toField()
                        f?.let { ctor.newInstance(f) }
                    }
                    filedType == MethodEntry::class.java ||
                            filedType == StaticMethodEntry::class.java -> {
                        val m = method {
                            methodName(entryNames)
                            returnType(entryType)
                            fromClass(classes)
                            parameterTypes(parameterTypes)
                            catchException(true)
                            lazyMode(lazyMode)
                        }.toMethod()
                        m?.let { ctor.newInstance(m) }
                    }
                    filedType == ConstructorEntry::class.java -> {
                        val c = constructor {
                            fromClass(classes)
                            parameterTypes(parameterTypes)
                            catchException(true)
                            lazyMode(lazyMode)
                        }.toConstructor()
                        c?.let { ctor.newInstance(c) }
                    }
                    else -> null
                }
                field.isAccessible = true
                field.set(initObj, obj)
            }
        }
    }
}