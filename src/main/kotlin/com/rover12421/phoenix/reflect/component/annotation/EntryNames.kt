package com.rover12421.phoenix.reflect.component.annotation

/**
 * 反射节点名字，可以是多个
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class EntryNames(vararg val value: String)