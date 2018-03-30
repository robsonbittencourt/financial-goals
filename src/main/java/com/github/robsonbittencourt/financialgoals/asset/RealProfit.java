package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.indicators.IndicatorType.INFLATION;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.github.robsonbittencourt.financialgoals.indicators.Indicator;

public class RealProfit implements Profit {
	
	private Profit grossProfit;
	private Profit netProfit;
	private LocalDate initialDate;
	private LocalDate finalDate;
	
	public RealProfit(AssetUpdateManager assetUpdateManager) {
		this.initialDate = assetUpdateManager.getFirstUpdate().getDate();
		this.finalDate = assetUpdateManager.getLastUpdate().getDate();
		this.grossProfit = new GrossProfit(assetUpdateManager);
		this.netProfit = new NetProfit(assetUpdateManager);
	}
	
	public RealProfit(AssetUpdateManager assetUpdateManager, LocalDate initialDate, LocalDate finalDate) {
		this.initialDate = initialDate;
		this.finalDate = finalDate;
		this.grossProfit = new GrossProfit(assetUpdateManager, initialDate, finalDate);
		this.netProfit = new NetProfit(assetUpdateManager, initialDate, finalDate);
	}

	@Override
	public BigDecimal getValue() {
		BigDecimal inflation = new Indicator().getIndicator(INFLATION, initialDate, finalDate);
		BigDecimal inflationDiscount = grossProfit.getValue().multiply(inflation);
		
		return netProfit.getValue().subtract(inflationDiscount);
	}

	@Override
	public BigDecimal getPercent() {
		return getValue().multiply(grossProfit.getPercent()).divide(grossProfit.getValue(), 4, RoundingMode.HALF_UP);
	}

}
