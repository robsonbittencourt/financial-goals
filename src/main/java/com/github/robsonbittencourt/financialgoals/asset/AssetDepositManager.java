package com.github.robsonbittencourt.financialgoals.asset;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class AssetDepositManager {
	
	private List<AssetDeposit> deposits = new ArrayList<>();

	public void deposit(BigDecimal depositedValue) {
		deposits.add(new AssetDeposit(LocalDate.now(), depositedValue));
	}

}
