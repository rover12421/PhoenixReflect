package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.AccessibleObject

abstract class AbsEntry<out T : AccessibleObject>(open val from: T)