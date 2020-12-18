package com.rover12421.phoenix.reflect.component

/**
 * 如果反射对象类没有继承本类，需手动调用 [ComponentUtil.initComponent] 完成初始化才能使用
 */
@Suppress("LeakingThis")
abstract class BaseReflectComponent {
    init {
        ComponentUtil.initComponent(this)
    }
}