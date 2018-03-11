package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;

class AssetUpdate {
	
	private BigDecimal grossValue;
	private BigDecimal rates;
	private BigDecimal taxes;
	
	public AssetUpdate(BigDecimal grossValue, BigDecimal rates, BigDecimal taxes) {
		this.grossValue = grossValue;
		this.rates = rates;
		this.taxes = taxes;
	}

	public BigDecimal getGrossValue() {
		return grossValue;
	}
	
	public BigDecimal getRates() {
		return rates;
	}
	
	public BigDecimal getTaxes() {
		return taxes;
	}
	
}
