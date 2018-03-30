package com.github.robsonbittencourt.financialgoals.asset;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GrossProfit implements Profit {
	
	private AssetUpdateManager assetUpdateManager;
	
	private AssetUpdate firstUpdate;
	private AssetUpdate lastUpdate;
	
	public GrossProfit(AssetUpdateManager assetUpdateManager) {
		this.assetUpdateManager = assetUpdateManager;
		this.firstUpdate = assetUpdateManager.getFirstUpdate();
		this.lastUpdate = assetUpdateManager.getLastUpdate();
	}

	public GrossProfit(AssetUpdateManager assetUpdateManager, LocalDate initialDate, LocalDate finalDate) {
		this.assetUpdateManager = assetUpdateManager;
		this.firstUpdate = assetUpdateManager.getFirstUpdateByDate(initialDate);
		this.lastUpdate = assetUpdateManager.getLastUpdateByDate(finalDate);
	}

	@Override
	public BigDecimal getValue() {
		BigDecimal transactionsValue = assetUpdateManager.getUpdates()
														 .stream()
														 .filter(u -> inRange(u.getDate(), firstUpdate.getDate(), lastUpdate.getDate()))
														 .map(u -> u.getTransactionValue())
														 .reduce(ZERO, BigDecimal::add);
		
		return lastUpdate.getGrossValue().subtract(transactionsValue).subtract(firstUpdate.getGrossValue());
	}
	
	private boolean inRange(LocalDate date, LocalDate initialDate, LocalDate finalDate) {
		if (date.equals(initialDate) || date.equals(finalDate)) {
			return true;
		}
		
		return date.isAfter(initialDate) && date.isBefore(finalDate);
	}

	@Override
	public BigDecimal getPercent() {
		return lastUpdate.getShareValue().divide(firstUpdate.getShareValue(), 4, HALF_UP).subtract(BigDecimal.ONE);
	}

}
