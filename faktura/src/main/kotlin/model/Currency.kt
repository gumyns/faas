package model

import generated.CurrCodeType

enum class Currency(val jpkCurrency: CurrCodeType) {
	PLN(CurrCodeType.PLN),
	USD(CurrCodeType.USD),
	EUR(CurrCodeType.EUR)
}