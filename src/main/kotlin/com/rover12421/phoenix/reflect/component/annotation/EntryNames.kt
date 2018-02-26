package com.rover12421.phoenix.reflect.component.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 反射节点名字，可以是多个
 */
@Target(AnnotationTarget.FIELD)
@Retention(RetentionPolicy.RUNTIME)
annotation class EntryNames(vararg val value: String)