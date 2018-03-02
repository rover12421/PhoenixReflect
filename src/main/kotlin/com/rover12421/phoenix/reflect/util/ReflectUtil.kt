package com.rover12421.phoenix.reflect.util

import com.rover12421.phoenix.reflect.exception.ReflectException
import sun.reflect.Reflection
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method


object ReflectUtil {
    /**
     * 获取当前的 ClassLoad
     */
    fun getDefaultClassLoad(): ClassLoader = Thread.currentThread().contextClassLoader

    /**
     * 加载 Class
     * == Class.fromName
     */
    fun loadClass(clazzStr: String, initialize: Boolean = true, classLoader: ClassLoader = getDefaultClassLoad()) : Class<*> {
        try {
            return Class.forName(clazzStr, initialize, classLoader)
        } catch (e : Throwable) {
            throw ReflectException(e)
        }
    }

    /**
     * 在指定的 Class [clazz] 中查找字段 [name]
     *
     * [findInSpuer] 是否查找父类， 默认为 true
     * [type] 查找 field 的类型
     */
    fun findFieldInClass(clazz: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Field {
        val fields = clazz.declaredFields
        fields.forEach { field ->
            if (
                    field.name == name
                    && (type == null || type == field.type)
            ) {
                field.isAccessible = true
                return field
            }
        }

        if (findInSpuer && clazz.superclass != null) {
            return findFieldInClass(clazz.superclass, name, findInSpuer, type)
        } else {
            throw ReflectException("[ReflectUtil] Not Found Field($name) in Clazz $clazz")
        }
    }

    /**
     * 在指定的 Class [clazz] 中查找方法 [name]
     *
     * [parameterTypes] 参数类型数组
     * [args] 参数实例
     * [findInSpuer] 是否查找父类， 默认为 true
     * [retType] 查找 field 的类型
     */
    fun findMethodInClass(clazz: Class<*>, name: String, parameterTypes: Array<Class<*>?>? = null, args: Array<Any?>? = null, findInSpuer: Boolean = true, retType: Class<*>? = null) : Method {
        val methods = clazz.declaredMethods
        methods.forEach { method ->
            if (
                    method.name == name
                    && (retType == null || retType.isAssignableFrom(method.returnType))
                    && isMatchParameterTypes(method.parameterTypes, parameterTypes, args)
            ) {
                method.isAccessible = true
                return method
            }
        }

        if (findInSpuer && clazz.superclass != null) {
            return findMethodInClass(clazz.superclass, name, parameterTypes, args, findInSpuer, retType)
        } else {
            throw ReflectException("[ReflectUtil] Not Found Method($name) in Clazz $clazz")
        }
    }

    /**
     * 通过 参数类型列表[fromParameterTypes] 和 调用参数的对象[fromArgs] 配合起来 来查询和 原参数列表[parameterTypes] 是否匹配
     *
     * 参数类型列表[fromParameterTypes] 的长度必须小于或等于 原参数列表[parameterTypes] 的长度
     * 调用参数的对象[fromArgs] 的长度也必须小于或等于 原参数列表[parameterTypes] 的长度
     */
    fun isMatchParameterTypes(parameterTypes: Array<Class<*>>, fromParameterTypes: Array<Class<*>?>?, fromArgs: Array<Any?>?): Boolean {
        val typs = fromParameterTypes ?: emptyArray()
        val args = fromArgs ?: emptyArray()
//        if (parameterTypes.isEmpty() && typs.isEmpty() && args.isEmpty()) {
//            /**
//             * 无参数
//             */
//            return true
//        }
        val size = parameterTypes.size
//        if (size != typs.size && size != args.size) {
//            /**
//             * 至少要又一个参数的长度和目标的长度相同
//             */
//            return false
//        }

        if (size < typs.size || size < args.size) {
            /**
             * 目标参数少于查找参数个数
             */
            return false
        }

        for (index in 0 until size) {
            val realType = parameterTypes[index]
            val fromType = typs.elementAtOrNull(index)
            if (fromType != null && realType != fromType) {
                /**
                 * 指定了类型，类型不匹配则结果不匹配
                 */
                return false
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
    fun findConstructorInClass(clazz: Class<*>, parameterTypes: Array<Class<*>?>? = null, args: Array<Any?>? = null) : Constructor<*> {
        val constructors = clazz.declaredConstructors
        constructors.forEach { constructor ->
            if (
                    isMatchParameterTypes(constructor.parameterTypes, parameterTypes, args)
            ) {
                constructor.isAccessible = true
                return constructor
            }
        }

        throw ReflectException("[ReflectUtil] Not Constructor in Clazz $clazz")
    }

    /**
     * 通过反射调用非静态方法
     */
    fun invokeDynamic(fromObj: Any, methodName: String, args: Array<Any?>? = null, parameterTypes: Array<Class<*>?>? = null, retType: Class<*>? = null): Any? {
        val invokeArgs = args?: emptyArray()
        return findMethodInClass(fromObj::class.java, methodName, parameterTypes, args, true, retType).invoke(fromObj, *invokeArgs)
    }

    /**
     * 通过反射调用静态方法
     */
    fun invokeStatic(fromClass: Class<*>, methodName: String, args: Array<Any?>? = null, parameterTypes: Array<Class<*>?>? = null, retType: Class<*>? = null): Any? {
        val invokeArgs = args?: emptyArray()
        return findMethodInClass(fromClass, methodName, parameterTypes, args, true, retType).invoke(fromClass, *invokeArgs)
    }

    /**
     * 通过反射构建新的实例对象
     */
    fun newInstance(fromClass: Class<*>, args: Array<Any?>? = null, parameterTypes: Array<Class<*>?>? = null): Any? {
        val invokeArgs = args?: emptyArray()
        return findConstructorInClass(fromClass, parameterTypes, args).newInstance(*invokeArgs)
    }

    fun getBooleanField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Boolean {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).getBoolean(fromObj)
    }

    fun getStaticBooleanField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Boolean {
        return findFieldInClass(fromClass, name, findInSpuer, type).getBoolean(fromClass)
    }

    fun getByteField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Byte {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).getByte(fromObj)
    }

    fun getStaticByteField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Byte {
        return findFieldInClass(fromClass, name, findInSpuer, type).getByte(fromClass)
    }

    fun getCharField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Char {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).getChar(fromObj)
    }

    fun getStaticCharField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Char {
        return findFieldInClass(fromClass, name, findInSpuer, type).getChar(fromClass)
    }

    fun getDoubleField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Double {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).getDouble(fromObj)
    }

    fun getStaticDoubleField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Double {
        return findFieldInClass(fromClass, name, findInSpuer, type).getDouble(fromClass)
    }

    fun getFloatField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Float {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).getFloat(fromObj)
    }

    fun getStaticFloatField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Float {
        return findFieldInClass(fromClass, name, findInSpuer, type).getFloat(fromClass)
    }

    fun getIntField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Int {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).getInt(fromObj)
    }

    fun getStaticIntField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Int {
        return findFieldInClass(fromClass, name, findInSpuer, type).getInt(fromClass)
    }

    fun getLongField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Long {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).getLong(fromObj)
    }

    fun getStaticLongField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Long {
        return findFieldInClass(fromClass, name, findInSpuer, type).getLong(fromClass)
    }

    fun getShortField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Short {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).getShort(fromObj)
    }

    fun getStaticShortField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Short {
        return findFieldInClass(fromClass, name, findInSpuer, type).getShort(fromClass)
    }

    fun getObjectField(fromObj: Any, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Any? {
        return findFieldInClass(fromObj::class.java, name, findInSpuer, type).get(fromObj)
    }

    fun getStaticObjectField(fromClass: Class<*>, name: String, findInSpuer: Boolean = true, type: Class<*>? = null) : Any? {
        return findFieldInClass(fromClass, name, findInSpuer, type).get(fromClass)
    }

    fun setBooleanField(fromObj: Any, name: String, value: Boolean, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).setBoolean(fromObj, value)
    }

    fun setStaticBooleanField(fromClass: Class<*>, name: String, value: Boolean, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).setBoolean(fromClass, value)
    }

    fun setByteField(fromObj: Any, name: String, value: Byte, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).setByte(fromObj, value)
    }

    fun setStaticByteField(fromClass: Class<*>, name: String, value: Byte, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).setByte(fromClass, value)
    }

    fun setCharField(fromObj: Any, name: String, value: Char, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).setChar(fromObj, value)
    }

    fun setStaticCharField(fromClass: Class<*>, name: String, value: Char, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).setChar(fromClass, value)
    }

    fun setDoubleField(fromObj: Any, name: String, value: Double, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).setDouble(fromObj, value)
    }

    fun setStaticDoubleField(fromClass: Class<*>, name: String, value: Double, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).setDouble(fromClass, value)
    }

    fun setFloatField(fromObj: Any, name: String, value: Float, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).setFloat(fromObj, value)
    }

    fun setStaticFloatField(fromClass: Class<*>, name: String, value: Float, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).setFloat(fromClass, value)
    }

    fun setIntField(fromObj: Any, name: String, value: Int, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).setInt(fromObj, value)
    }

    fun setStaticIntField(fromClass: Class<*>, name: String, value: Int, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).setInt(fromClass, value)
    }

    fun setLongField(fromObj: Any, name: String, value: Long, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).setLong(fromObj, value)
    }

    fun setStaticLongField(fromClass: Class<*>, name: String, value: Long, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).setLong(fromClass, value)
    }

    fun setShortField(fromObj: Any, name: String, value: Short, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).setShort(fromObj, value)
    }

    fun setStaticShortField(fromClass: Class<*>, name: String, value: Short, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).setShort(fromClass, value)
    }

    fun setObjectField(fromObj: Any, name: String, value: Any?, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromObj::class.java, name, findInSpuer, type).set(fromObj, value)
    }

    fun setStaticObjectField(fromClass: Class<*>, name: String, value: Any?, findInSpuer: Boolean = true, type: Class<*>? = null) {
        ReflectUtil.findFieldInClass(fromClass, name, findInSpuer, type).set(fromClass, value)
    }
}
