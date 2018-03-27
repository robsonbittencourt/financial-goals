package com.github.robsonbittencourt.financialgoals.commons;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.github.robsonbittencourt.financialgoals.asset.InvestmentReturn;

public class MoneyMathTest {
	
	@Test
	public void shouldCalculateDiferenceInValueAndInPercentBetweenFirstAndSecondNumber() {
		BigDecimal number1 = valueOf(500);
		BigDecimal number2 = valueOf(550);

		InvestmentReturn investmentReturn = MoneyMath.calculateInvestmentReturn(number1, number2);
		
		assertEquals(new BigDecimal("50.0000"), investmentReturn.getValue());
		assertEquals(new BigDecimal("0.1000"), investmentReturn.getPercent());
	}

}
