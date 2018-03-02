package com.rover12421.phoenix.reflect.component.annotation

import kotlin.reflect.KClass

/**
 * [FromClassByClass] 和 [FromClassByString] 只能使用一个
 * 同时使用， [FromClassByClass] 优先匹配
 *
 * field 上使用，忽略 class 上的注释
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FromClassByClass(vararg val value: KClass<*>)

/**
 * [FromClassByClass] 和 [FromClassByString] 只能使用一个
 * 同时使用， [FromClassByClass] 优先匹配
 *
 * field 上使用，忽略 class 上的注释
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FromClassByString(vararg val value: String)
