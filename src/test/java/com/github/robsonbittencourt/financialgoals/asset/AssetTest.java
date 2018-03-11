package com.github.robsonbittencourt.financialgoals.asset;

import static com.github.robsonbittencourt.financialgoals.asset.ProfitType.FIX;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;

public class AssetTest {
	
	@Test
	public void shouldCreateAnAsset() {
		String description = "CDB Itaú Bank";
		ProfitType profitType = FIX;
		BigDecimal initialValue = BigDecimal.valueOf(100);
		
		Asset asset = new Asset(description, profitType, initialValue);
		
		assertEquals("CDB Itaú Bank", asset.getDescription());
		assertEquals(FIX, asset.getProfitType());
		assertEquals(BigDecimal.valueOf(100), asset.getInitialValue());
	}
	
	@Test
	public void shouldUseActualDateAsInitialDateWhenCreateAnAsset() {
		Asset asset = new Asset("CDB Itaú Bank", FIX, BigDecimal.valueOf(100));
		
		assertEquals(LocalDate.now(), asset.getInitialDate());
	}
	
	@Test
	public void shouldAddMoreMoneyOnAsset() {
		Asset asset = new Asset("CDB Itaú Bank", FIX, BigDecimal.valueOf(100));
		
		asset.deposit(BigDecimal.valueOf(100));
		
		assertEquals(asset.getAssetDeposits().get(0).getValue(), BigDecimal.valueOf(100));
	}
	
	@Test
	public void shouldUpdateGrossValueWhenAddMoreMoneyOnAsset() {
		Asset asset = new Asset("CDB Itaú Bank", FIX, BigDecimal.valueOf(100));
		
		asset.deposit(BigDecimal.valueOf(100));
		
		assertEquals(asset.getGrossValue(), BigDecimal.valueOf(200));
	}
	
	@Test
	public void shouldUpdateValuesOfAsset() {
		Asset asset = new Asset("CDB Itaú Bank", FIX, BigDecimal.valueOf(100));
		
		asset.updateValues(BigDecimal.valueOf(120),BigDecimal.valueOf(10), BigDecimal.valueOf(15));
		
		assertEquals(BigDecimal.valueOf(120), asset.getLastUpdate().getGrossValue());
		assertEquals(BigDecimal.valueOf(10), asset.getLastUpdate().getRates());
		assertEquals(BigDecimal.valueOf(15), asset.getLastUpdate().getTaxes());
	}
	
}
