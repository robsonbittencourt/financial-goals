package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetSummary {
	
	private BigDecimal grossValue;
	private Profit grossProfit;
	private BigDecimal netValue;
	private Profit netProfit;
	private Profit realProfit;
	
}
