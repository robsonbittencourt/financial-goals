package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.commons.BigDecimalHelper.setDefaultScale;
import static com.github.robsonbittencourt.financialgoals.commons.MoneyMath.calculateInvestmentReturn;

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

	public Asset(LocalDate initialDate, String description, ProfitType profitType, BigDecimal initialValue) {
		this.initialDate = initialDate;
		this.description = description;
		this.profitType = profitType;
		this.initialValue = setDefaultScale(initialValue);
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
		return assetUpdateManager.getLastUpdate().getGrossValue();
	}

	public InvestmentReturn getGrossProfit() {
		return calculateInvestmentReturn(this.getInitialValue(), this.getGrossValue());
	}

	public InvestmentReturn getGrossProfit(LocalDate initialDate, LocalDate finalDate) {
		AssetUpdate firstUpdate = assetUpdateManager.getFirstUpdateByDate(initialDate);
		AssetUpdate lastUpdate = assetUpdateManager.getLastUpdateByDate(finalDate);
		
		return calculateInvestmentReturn(firstUpdate.getGrossValue(), lastUpdate.getGrossValue());
	}
	
}
