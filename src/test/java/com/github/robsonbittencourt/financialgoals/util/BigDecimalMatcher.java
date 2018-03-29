package com.github.robsonbittencourt.financialgoals.util;

import java.math.BigDecimal;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class BigDecimalMatcher extends TypeSafeDiagnosingMatcher<BigDecimal> {

	private final BigDecimal comparedValue;

	public BigDecimalMatcher(BigDecimal comparedValue) {
		this.comparedValue = comparedValue;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("equals BigDecimal, striping zeros");
	}

	@Override
	protected boolean matchesSafely(BigDecimal value, Description description) {
		description.appendValue(value).appendText(" is different from a ").appendValue(comparedValue);
		return value.compareTo(this.comparedValue) == 0;
	}

	public static BigDecimalMatcher equalsBigDecimal(BigDecimal comparedValue) {
		return new BigDecimalMatcher(comparedValue);
	}

}
