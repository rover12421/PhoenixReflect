## Phoneix Reflect
### 一个用于 Kotlin 的反射库

----

- Gradle 配置

``` groovy
repositories {
    maven { url 'https://oss.sonatype.org/service/local/repositories/releases/content/' }
}

dependencies {
    compile 'com.rover12421.phoenix:PhoenixReflect:3.0'
}
```

- 测试

``` java
package com.rover12421.phoenix.reflect.test;

public class OriginClass {
    static String s1 = "s1";
    public int i1 = 100;

    private void say(String info) {
        System.out.println(info);
    }

    public static String getS1() {
        return s1;
    }

    public String test(int i1, String str, Integer i2, Object ... any) {
        String ret = "i1=" + i1 + ", str=" + str + ", i2=" + i2 + ", any=[";
        for (Object o : any) {
            ret += ", " + o;
        }
        ret += "]";
        return ret;
    }
}
```

``` kotlin
package com.rover12421.phoenix.reflect.test

import com.rover12421.phoenix.reflect.component.BaseReflectComponent
import com.rover12421.phoenix.reflect.component.annotation.EntryNames
import com.rover12421.phoenix.reflect.component.annotation.FromClassByString
import com.rover12421.phoenix.reflect.component.annotation.ParameterTypesByClass
import com.rover12421.phoenix.reflect.component.entry.IntEntry
import com.rover12421.phoenix.reflect.component.entry.MethodEntry
import com.rover12421.phoenix.reflect.component.entry.ObjectEntry
import com.rover12421.phoenix.reflect.component.entry.StaticMethodEntry

@FromClassByString("com.rover12421.phoenix.reflect.test.OriginClass")
object OriginRefClass : BaseReflectComponent() {
    @EntryNames("S1", "s1")
    var s: ObjectEntry? = null
    var i1: IntEntry? = null

    var say: MethodEntry? = null
    var getS1: StaticMethodEntry? = null

    @ParameterTypesByClass(Int::class, String::class, Integer::class, Array<Any>::class)
    private var test: MethodEntry? = null

    fun test() {
        val originClass = OriginClass()
        println(s?.getValue(originClass))
        println(i1?.getValue(originClass))
        say?.invoke(originClass, "hhhh")
        println(getS1?.invoke())
        println(test?.invoke(originClass, 123, "kkk", 456, arrayOf(11111, this, originClass)))
    }
}

fun main(args: Array<String>) {
    OriginRefClass.test()
}
```

out:
``` text
s1
100
hhhh
s1
i1=123, str=kkk, i2=456, any=[, 11111, com.rover12421.phoenix.reflect.test.OriginRefClass@37a71e93, com.rover12421.phoenix.reflect.test.OriginClass@7e6cbb7a]
```

试试另一种风格
```kotlin
        field {
            fieldName("S1", "s1")
            fromClass(OriginClass::class.java)
            catchException(true)
        }.getValue(OriginClass()).apply { println(this) }

        method {
            methodName("say")
            fromClass("com.rover12421.phoenix.reflect.test.OriginClass")
            lazyMode(true)
        }.invokeStatic("this is a static method!")
```