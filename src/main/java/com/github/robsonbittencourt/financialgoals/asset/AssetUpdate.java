package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class AssetUpdate implements Comparable<AssetUpdate> {
	
	private LocalDate date;
	private BigDecimal grossValue;
	private BigDecimal rates;
	private BigDecimal taxes;
	
	@Override
	public int compareTo(AssetUpdate update) {
		return this.date.compareTo(update.getDate());
	}
	
}
