package com.github.robsonbittencourt.financialgoals.commons;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalHelperTest {

	@Test
	public void shouldCreateBigDecimalWithDefaultScale() {
		BigDecimal value = new BigDecimal("10");

		BigDecimal newValue = BigDecimalHelper.setDefaultScale(value);

		assertEquals(new BigDecimal("10.0000"), newValue);
	}

	@Test
	public void shouldUseHalfUpToRoundingValue() {
		BigDecimal value = new BigDecimal("10.99866");

		BigDecimal newValue = BigDecimalHelper.setDefaultScale(value);

		assertEquals(new BigDecimal("10.9987"), newValue);
	}

}
