package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvestmentReturn {
	
	private BigDecimal value;
	private BigDecimal percent;
	
}
