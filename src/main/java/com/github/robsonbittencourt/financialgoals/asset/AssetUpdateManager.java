package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class AssetUpdateManager {
	
	private List<AssetUpdate> updates = new ArrayList<>();
	
	public void updateGrossValue(BigDecimal grossValue) {
		updateValues(grossValue, null, null);
	}

	public void updateValues(BigDecimal grossValue, BigDecimal rates, BigDecimal taxes) {
		if (updates.isEmpty()) {
			updates.add(new AssetUpdate(grossValue, rates, taxes));
		} else {
			AssetUpdate lastUpdate = getLastUpdate();
			
			BigDecimal newGrossValue = grossValue != null ? grossValue : lastUpdate.getGrossValue();
			BigDecimal newRates = rates != null ? rates : lastUpdate.getRates();
			BigDecimal newTaxes = taxes != null ? taxes : lastUpdate.getTaxes();
			
			updates.add(new AssetUpdate(newGrossValue, newRates, newTaxes));
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
	
}
