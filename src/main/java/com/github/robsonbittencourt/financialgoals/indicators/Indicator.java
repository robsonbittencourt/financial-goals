package com.github.robsonbittencourt.financialgoals.indicators;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Indicator {
	
	public BigDecimal getIndicator(IndicatorType type, LocalDate date, LocalDate finalDate) {
		//TODO mock
		return new BigDecimal("0.0054").setScale(4);
	}
	
}
