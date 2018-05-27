package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.AccessibleObject

abstract class AbsEntry<out From : AccessibleObject, T>(open val from: From)