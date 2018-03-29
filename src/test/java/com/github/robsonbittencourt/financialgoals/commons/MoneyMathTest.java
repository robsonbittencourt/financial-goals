package com.github.robsonbittencourt.financialgoals.commons;

import static com.github.robsonbittencourt.financialgoals.util.BigDecimalMatcher.equalsBigDecimal;
import static java.math.BigDecimal.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import com.github.robsonbittencourt.financialgoals.asset.InvestmentReturn;

public class MoneyMathTest {
	
	@Test
	public void shouldCalculateDiferenceInValueAndInPercentBetweenFirstAndSecondNumber() {
		BigDecimal number1 = valueOf(500);
		BigDecimal number2 = valueOf(550);

		InvestmentReturn investmentReturn = MoneyMath.calculateInvestmentReturn(number1, number2);
		
		assertThat(valueOf(50), is(equalsBigDecimal(investmentReturn.getValue())));
		assertThat(valueOf(0.1), is(equalsBigDecimal(investmentReturn.getPercent())));
	}

}
