package com.harshalv.jetpackcompose.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CompactFeatureConfigTest {


    @Test
    fun `given enable map is null, when isFeatureEnable is called, then return false`() {
        val enable = null
        val disable = Disabled()
        val compactFeatureConfig = CompactFeatureConfig(enable, disable)
        val result = compactFeatureConfig.isFeatureEnable()
        assertFalse(result)
    }

    @Test
    fun `given enable map is not null and not empty, when isFeatureEnable is called, then return true`() {
        val enable = mapOf("feature" to true)
        val disable = Disabled()
        val compactFeatureConfig = CompactFeatureConfig(enable, disable)
        val result = compactFeatureConfig.isFeatureEnable()
        assertTrue(result)
    }

    @Test
    fun `given enable map does not contain subFeatureKey, when isSubFeatureEnable is called, then return false`() {
        val enable = mapOf("otherFeature" to true)
        val disable = Disabled()
        val compactFeatureConfig = CompactFeatureConfig(enable, disable)
        val result = compactFeatureConfig.isSubFeatureEnable(SubFeatureKey.ACCOUNT_TRANSACTION)
        assertFalse(result)
    }

    @Test
    fun `given enable map contains subFeatureKey and it's true, when isSubFeatureEnable is called, then return true`() {
        val enable = mapOf(SubFeatureKey.ACCOUNT_TRANSACTION.jsonKey to true)
        val disable = Disabled()
        val compactFeatureConfig = CompactFeatureConfig(enable, disable)
        val result = compactFeatureConfig.isSubFeatureEnable(SubFeatureKey.ACCOUNT_TRANSACTION)
        assertTrue(result)
    }

    @Test
    fun `given enable map contains subFeatureKey and it's false, when isSubFeatureEnable is called, then return false`() {
        val enable = mapOf(SubFeatureKey.ACCOUNT_TRANSACTION.jsonKey to false)
        val disable = Disabled()
        val compactFeatureConfig = CompactFeatureConfig(enable, disable)
        val result = compactFeatureConfig.isSubFeatureEnable(SubFeatureKey.ACCOUNT_TRANSACTION)
        assertFalse(result)
    }

}