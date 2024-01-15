package com.harshalv.jetpackcompose.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsTest {

    private val utils = Utils()

    @Test
    fun `getBalanceTypeTitle for AVAILABLE should return correct DisplayText`() {
        val displayText = utils.getBalanceTypeTitle(MainBalanceType.AVAILABLE)
        assertEquals("account_summary_available_balance", displayText.text)
        assertEquals("account_summary_available_balance_accessibilityText", displayText.accessibilityText)
    }

    @Test
    fun `getBalanceTypeTitle for LEDGER should return correct DisplayText`() {
        val displayText = utils.getBalanceTypeTitle(MainBalanceType.LEDGER)
        assertEquals("account_summary_used_balance", displayText.text)
        assertEquals("account_summary_used_balance_accessibility", displayText.accessibilityText)
    }

    @Test
    fun `getBalanceTypeTitle for OUTSTANDING should return correct DisplayText`() {
        val displayText = utils.getBalanceTypeTitle(MainBalanceType.OUTSTANDING)
        assertEquals("account_summary_used_balance", displayText.text)
        assertEquals("account_summary_used_balance_accessibility", displayText.accessibilityText)
    }

    @Test
    fun `getBalanceTypeTitle for AVAILABLE_CREDIT should return correct DisplayText`() {
        val displayText = utils.getBalanceTypeTitle(MainBalanceType.AVAILABLE_CREDIT)
        assertEquals("account_summary_used_balance", displayText.text)
        assertEquals("account_summary_used_balance_accessibility", displayText.accessibilityText)
    }

    @Test
    fun `getBalanceTypeTitle for USED_CREDIT should return correct DisplayText`() {
        val displayText = utils.getBalanceTypeTitle(MainBalanceType.USED_CREDIT)
        assertEquals("account_summary_used_balance", displayText.text)
        assertEquals("account_summary_used_balance_accessibility", displayText.accessibilityText)
    }

}