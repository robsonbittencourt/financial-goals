package com.github.robsonbittencourt.financialgoals.asset;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Collections.reverseOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class AssetUpdateManager {
	
	private List<AssetUpdate> updates = new ArrayList<>();
	
	public void updateGrossValue(LocalDate date, BigDecimal grossValue) {
		updateValues(date, grossValue, null, null);
	}

	public void updateValues(LocalDate date, BigDecimal grossValue, BigDecimal rates, BigDecimal taxes) {
		if (updates.isEmpty()) {
			updates.add(firstUpdate(date, grossValue));
		} else {
			AssetUpdate lastUpdate = getLastUpdate();
			
			BigDecimal newGrossValue = grossValue != null ? grossValue : lastUpdate.getGrossValue();
			BigDecimal newRates = rates != null ? rates : lastUpdate.getRates();
			BigDecimal newTaxes = taxes != null ? taxes : lastUpdate.getTaxes();
			
			BigDecimal shares = lastUpdate.getShares();
			BigDecimal shareValue = newGrossValue.divide(shares, 4, HALF_UP);
			
			updates.add(new AssetUpdate(date, valueOf(0), newGrossValue, newRates, newTaxes, shareValue, shares));
		}
	}
	
	private AssetUpdate firstUpdate(LocalDate date, BigDecimal grossValue) {
		BigDecimal shareValue = valueOf(100);
		BigDecimal shares = grossValue.divide(shareValue, 4, HALF_UP);
		
		return new AssetUpdate(date, valueOf(0), grossValue, valueOf(0), valueOf(0), shareValue, shares);
	}
	
	public void makeTransaction(LocalDate date, BigDecimal transactionValue) {
		AssetUpdate lastUpdate = getLastUpdate();
		
		BigDecimal grossValue = lastUpdate.getGrossValue().add(transactionValue);
		BigDecimal rates = lastUpdate.getRates();
		BigDecimal taxes = lastUpdate.getTaxes();
		
		BigDecimal shareValue = lastUpdate.getShareValue();
		BigDecimal newShares = transactionValue.divide(shareValue, 4, HALF_UP);
		BigDecimal shares = lastUpdate.getShares().add(newShares);
		
		updates.add(new AssetUpdate(date, transactionValue, grossValue, rates, taxes, shareValue, shares));
	}

	public List<AssetUpdate> getUpdates() {
		return updates;
	}
	
	public AssetUpdate getFirstUpdate() {
		return updates.get(0);
	}
	
	public AssetUpdate getLastUpdate() {
		return updates.get(updates.size() - 1);
	}

	public AssetUpdate getFirstUpdateByDate(LocalDate date) {
		return updates.stream()
					  .sorted()
			          .filter(u -> u.getDate().equals(date) || u.getDate().isAfter(date))
			          .findFirst()
			          .orElseThrow(UpdateNotFoundException::new);
	}
	
	public AssetUpdate getLastUpdateByDate(LocalDate date) {
		return updates.stream()
				      .sorted(reverseOrder())
				      .filter(u -> u.getDate().equals(date) || u.getDate().isBefore(date))
				      .findFirst()
				      .orElseThrow(UpdateNotFoundException::new);
	}
	
}
