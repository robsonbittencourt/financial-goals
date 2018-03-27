package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class InvestmentReturn {
	
	private BigDecimal value;
	private BigDecimal percent;
	
	public InvestmentReturn(BigDecimal value, BigDecimal percent) {
		this.value = value.setScale(2);
		this.percent = percent.setScale(2);
	}
	
}
