package com.rover12421.phoenix.reflect.component.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

/**
 * [MethodParamsByClass] 和 [MethodParamsByString] 只能使用一个
 * 同时使用，只有 [MethodParamsByClass] 生效
 */
@Target(AnnotationTarget.FIELD)
@Retention(RetentionPolicy.RUNTIME)
annotation class MethodParamsByClass(vararg val value: KClass<*>)

/**
 * [MethodParamsByClass] 和 [MethodParamsByString] 只能使用一个
 * 同时使用，只有 [MethodParamsByClass] 生效
 */
@Target(AnnotationTarget.FIELD)
@Retention(RetentionPolicy.RUNTIME)
annotation class MethodParamsByString(vararg val value: String)