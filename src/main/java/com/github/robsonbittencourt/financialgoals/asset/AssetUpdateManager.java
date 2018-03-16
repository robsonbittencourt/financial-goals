package com.github.robsonbittencourt.financialgoals.asset;

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
			updates.add(new AssetUpdate(date, grossValue, rates, taxes));
		} else {
			AssetUpdate lastUpdate = getLastUpdate();
			
			BigDecimal newGrossValue = grossValue != null ? grossValue : lastUpdate.getGrossValue();
			BigDecimal newRates = rates != null ? rates : lastUpdate.getRates();
			BigDecimal newTaxes = taxes != null ? taxes : lastUpdate.getTaxes();
			
			updates.add(new AssetUpdate(date, newGrossValue, newRates, newTaxes));
		}
	}

	public List<AssetUpdate> getUpdates() {
		return updates;
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
