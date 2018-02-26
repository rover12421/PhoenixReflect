package com.rover12421.phoenix.reflect.wrapper

import java.lang.reflect.Field

class FieldWrapper(val field: Field, from: Any, fromClass: Class<*> = from::class.java) : ReflectWrapper(from, fromClass) {
    fun getValueWrapper(any: Any = from) = ObjectWrapper.fromObject(getValue(any))

    fun getValue    (any: Any = from)      = field.get(any)
    fun getInt      (any: Any = from)       = field.getInt(any)
    fun getByte     (any: Any = from)      = field.getByte(any)
    fun getBoolean  (any: Any = from)   = field.getBoolean(any)
    fun getChar     (any: Any = from)      = field.getChar(any)
    fun getDouble   (any: Any = from)    = field.getDouble(any)
    fun getFloat    (any: Any = from)     = field.getFloat(any)
    fun getLong     (any: Any = from)      = field.getLong(any)
    fun getShort    (any: Any = from)     = field.getShort(any)

    fun setValue    (any: Any, v: Any)       = field.set         (any, v)
    fun setInt      (any: Any, v: Int)       = field.setInt      (any, v)
    fun setByte     (any: Any, v: Byte)      = field.setByte     (any, v)
    fun setBoolean  (any: Any, v: Boolean)   = field.setBoolean  (any, v)
    fun setChar     (any: Any, v: Char)      = field.setChar     (any, v)
    fun setDouble   (any: Any, v: Double)    = field.setDouble   (any, v)
    fun setFloat    (any: Any, v: Float)     = field.setFloat    (any, v)
    fun setLong     (any: Any, v: Long)      = field.setLong     (any, v)
    fun setShort    (any: Any, v: Short)     = field.setShort    (any, v)

    fun setValue    (v: Any)       = field.set         (from, v)
    fun setInt      (v: Int)       = field.setInt      (from, v)
    fun setByte     (v: Byte)      = field.setByte     (from, v)
    fun setBoolean  (v: Boolean)   = field.setBoolean  (from, v)
    fun setChar     (v: Char)      = field.setChar     (from, v)
    fun setDouble   (v: Double)    = field.setDouble   (from, v)
    fun setFloat    (v: Float)     = field.setFloat    (from, v)
    fun setLong     (v: Long)      = field.setLong     (from, v)
    fun setShort    (v: Short)     = field.setShort    (from, v)
}
