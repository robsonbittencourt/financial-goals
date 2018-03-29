package com.github.robsonbittencourt.financialgoals.commons;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

import com.github.robsonbittencourt.financialgoals.asset.InvestmentReturn;

public class MoneyMath {

	public static InvestmentReturn calculateInvestmentReturn(BigDecimal initialValue, BigDecimal finalValue) {
		BigDecimal value = finalValue.subtract(initialValue);
		BigDecimal percent = finalValue.divide(initialValue, 10, HALF_UP).subtract(BigDecimal.ONE);
		
		return new InvestmentReturn(value, percent);
	}

}
