package com.github.robsonbittencourt.financialgoals.commons;

import java.math.BigDecimal;

public class MoneyMath {

	public static BigDecimal calculateDiferenceInPercent(BigDecimal number1, BigDecimal number2) {
		return number2.divide(number1).subtract(BigDecimal.ONE);
	}

}
