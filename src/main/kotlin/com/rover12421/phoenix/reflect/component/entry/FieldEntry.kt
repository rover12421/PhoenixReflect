package com.rover12421.phoenix.reflect.component.entry

import com.rover12421.phoenix.reflect.wrapper.FieldWrapper

abstract class FieldEntry(@JvmField val fieldWrapper: FieldWrapper) : AbsEntry<FieldWrapper>(fieldWrapper)
