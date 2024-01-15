package com.harshalv.jetpackcompose.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UtilsTest {

    private val utils = Utils()

    @Test
    fun `getBalanceTypeTitle should return correct DisplayText for AVAILABLE balance type`() {
        val expected = DisplayText(
            text = "account_summary_available_balance",
            accessibilityText = "account_summary_available_balance_accessbilityText"
        )

        val actual = utils.getBalanceTypeTitle(MainBalanceType.AVAILABLE)

        assertEquals(expected, actual)
    }

    @Test
    fun `getBalanceTypeTitle should return correct DisplayText for LEADER balance type`() {
        val expected = DisplayText(
            text = "account_summary_used_balance",
            accessibilityText = "account_summary_used_balance_accessobility"
        )

        val actual = utils.getBalanceTypeTitle(MainBalanceType.LEDGER)

        assertEquals(expected, actual)
    }

    @Test
    fun `getBalanceTypeTitle should return correct DisplayText for other balance types`() {
        val expected = DisplayText(
            text = "account_summary_used_balance",
            accessibilityText = "account_summary_used_balance_accessobility"
        )

        val actualOutstanding = utils.getBalanceTypeTitle(MainBalanceType.OUTSTANDING)
        val actualAvailableCredit = utils.getBalanceTypeTitle(MainBalanceType.AVAILABLE_CREDIT)
        val actualUsedCredit = utils.getBalanceTypeTitle(MainBalanceType.USED_CREDIT)

        assertEquals(expected, actualOutstanding)
        assertEquals(expected, actualAvailableCredit)
        assertEquals(expected, actualUsedCredit)
    }
}