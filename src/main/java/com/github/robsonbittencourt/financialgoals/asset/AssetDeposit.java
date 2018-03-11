package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.time.LocalDate;

class AssetDeposit {
	
	private LocalDate date;
	private BigDecimal value;
	
	public AssetDeposit(LocalDate date, BigDecimal value) {
		this.date = date;
		this.value = value;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public BigDecimal getValue() {
		return value;
	}

}
