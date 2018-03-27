package com.github.robsonbittencourt.financialgoals.commons;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

public class BigDecimalHelper {
	
	private static final int DEFAULT_SCALE = 4;

	public static BigDecimal setDefaultScale(BigDecimal value) {
		return value == null ? value : value.setScale(DEFAULT_SCALE, HALF_UP);
	}
	
}
