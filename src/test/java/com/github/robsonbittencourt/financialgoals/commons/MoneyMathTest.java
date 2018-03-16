package com.github.robsonbittencourt.financialgoals.commons;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class MoneyMathTest {
	
	
	@Test
	public void shouldCalculateDiferenceInPercentBetweenFirstAndSecondNumber() {
		BigDecimal number1 = BigDecimal.valueOf(500);
		BigDecimal number2 = BigDecimal.valueOf(550);

		BigDecimal result = MoneyMath.calculateDiferenceInPercent(number1, number2);
		
		assertEquals(BigDecimal.valueOf(0.1), result);
	}

}
