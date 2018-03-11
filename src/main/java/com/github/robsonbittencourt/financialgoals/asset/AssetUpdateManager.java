package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
		if (updates.isEmpty()) {
			throw new IndexOutOfBoundsException("Don't exist updates");
		}
		
		return updates.get(updates.size() - 1);
	}

	public boolean hasUpdates() {
		return !updates.isEmpty();
	}

	public Optional<AssetUpdate> getFirstUpdateByDate(LocalDate date) {
		return updates.stream()
				.sorted()
				.filter(u -> u.getDate().isEqual(date) || date.isAfter(u.getDate()))
				.findFirst();
	}	
	
	public Optional<AssetUpdate> getLastUpdateByDate(LocalDate date) {
		return updates.stream()
				.sorted(Comparator.reverseOrder())
				.filter(u -> u.getDate().isEqual(date) || date.isAfter(u.getDate()))
				.findFirst();
	}	
	
}
