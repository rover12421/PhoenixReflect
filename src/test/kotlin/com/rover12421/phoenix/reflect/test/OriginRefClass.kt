package com.rover12421.phoenix.reflect.test

import com.rover12421.phoenix.reflect.component.BaseReflectComponent
import com.rover12421.phoenix.reflect.component.annotation.EntryNames
import com.rover12421.phoenix.reflect.component.annotation.FromClassByString
import com.rover12421.phoenix.reflect.component.annotation.ParameterTypesByClass
import com.rover12421.phoenix.reflect.component.entry.IntEntry
import com.rover12421.phoenix.reflect.component.entry.MethodEntry
import com.rover12421.phoenix.reflect.component.entry.ObjectEntry
import com.rover12421.phoenix.reflect.component.entry.StaticMethodEntry

@FromClassByString("com.rover12421.phoenix.reflect.test.OriginClass")
object OriginRefClass : BaseReflectComponent() {
    @EntryNames("S1", "s1")
    var s: ObjectEntry<String>? = null
    var i1: IntEntry? = null

    var say: MethodEntry<Void>? = null
    var getS1: StaticMethodEntry<String>? = null

    @ParameterTypesByClass(Int::class, String::class, Integer::class, Array<Any>::class)
    private var test: MethodEntry<String>? = null

    fun test() {
        println(this)

        val originClass = OriginClass()
        println("=======")
        println(s?.getValue(originClass))
        println(i1?.getValue(originClass))
        say?.invoke(originClass, "hhhh")
        println(getS1?.invoke())
        println(test?.invoke(originClass, 123, "kkk", 456, arrayOf(11111, this, originClass)))
    }
}

fun main(args: Array<String>) {
    OriginRefClass.test()
}