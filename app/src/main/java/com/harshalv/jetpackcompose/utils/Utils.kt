package com.harshalv.jetpackcompose.utils

data class DisplayText(val text: String, val accessibilityText: String = text)

enum class MainBalanceType {
    LEDGER,
    AVAILABLE,
    OUTSTANDING,
    AVAILABLE_CREDIT,
    USED_CREDIT
}

class Utils {
    fun getBalanceTypeTitle(balanceType: MainBalanceType) = when (balanceType) {
        MainBalanceType.AVAILABLE -> DisplayText(
            text = "account_summary_available_balance",
            accessibilityText = "account_summary_available_balance_accessbilityText"
        )

        MainBalanceType.LEDGER -> DisplayText(
            text = "account_summary_used_balance",
            accessibilityText = "account_summary_used_balance_accessobility"
        )

        else -> DisplayText(
            text = "account_summary_used_balance",
            accessibilityText = "account_summary_used_balance_accessobility"
        )
    }
}