package com.rover12421.phoenix.reflect.component.annotation

import kotlin.reflect.KClass

/**
 * 反射节点类型，可用用于Method的返回值类型，也可以是 filed 的类型
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ReturnTypeByClass(val value: KClass<*>)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ReturnTypeByString(val value: String)