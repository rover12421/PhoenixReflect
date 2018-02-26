package com.rover12421.phoenix.reflect.wrapper

import com.rover12421.phoenix.reflect.exception.ReflectException
import com.rover12421.phoenix.reflect.util.ReflectUtil

class ObjectWrapper private constructor(from: Any, fromClass: Class<*> = from::class.java) : ReflectWrapper(from, fromClass) {
    companion object {
        fun fromObject(any: Any) = ObjectWrapper(any)
        fun fromClass(clazz: Class<*>) = ObjectWrapper(clazz, clazz)
        fun fromClassStr(clazzStr: String, initialize: Boolean = true,
                         classLoader: ClassLoader = ReflectUtil.getCurrentClassLoad())
                : ObjectWrapper {
            val clazz = ReflectUtil.loadClass(clazzStr, initialize, classLoader)
            return ObjectWrapper(clazz, clazz)
        }
    }

    fun field(name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : FieldWrapper {
        try {
            return FieldWrapper(ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type), from, fromClass)
        } catch (e: Throwable) {
            throw ReflectException("[ObjectWrapper] Not Found Field($name) in Clazz $fromClass")
        }
    }

    fun method(name: String, parameterTypes: Array<Class<*>?>? = null, args: Array<Any?>? = null, findInSpuer: Boolean = true, retType: Class<*>? = null) : MethodWrapper {
        try {
            return MethodWrapper(ReflectUtil.findMethodInClass(fromClass, name, parameterTypes, args, findInSpuer, retType), from, fromClass)
        } catch (e: Throwable) {
            throw ReflectException("[ObjectWrapper] Not Found Field($name) in Clazz $fromClass")
        }
    }

    fun <T : Any> constructor(parameterTypes: Array<Class<*>?>? = null, args: Array<Any?>? = null) : ConstructorWrapper<T> {
        try {
            return ConstructorWrapper(ReflectUtil.findConstructorInClass(fromClass, parameterTypes, args), from, fromClass)
        } catch (e: Throwable) {
            throw ReflectException("[ObjectWrapper] Not Found Constructor in Clazz $fromClass")
        }
    }
}