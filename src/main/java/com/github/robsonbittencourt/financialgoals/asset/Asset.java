package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class Asset {

	private LocalDate initialDate;
	private String description;
	private ProfitType profitType;
	private BigDecimal initialValue;

	@Getter(AccessLevel.NONE)
	private AssetUpdateManager assetUpdateManager = new AssetUpdateManager();

	public Asset(LocalDate initialDate, String description, ProfitType profitType, BigDecimal initialValue) {
		this.initialDate = initialDate;
		this.description = description;
		this.profitType = profitType;
		this.initialValue = initialValue;
		
		assetUpdateManager.updateGrossValue(initialDate, initialValue);
	}

	public AssetUpdate getLastUpdate() {
		return assetUpdateManager.getLastUpdate();
	}

	public void makeTransaction(LocalDate date, BigDecimal depositedValue) {
		assetUpdateManager.makeTransaction(date, depositedValue);
	}

	public void updateValues(LocalDate date, BigDecimal grossValue, BigDecimal rates, BigDecimal taxes) {
		assetUpdateManager.updateValues(date, grossValue, rates, taxes);
	}

	public AssetSummary getResume() {
		return AssetSummary.builder()
						   .grossValue(assetUpdateManager.getLastUpdate().getGrossValue())
						   .grossProfit(new GrossProfit(assetUpdateManager))
						   .netValue(assetUpdateManager.getLastUpdate().getNetValue())
						   .netProfit(new NetProfit(assetUpdateManager))
						   .realProfit(new RealProfit(assetUpdateManager))
						   .build();
	}
	
	public AssetSummary getResume(LocalDate initialDate, LocalDate finalDate) {
		return AssetSummary.builder()
						   .grossValue(assetUpdateManager.getLastUpdateByDate(finalDate).getGrossValue())
						   .grossProfit(new GrossProfit(assetUpdateManager, initialDate, finalDate))
						   .netValue(assetUpdateManager.getLastUpdateByDate(finalDate).getNetValue())
						   .netProfit(new NetProfit(assetUpdateManager, initialDate, finalDate))
						   .realProfit(new RealProfit(assetUpdateManager, initialDate, finalDate))
						   .build();
	}

}
