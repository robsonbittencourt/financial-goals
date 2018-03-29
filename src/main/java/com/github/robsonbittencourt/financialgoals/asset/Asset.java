package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.indicators.IndicatorType.INFLATION;
import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.github.robsonbittencourt.financialgoals.indicators.Indicator;

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

	public BigDecimal getGrossValue() {
		return assetUpdateManager.getLastUpdate().getGrossValue();
	}

	public InvestmentReturn getGrossProfit() {
		LocalDate finalDate = assetUpdateManager.getLastUpdate().getDate();
		
		return getGrossProfit(initialDate, finalDate);
	}

	public InvestmentReturn getGrossProfit(LocalDate initialDate, LocalDate finalDate) {
		AssetUpdate firstUpdate = assetUpdateManager.getFirstUpdateByDate(initialDate);
		AssetUpdate lastUpdate = assetUpdateManager.getLastUpdateByDate(finalDate);
		
		BigDecimal transactionsValue = assetUpdateManager.getUpdates()
														 .stream()
														 .filter(u -> inRange(u.getDate(), initialDate, finalDate))
														 .map(u -> u.getTransactionValue())
														 .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal value = lastUpdate.getGrossValue().subtract(transactionsValue).subtract(firstUpdate.getGrossValue());
		BigDecimal percent = lastUpdate.getShareValue().divide(firstUpdate.getShareValue(), 4, HALF_UP).subtract(BigDecimal.ONE);
		
		return new InvestmentReturn(value, percent);
	}
	
	private boolean inRange(LocalDate date, LocalDate initialDate, LocalDate finalDate) {
		if (date.equals(initialDate) || date.equals(finalDate)) {
			return true;
		}
		
		return date.isAfter(initialDate) && date.isBefore(finalDate);
	}

	public BigDecimal getNetValue() {
		return assetUpdateManager.getLastUpdate().getNetValue();
	}

	public InvestmentReturn getNetProfit() {
		LocalDate finalDate = assetUpdateManager.getLastUpdate().getDate();
		
		return getNetProfit(initialDate, finalDate);
	}
	
	public InvestmentReturn getNetProfit(LocalDate initialDate, LocalDate finalDate) {
		AssetUpdate lastUpdate = assetUpdateManager.getLastUpdateByDate(finalDate);
		InvestmentReturn grossProfit = getGrossProfit(initialDate, finalDate);
		
		BigDecimal grossProfitValue = grossProfit.getValue();
		BigDecimal grossProfitPercent = grossProfit.getPercent();
		BigDecimal rates = lastUpdate.getRates();
		BigDecimal taxes = lastUpdate.getTaxes();
		
		// TODO as taxas e impostos devem ser buscados no período também
		BigDecimal netProfitValue = grossProfitValue.subtract(rates).subtract(taxes);
		BigDecimal netProfitPercent = netProfitValue.multiply(grossProfitPercent).divide(grossProfitValue, 4, RoundingMode.HALF_UP);
		
		return new InvestmentReturn(netProfitValue, netProfitPercent);
	}
	
	public InvestmentReturn getRealProfit() {
		LocalDate finalDate = assetUpdateManager.getLastUpdate().getDate();
		
		return getRealProfit(initialDate, finalDate);
	}

	public InvestmentReturn getRealProfit(LocalDate initialDate, LocalDate finalDate) {
		InvestmentReturn grossProfit = getGrossProfit(initialDate, finalDate);
		BigDecimal grossProfitValue = grossProfit.getValue();
		BigDecimal grossProfitPercent = grossProfit.getPercent();

		BigDecimal netProfitValue = getNetProfit(initialDate, finalDate).getValue();

		BigDecimal inflation = new Indicator().getIndicator(INFLATION, initialDate, finalDate);
		BigDecimal inflationDiscount = grossProfitValue.multiply(inflation);
		
		BigDecimal realProfitValue = netProfitValue.subtract(inflationDiscount);
		BigDecimal realProfitPercent = realProfitValue.multiply(grossProfitPercent).divide(grossProfitValue, 4, RoundingMode.HALF_UP);

		return new InvestmentReturn(realProfitValue, realProfitPercent);
	}
	
}
