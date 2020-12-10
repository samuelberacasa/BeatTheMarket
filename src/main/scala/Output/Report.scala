package Output

object Report {
  val dashboard = List(("revenue","$/"),("revenue","g_"), ("grossProfit","$/"),("grossProfitMargin","%"),("ebitda","$/"),
    ("operatingIncome","$/"), ("operatingProfitMargin","%"),("netIncome","$/"),("netProfitMargin","%"), ("line","-"),
    ("totalDebt","$/"), ("debtEquityRatio","#"), ("interestCoverage","#"), ("line","-"),("stockPrice","$"),("numberOfShares","#/"),
    ("marketCapitalization","$/"))

  val incomeStatement = List(("revenue","$/"), ("costOfRevenue","$/"), ("grossProfit","$/"),
    ("researchAndDevelopmentExpenses","$/"), ("generalAndAdministrativeExpenses","$/"), ("sellingAndMarketingExpenses","$/"),
    ("ebitda","$/"), ("depreciationAndAmortization","$/"), ("operatingIncome","$/"), ("interestExpense","$/"),
    ("totalOtherIncomeExpensesNet","$/"), ("incomeBeforeTax","$/"), ("incomeTaxExpense","$/"), ("netIncome","$/"))

  val balanceSheet = List(("cashAndCashEquivalents","$/"),("shortTermInvestments","$/"),("netReceivables","$/"),
    ("inventory","$/"),("otherCurrentAssets","$/"),("totalCurrentAssets","$/"),("line","-"),("propertyPlantEquipmentNet","$/"),
    ("goodwillAndIntangibleAssets","$/"),("longTermInvestments","$/"),("taxAssets","$/"),("otherNonCurrentAssets","$/"),
    ("totalNonCurrentAssets","$/"),("line","-"),("totalAssets","$/"),("line","-"),("accountPayables","$/"),("shortTermDebt","$/"),("taxPayables","$/"),
    ("deferredRevenue","$/"),("otherCurrentLiabilities","$/"),("totalCurrentLiabilities","$/"),("line","-"),("longTermDebt","$/"),
    ("deferredRevenueNonCurrent","$/"),("deferredTaxLiabilitiesNonCurrent","$/"),("otherNonCurrentLiabilities","$/"),
    ("totalNonCurrentLiabilities","$/"),("line","-"),("totalLiabilities","$/"),("line","-"),("commonStock","$/"),("retainedEarnings","$/"),
    ("accumulatedOtherComprehensiveIncomeLoss","$/"),("othertotalStockholdersEquity","$/"),("line","-"),("totalStockholdersEquity","$/"),("line","-"),
    ("totalInvestments","$/"),("totalDebt","$/"),("netDebt","$/"))

  val cashFlowStatement = List(("netIncome","$/"),("depreciationAndAmortization","$/"),("deferredIncomeTax","$/"),
    ("stockBasedCompensation","$/"),("changeInWorkingCapital","$/"),("otherWorkingCapital","$/"),("otherNonCashItems","$/"),
    ("netCashProvidedByOperatingActivities","$/"),("line","-"),("investmentsInPropertyPlantAndEquipment","$/"),("acquisitionsNet","$/"),
    ("purchasesOfInvestments","$/"),("salesMaturitiesOfInvestments","$/"),("otherInvestingActivites","$/"),
    ("netCashUsedForInvestingActivites","$/"),("line","-"),("debtRepayment","$/"),("commonStockIssued","$/"),("commonStockRepurchased","$/"),
    ("dividendsPaid","$/"),("otherFinancingActivites","$/"),("netCashUsedProvidedByFinancingActivities","$/"),("line","-"),
    ("netChangeInCash","$/"))

  val enterpriseValue = List(("stockPrice","$"),("numberOfShares","#/"),("marketCapitalization","$/"),
    ("enterpriseValue","$/"))

  val profitabilityRatios = List(("grossProfitMargin","%"),("operatingProfitMargin","%"),("netProfitMargin","%"),
    ("operatingCashFlowSalesRatio","%"),("changeInCashRatio","%"),("retainedEarningsRatio","%"))

  val workingCapitalRatios = List(("currentRatio","#"),("quickRatio","#"),("cashRatio","#"),("receivablesTurnover","x"),
    ("payablesTurnover","x"),("inventoryTurnover","x"),("daysOfSalesOutstanding","#"),("daysOfInventoryOutstanding","#"),
    ("daysOfPayablesOutstanding","#"),("cashConversionCycle","#"))

  val leverageRatios = List(("interestCoverage","#"),("shortTermCoverageRatios","#"),("shortToLongTermDebt","#"),
    ("debtEquityRatio","#"),("totalDebtToCapitalization","#"))

  val valuationRatios = List(("companyEquityMultiplier","x"),("companyMarketCapMultiplier","x"),
    ("enterpriseValueMultiple","x"),("priceEarningsRatio","#"),("priceToBookRatio","#"),
    ("freeCashFlowOperatingCashFlowRatio","#"))

  val OperatingRatios = List(("returnOnAssets","%"),("returnOnEquity","%"),("returnOnCapitalEmployed","%"),("assetTurnover","x"))

  val equityRatios = List(("eps","$"),("stocksIssuedToRepurchased","%"),("dividendYield","%"))

  val growthReport = List(("revenue","$/"), ("revenue","g"), ("grossProfit","$/"), ("grossProfit","g"),
    ("operatingIncome","$/"), ("operatingIncome","g"),("netIncome","$/"), ("netIncome","g"))

  val cagrReport = List(("revenue","$/"), ("revenue","g_"), ("grossProfit","$/"), ("grossProfit","g_"),
    ("operatingIncome","$/"), ("operatingIncome","g_"),("netIncome","$/"), ("netIncome","g_"))
}
