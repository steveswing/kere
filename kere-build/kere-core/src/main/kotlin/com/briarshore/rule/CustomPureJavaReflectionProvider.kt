package com.briarshore.rule

import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException

/**
 * Class: CustomPureJavaReflectionProvider
 */
class CustomPureJavaReflectionProvider : PureJavaReflectionProvider() {

    override fun writeField(`object`: Any, fieldName: String, value: Any?, definedIn: Class<Any>?) {
        if (null != definedIn) {
            val methods = definedIn.methods.filter(
                    { m -> null != m && null != value && m.name.startsWith("set") && m.name.endsWith(fieldName) && assignableFrom(m.parameterTypes, value.javaClass) }).toSet()

            //            final Set<Method> methods = Sets.filter(Sets.newHashSet(definedIn.getMethods()), new Predicate<Method>() {
            //                @Override
            //                public boolean test(@Nullable final Method m) {
            //                    return null != m && null != value && Strings.startsWith(m.getName(), "set") && Strings.endsWith(m.getName(), Strings.capitalize(fieldName)) && assignableFrom(m
            // .getParameterTypes(),
            //                            value.getClass());
            //                }
            //
            //                private boolean assignableFrom(final Class<?>[] parameterTypes, final Class cls) {
            //                    for (final Class<?> parameterType : parameterTypes) {
            //                        if (parameterType.isAssignableFrom(cls)) {
            //                            return true;
            //                        }
            //                    }
            //                    return false;
            //                }
            //            });

            methods.forEach {
                log.debug("calling {}", it.name)
                it.invoke(`object`, value)
            }
            if (!methods.isEmpty()) {
                for (method in methods) {
                    try {
                        log.debug("calling {}", method.getName())
                        method.invoke(`object`, value)
                        return
                    } catch (ignored: IllegalAccessException) {
                    } catch (ignored: InvocationTargetException) {
                    }
                }
            } else {
                val f = definedIn.declaredFields.filter({ n -> n.name.equals(fieldName, true) }).firstOrNull()

                //                final Field f = FieldUtils.getDeclaredField(definedIn, fieldName, true);
                //                if (null != f) {
                if (null != f) {
                    if (Collection::class.java.isAssignableFrom(f.type)) {
                        log.debug("setter for {} not found", fieldName)
                    }
                } else {
                    log.debug("getDeclaredField {} {} is not found", definedIn.simpleName, fieldName)
                }
            }
        } else {
            return
        }

        super.writeField(`object`, fieldName, value, definedIn)
    }

    companion object {
        private val log = LoggerFactory.getLogger(CustomPureJavaReflectionProvider::class.java)
    }

    private fun assignableFrom(parameterTypes: Array<Class<*>>, cls: Class<*>): Boolean {
        return parameterTypes.any { it.isAssignableFrom(cls) }
    }
}
