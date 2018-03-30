package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class NetProfit implements Profit {
	
	private Profit grossProfit;
	private AssetUpdate firstUpdate;
	private AssetUpdate lastUpdate;
	
	public NetProfit(AssetUpdateManager assetUpdateManager) {
		this.grossProfit = new GrossProfit(assetUpdateManager);
		this.firstUpdate = assetUpdateManager.getFirstUpdate();
		this.lastUpdate = assetUpdateManager.getLastUpdate();
	}

	public NetProfit(AssetUpdateManager assetUpdateManager, LocalDate initialDate, LocalDate finalDate) {
		this.grossProfit = new GrossProfit(assetUpdateManager, initialDate, finalDate);
		this.firstUpdate = assetUpdateManager.getFirstUpdateByDate(initialDate);
		this.lastUpdate = assetUpdateManager.getLastUpdateByDate(finalDate);
	}

	@Override
	public BigDecimal getValue() {
		BigDecimal rates = lastUpdate.getRates().subtract(firstUpdate.getRates());
		BigDecimal taxes = lastUpdate.getTaxes().subtract(firstUpdate.getTaxes());
		
		return grossProfit.getValue().subtract(rates).subtract(taxes);
	}

	@Override
	public BigDecimal getPercent() {
		return getValue().multiply(grossProfit.getPercent()).divide(grossProfit.getValue(), 4, RoundingMode.HALF_UP);
	}

}
