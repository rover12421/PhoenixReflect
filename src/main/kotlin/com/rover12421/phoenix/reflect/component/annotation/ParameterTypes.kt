package com.rover12421.phoenix.reflect.component.annotation

import kotlin.reflect.KClass

/**
 * [ParameterTypesByClass] 和 [ParameterTypesByString] 只能使用一个
 * 同时使用，只有 [ParameterTypesByClass] 生效
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ParameterTypesByClass(vararg val value: KClass<*>)

/**
 * [ParameterTypesByClass] 和 [ParameterTypesByString] 只能使用一个
 * 同时使用，只有 [ParameterTypesByClass] 生效
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ParameterTypesByString(vararg val value: String)