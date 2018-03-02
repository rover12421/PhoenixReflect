package com.rover12421.phoenix.reflect.component.entry

import java.lang.reflect.Field

abstract class FieldEntry(@JvmField val field: Field) : AbsEntry<Field>(field)
