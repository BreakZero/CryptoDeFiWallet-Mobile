package com.crypto.core.ui.routers

import androidx.annotation.Keep

enum class ParameterType {
    NONE, PLACEHOLDER, OPTIONAL
}

@Keep
data class Navigator internal constructor(
    private val parameterType: ParameterType,
    private val route: String
) {
    private val params: MutableList<KeyPair<*>> = mutableListOf()

    internal fun addParameter(type: String, value: Any?) {
        params.add(KeyPair(type, value))
    }

    fun route(): String {
        return when (parameterType) {
            ParameterType.NONE -> {
                route
            }
            ParameterType.PLACEHOLDER -> {
                params.let {
                    // e.g. $route/${info.contractAddress}/${info.symbol}
                    buildString {
                        append(route)
                        it.forEach {
                            append("/")
                            append("{${it.key}}")
                        }
                    }
                }
            }
            ParameterType.OPTIONAL -> {
                params.let {
                    // e.g. $route?url={url}&chain={chain}&rpc={rpc}
                    buildString {
                        append(route)
                        append("?")
                        it.forEach {
                            append(it.key)
                            append("={${it.key}}")
                            append("&")
                        }
                    }.removeSuffix("&")
                }
            }
        }
    }

    fun router(): String {
        return when (parameterType) {
            ParameterType.NONE -> {
                route
            }
            ParameterType.PLACEHOLDER -> {
                params.let {
                    // /${info.contractAddress}/${info.symbol}
                    buildString {
                        append(route)
                        it.forEach {
                            append("/")
                            append(it.value)
                        }
                    }
                }
            }
            ParameterType.OPTIONAL -> {
                params.let {
                    buildString {
                        append(route)
                        append("?")
                        it.forEach {
                            append(it.key)
                            append("=")
                            append(it.value)
                            append("&")
                        }
                    }.removeSuffix("&")
                }
            }
        }
    }
}

fun buildNavigator(
    parameterType: ParameterType,
    route: String,
    init: Navigator.() -> Unit = {}
): Navigator {
    return Navigator(parameterType, route).apply(init)
}

class _Parameter(private val navigator: Navigator) {
    infix fun String.to(that: Any?) = navigator.addParameter(this, that)
}

inline fun Navigator.parameter(block: _Parameter.() -> Unit) {
    _Parameter(this).apply(block)
}

private data class KeyPair<T>(
    val key: String,
    val value: T
)
