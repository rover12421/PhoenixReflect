package com.rover12421.phoenix.reflect.util

import com.rover12421.phoenix.reflect.exception.ReflectException
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.math.max

@Suppress("MemberVisibilityCanBePrivate", "unused")
object ReflectUtil {
    /**
     * 获取当前的 ClassLoad
     */
    fun getDefaultClassLoad(): ClassLoader? = Thread.currentThread().contextClassLoader

    /**
     * 加载 Class
     * == Class.fromName
     */
    fun loadClass(clazzStr: String, initialize: Boolean = true, classLoader: ClassLoader? = getDefaultClassLoad()) : Class<*> {
        try {
            return Class.forName(clazzStr, initialize, classLoader)
        } catch (e : Throwable) {
            throw ReflectException(e)
        }
    }

    fun loadClass(clazzStr: String, classLoader: ClassLoader?) : Class<*> {
        try {
            return Class.forName(clazzStr, true, classLoader)
        } catch (e : Throwable) {
            throw ReflectException(e)
        }
    }

    /**
     * 在指定的 Class [clazz] 中查找字段 [name]
     *
     * [findInSuper] 是否查找父类， 默认为 true
     * [type] 查找 field 的类型
     */
    fun findFieldInClass(clazz: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Field {
        val field = clazz.declaredFields.firstOrNull { field ->
            field.name == name && (type == null || type == field.type)
        }

        if (field != null) {
            field.isAccessible = true
            return field
        }

        if (findInSuper && clazz.superclass != null) {
            try {
                return findFieldInClass(clazz.superclass!!, name, findInSuper, type)
            } catch (e: Throwable) {
                throw ReflectException("[ReflectUtil] Not Found Field($name) in Class $clazz or SuperClass")
            }
        } else {
            throw ReflectException("[ReflectUtil] Not Found Field($name) in Class $clazz")
        }
    }

    /**
     * 在指定的 Class [clazz] 中查找方法 [name]
     *
     * [parameterTypes] 参数类型数组
     * [args] 参数实例
     * [findInSuper] 是否查找父类， 默认为 true
     * [retType] 查找 field 的类型
     * [lazyMode] 懒人模式
     */
    fun findMethodInClass(clazz: Class<*>, name: String, parameterTypes: Array<Class<*>?>? = null, args: Array<Any?>? = null, findInSuper: Boolean = true, retType: Class<*>? = null, lazyMode: Boolean = false) : Method {
        val methods = clazz.declaredMethods
        methods.forEach { method ->
            if (
                    method.name == name
                    && (retType == null || retType.isAssignableFrom(method.returnType))
                    && isMatchParameterTypes(method.parameterTypes, parameterTypes, args, lazyMode)
            ) {
                method.isAccessible = true
                return method
            }
        }

        if (findInSuper && clazz.superclass != null) {
            try {
                return findMethodInClass(clazz.superclass!!, name, parameterTypes, args, findInSuper, retType, lazyMode)
            } catch (e: Throwable) {
                throw ReflectException("[ReflectUtil] Not Found Method($name) in Class $clazz or SuperClass")
            }
        } else {
            throw ReflectException("[ReflectUtil] Not Found Method($name) in Class $clazz")
        }
    }

    /**
     * 通过 参数类型列表[fromParameterTypes] 和 调用参数的对象[fromArgs] 配合起来 来查询和 原参数列表[parameterTypes] 是否匹配
     * [lazyMode] 懒人模式
     *
     * 参数类型列表[fromParameterTypes] 的长度必须小于或等于 原参数列表[parameterTypes] 的长度
     * 调用参数的对象[fromArgs] 的长度也必须小于或等于 原参数列表[parameterTypes] 的长度
     * [fromParameterTypes] 和 [fromArgs] 至少有一个必须等于 [parameterTypes] 的长度
     * 这样做是为了更精准命中目标，减少不同SDK因为参数数量不同而都匹配上
     *
     * [lazyMode] 模式下， [parameterTypes] >= [fromParameterTypes]/[fromArgs] 长度即可
     *
     */
    fun isMatchParameterTypes(parameterTypes: Array<Class<*>>, fromParameterTypes: Array<Class<*>?>? = null, fromArgs: Array<Any?>? = null, lazyMode: Boolean = false): Boolean {
        val types = fromParameterTypes ?: emptyArray()
        val args = fromArgs ?: emptyArray()
        val size = parameterTypes.size

        val fromSize = max(types.size, args.size)

        if (!lazyMode && size != fromSize) {
            /**
             * 非懒人模式，参数长度为精准匹配
             */
            return false
        }

        if (lazyMode) {
            if (size < fromSize) {
                /**
                 * 懒人模式下
                 * parameterTypes.size 为最大即可
                 */
                return false
            } else if (fromSize == 0) {
                /**
                 * 懒人模式下
                 * 无参数匹配，直接返回即可
                 */
                return true
            }
        }

        for (index in 0 until size) {
            val realType = parameterTypes[index]
            val fromType = types.elementAtOrNull(index)

            if (fromType != null) {
                if (fromType == Void::class.java || fromType == Void.TYPE) {
                    /**
                     * 用Void的替代 null，不检查类型匹配
                     */
                    break
                } else if (realType != fromType) {
                    /**
                     * 指定了类型，类型不匹配则结果不匹配
                     */
                    return false
                }
            }

            val fromArg = args.elementAtOrNull(index)
            if (fromArg != null && !Primitives.wrap(realType).isInstance(fromArg)) {
                /**
                 * 没有指定 Type，指定 调用参数 对象，通过调用参数对象来判断是否和目标需求一致
                 */
                return false
            }

            /**
             * 没有指定类型，也没有调用实例，继续比较下一个。。。
             */
        }

        return true
    }

    /**
     * 在指定的 Class [clazz] 中查找构造方法
     *
     * [parameterTypes] 参数类型数组
     * [args] 参数实例
     */
    fun findConstructorInClass(clazz: Class<*>, parameterTypes: Array<Class<*>?>? = null, args: Array<Any?>? = null, lazyMode: Boolean = false) : Constructor<*> {
        val constructors = clazz.declaredConstructors
        constructors.forEach { constructor ->
            if (
                    isMatchParameterTypes(constructor.parameterTypes, parameterTypes, args, lazyMode)
            ) {
                constructor.isAccessible = true
                return constructor
            }
        }

        throw ReflectException("Not Constructor in Clazz $clazz")
    }

    /**
     * 通过反射调用非静态方法
     */
    fun invokeDynamic(fromObj: Any, methodName: String, args: Array<Any?>? = null, parameterTypes: Array<Class<*>?>? = null, retType: Class<*>? = null, lazyMode: Boolean = false): Any? {
        val invokeArgs = args?: emptyArray()
        return findMethodInClass(fromObj::class.java, methodName, parameterTypes, args, true, retType, lazyMode).invoke(fromObj, *invokeArgs)
    }

    /**
     * 通过反射调用静态方法
     */
    fun invokeStatic(fromClass: Class<*>, methodName: String, args: Array<Any?>? = null, parameterTypes: Array<Class<*>?>? = null, retType: Class<*>? = null, lazyMode: Boolean = false): Any? {
        val invokeArgs = args?: emptyArray()
        return findMethodInClass(fromClass, methodName, parameterTypes, args, true, retType, lazyMode).invoke(fromClass, *invokeArgs)
    }

    /**
     * 通过反射构建新的实例对象
     */
    fun newInstance(fromClass: Class<*>, args: Array<Any?>? = null, parameterTypes: Array<Class<*>?>? = null, lazyMode: Boolean = false): Any? {
        val invokeArgs = args?: emptyArray()
        return findConstructorInClass(fromClass, parameterTypes, args, lazyMode).newInstance(*invokeArgs)
    }

    fun getBooleanField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Boolean {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).getBoolean(fromObj)
    }

    fun getStaticBooleanField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Boolean {
        return findFieldInClass(fromClass, name, findInSuper, type).getBoolean(fromClass)
    }

    fun getByteField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Byte {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).getByte(fromObj)
    }

    fun getStaticByteField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Byte {
        return findFieldInClass(fromClass, name, findInSuper, type).getByte(fromClass)
    }

    fun getCharField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Char {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).getChar(fromObj)
    }

    fun getStaticCharField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Char {
        return findFieldInClass(fromClass, name, findInSuper, type).getChar(fromClass)
    }

    fun getDoubleField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Double {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).getDouble(fromObj)
    }

    fun getStaticDoubleField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Double {
        return findFieldInClass(fromClass, name, findInSuper, type).getDouble(fromClass)
    }

    fun getFloatField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Float {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).getFloat(fromObj)
    }

    fun getStaticFloatField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Float {
        return findFieldInClass(fromClass, name, findInSuper, type).getFloat(fromClass)
    }

    fun getIntField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Int {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).getInt(fromObj)
    }

    fun getStaticIntField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Int {
        return findFieldInClass(fromClass, name, findInSuper, type).getInt(fromClass)
    }

    fun getLongField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Long {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).getLong(fromObj)
    }

    fun getStaticLongField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Long {
        return findFieldInClass(fromClass, name, findInSuper, type).getLong(fromClass)
    }

    fun getShortField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Short {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).getShort(fromObj)
    }

    fun getStaticShortField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Short {
        return findFieldInClass(fromClass, name, findInSuper, type).getShort(fromClass)
    }

    fun getObjectField(fromObj: Any, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Any? {
        return findFieldInClass(fromObj::class.java, name, findInSuper, type).get(fromObj)
    }

    fun getStaticObjectField(fromClass: Class<*>, name: String, findInSuper: Boolean = true, type: Class<*>? = null) : Any? {
        return findFieldInClass(fromClass, name, findInSuper, type).get(fromClass)
    }

    fun setBooleanField(fromObj: Any, name: String, value: Boolean, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).setBoolean(fromObj, value)
    }

    fun setStaticBooleanField(fromClass: Class<*>, name: String, value: Boolean, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).setBoolean(fromClass, value)
    }

    fun setByteField(fromObj: Any, name: String, value: Byte, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).setByte(fromObj, value)
    }

    fun setStaticByteField(fromClass: Class<*>, name: String, value: Byte, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).setByte(fromClass, value)
    }

    fun setCharField(fromObj: Any, name: String, value: Char, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).setChar(fromObj, value)
    }

    fun setStaticCharField(fromClass: Class<*>, name: String, value: Char, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).setChar(fromClass, value)
    }

    fun setDoubleField(fromObj: Any, name: String, value: Double, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).setDouble(fromObj, value)
    }

    fun setStaticDoubleField(fromClass: Class<*>, name: String, value: Double, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).setDouble(fromClass, value)
    }

    fun setFloatField(fromObj: Any, name: String, value: Float, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).setFloat(fromObj, value)
    }

    fun setStaticFloatField(fromClass: Class<*>, name: String, value: Float, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).setFloat(fromClass, value)
    }

    fun setIntField(fromObj: Any, name: String, value: Int, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).setInt(fromObj, value)
    }

    fun setStaticIntField(fromClass: Class<*>, name: String, value: Int, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).setInt(fromClass, value)
    }

    fun setLongField(fromObj: Any, name: String, value: Long, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).setLong(fromObj, value)
    }

    fun setStaticLongField(fromClass: Class<*>, name: String, value: Long, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).setLong(fromClass, value)
    }

    fun setShortField(fromObj: Any, name: String, value: Short, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).setShort(fromObj, value)
    }

    fun setStaticShortField(fromClass: Class<*>, name: String, value: Short, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).setShort(fromClass, value)
    }

    fun setObjectField(fromObj: Any, name: String, value: Any?, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromObj::class.java, name, findInSuper, type).set(fromObj, value)
    }

    fun setStaticObjectField(fromClass: Class<*>, name: String, value: Any?, findInSuper: Boolean = true, type: Class<*>? = null) {
        findFieldInClass(fromClass, name, findInSuper, type).set(fromClass, value)
    }
}
