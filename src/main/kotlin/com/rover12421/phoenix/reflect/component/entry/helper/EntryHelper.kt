package com.rover12421.phoenix.reflect.component.entry.helper

import com.rover12421.phoenix.reflect.component.entry.*

object EntryHelper {
    private val AllEntryClass = arrayOf(
            BooleanEntry::class.java,
            DoubleEntry::class.java,
            FloatEntry::class.java,
            IntEntry::class.java,
            LongEntry::class.java,
            ObjectEntry::class.java,
            ShortEntry::class.java,

            StaticBooleanEntry::class.java,
            StaticDoubleEntry::class.java,
            StaticFloatEntry::class.java,
            StaticIntEntry::class.java,
            StaticLongEntry::class.java,
            StaticObjectEntry::class.java,
            StaticShortEntry::class.java,

            ConstructorEntry::class.java,

            MethodEntry::class.java,
            StaticMethodEntry::class.java
    )

    @JvmField val EntryConstructor = AllEntryClass.associateBy({it}, {it.constructors[0]})
}