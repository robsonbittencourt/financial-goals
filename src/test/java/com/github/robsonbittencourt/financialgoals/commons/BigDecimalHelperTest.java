package com.github.robsonbittencourt.financialgoals.commons;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalHelperTest {

	@Test
	public void shouldCreateBigDecimalWithDefaultScale() {
		BigDecimal value = new BigDecimal("10");

		BigDecimal newValue = BigDecimalHelper.setDefaultScale(value);

		assertEquals(new BigDecimal("10.00"), newValue);
	}

	@Test
	public void shouldUseHalfUpToRoundingValue() {
		BigDecimal value = new BigDecimal("10.998");

		BigDecimal newValue = BigDecimalHelper.setDefaultScale(value);

		assertEquals(new BigDecimal("11.00"), newValue);
	}

}
