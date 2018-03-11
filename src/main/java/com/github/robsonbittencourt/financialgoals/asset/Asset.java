package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
	
	@Getter(AccessLevel.NONE)
	private AssetDepositManager assetDepositManager = new AssetDepositManager();
	
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
	
	public List<AssetDeposit> getAssetDeposits() {
		return assetDepositManager.getDeposits();
	}

	public void deposit(LocalDate date, BigDecimal depositedValue) {
		assetDepositManager.deposit(depositedValue);
		
		BigDecimal newGrossValue = getGrossValue().add(depositedValue);
		
		assetUpdateManager.updateGrossValue(date, newGrossValue);
	}

	public void updateValues(LocalDate date, BigDecimal grossValue, BigDecimal rates, BigDecimal taxes) {
		assetUpdateManager.updateValues(date, grossValue, rates, taxes);
	}
	
	public BigDecimal getGrossValue() {
		if (assetUpdateManager.hasUpdates()) {
			return assetUpdateManager.getLastUpdate().getGrossValue();
		} else {
			return initialValue;
		}
	}
	
	public BigDecimal getGrossProfit() {
		return this.getGrossValue().subtract(this.getInitialValue());
	}
	
	public BigDecimal getGrossProfit(LocalDate initialDate, LocalDate finalDate) {
		Optional<AssetUpdate> firstUpdate = assetUpdateManager.getFirstUpdateByDate(initialDate);
		Optional<AssetUpdate> lastUpdate = assetUpdateManager.getLastUpdateByDate(finalDate);
		
		return lastUpdate.get().getGrossValue().subtract(firstUpdate.get().getGrossValue());
	}
	
	public BigDecimal getGrossProfitPercent() {
		return this.getGrossValue().divide(this.getInitialValue()).subtract(BigDecimal.ONE);
	}

	

}
