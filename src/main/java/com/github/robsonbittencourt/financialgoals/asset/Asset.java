package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
	
	public Asset(String description, ProfitType profitType, BigDecimal initialValue) {
		this.initialValue = initialValue;
		this.initialDate = LocalDate.now();
		this.description = description;
		this.profitType = profitType;
	}
	
	public BigDecimal getGrossValue() {
		if (assetUpdateManager.hasUpdates()) {
			return assetUpdateManager.getLastUpdate().getGrossValue();
		} else {
			return initialValue;
		}
	}
	
	public AssetUpdate getLastUpdate() {
		return assetUpdateManager.getLastUpdate();
	}
	
	public List<AssetDeposit> getAssetDeposits() {
		return assetDepositManager.getDeposits();
	}

	public void deposit(BigDecimal depositedValue) {
		assetDepositManager.deposit(depositedValue);
		
		BigDecimal newGrossValue = getGrossValue().add(depositedValue);
		
		assetUpdateManager.updateGrossValue(newGrossValue);
	}

	public void updateValues(BigDecimal grossValue, BigDecimal rates, BigDecimal taxes) {
		assetUpdateManager.updateValues(grossValue, rates, taxes);
	}

}
