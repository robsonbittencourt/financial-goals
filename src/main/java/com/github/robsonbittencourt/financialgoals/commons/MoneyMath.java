package com.github.robsonbittencourt.financialgoals.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.github.robsonbittencourt.financialgoals.asset.InvestmentReturn;

public class MoneyMath {

	public static InvestmentReturn calculateInvestmentReturn(BigDecimal initialValue, BigDecimal finalValue) {
		BigDecimal value = finalValue.subtract(initialValue);
		BigDecimal percent = finalValue.divide(initialValue, 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
		
		return new InvestmentReturn(value, percent);
	}

}
