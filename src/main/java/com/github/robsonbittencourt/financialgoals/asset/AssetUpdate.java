package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.commons.BigDecimalHelper.setDefaultScale;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

@Getter
class AssetUpdate implements Comparable<AssetUpdate> {
	
	private LocalDate date;
	private BigDecimal grossValue;
	private BigDecimal rates;
	private BigDecimal taxes;
	
	public AssetUpdate(LocalDate date, BigDecimal grossValue, BigDecimal rates, BigDecimal taxes) {
		this.date = date;
		this.grossValue = setDefaultScale(grossValue);
		this.rates = setDefaultScale(rates);
		this.taxes = setDefaultScale(taxes);
	}
	
	@Override
	public int compareTo(AssetUpdate update) {
		return this.date.compareTo(update.getDate());
	}
	
}
