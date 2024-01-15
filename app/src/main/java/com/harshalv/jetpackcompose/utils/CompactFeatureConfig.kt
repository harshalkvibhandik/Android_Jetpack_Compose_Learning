package com.harshalv.jetpackcompose.utils

data class Disabled(val disable: Boolean = true)

enum class SubFeatureKey(val jsonKey: String) {
    ACCOUNT_TRANSACTION("account_transaction")
}

data class CompactFeatureConfig(
    private val enable: Map<String, Any>?,
    private val disable: Disabled?
) {
    fun isFeatureEnable() = enable.isNullOrEmpty().not()
    fun isSubFeatureEnable(key: SubFeatureKey): Boolean {
        return enable?.get(key.jsonKey) == true
    }
}