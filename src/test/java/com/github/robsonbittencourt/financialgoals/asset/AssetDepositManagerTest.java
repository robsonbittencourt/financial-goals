package com.github.robsonbittencourt.financialgoals.asset;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;

public class AssetDepositManagerTest {
	
	@Test
	public void shouldAddDepositsOnAsset() {
		BigDecimal depositedValue = BigDecimal.valueOf(100);
		
		AssetDepositManager manager = new AssetDepositManager();
		
		manager.deposit(depositedValue);
		
		assertEquals(LocalDate.now(), manager.getDeposits().get(0).getDate());
		assertEquals(BigDecimal.valueOf(100), manager.getDeposits().get(0).getValue());
	}
	
}
