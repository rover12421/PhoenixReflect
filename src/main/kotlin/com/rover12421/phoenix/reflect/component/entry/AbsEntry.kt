package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.ReflectWrapper

abstract class AbsEntry<out T : ReflectWrapper>(open val from: T)