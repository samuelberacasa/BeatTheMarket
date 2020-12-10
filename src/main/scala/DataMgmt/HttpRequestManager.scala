package DataMgmt

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import scala.io.Source.fromInputStream

object HttpRequestManager{
  val client = HttpClientBuilder.create().build()
  val apiKeyFinMod = "23a16f4f83d3f48ddad551eb64f50dad"
  val apiKeyVantage = "RHEWDBLSIOD6V8FK"

  def getAnnualIncomeStatement(stockId:String, years:Int):String = {
    val request = new HttpGet("https://financialmodelingprep.com/api/v3/income-statement/"+stockId+"?apikey="+apiKeyFinMod+"&limit="+years)
    val response = client execute request
    request.releaseConnection()
    fromInputStream(response.getEntity.getContent()).getLines().mkString
  }

  def getAnnualBalanceSheet(stockId:String, years:Int):String = {
    val request = new HttpGet("https://financialmodelingprep.com/api/v3/balance-sheet-statement/"+stockId+"?apikey="+apiKeyFinMod+"&limit="+years)
    val response = client execute request
    request.releaseConnection()
    fromInputStream(response.getEntity.getContent()).getLines().mkString
  }

  def getAnnualCashFlowStatement(stockId:String, years:Int):String = {
    val request = new HttpGet("https://financialmodelingprep.com/api/v3/cash-flow-statement/"+stockId+"?apikey="+apiKeyFinMod+"&limit="+years)
    val response = client execute request
    request.releaseConnection()
    fromInputStream(response.getEntity.getContent()).getLines().mkString
  }

  def getAnnualEnterpriseValue(stockId:String, years:Int):String = {
    val request = new HttpGet("https://financialmodelingprep.com/api/v3/enterprise-values/"+stockId+"?apikey="+apiKeyFinMod+"&limit="+years)
    val response = client execute request
    request.releaseConnection()
    fromInputStream(response.getEntity.getContent()).getLines().mkString
  }

  def getAnnualRatios(stockId:String, years:Int):String = {
    val request = new HttpGet("https://financialmodelingprep.com/api/v3/ratios/"+stockId+"?apikey="+apiKeyFinMod+"&limit="+years)
    val response = client execute request
    request.releaseConnection()
    fromInputStream(response.getEntity.getContent()).getLines().mkString
  }

  def getMonthlyStockReturns(stockId:String):String = {
    val request = new HttpGet("https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol="+stockId+"&apikey="+apiKeyVantage)
    try{
      val response = client execute request
      fromInputStream(response.getEntity.getContent()).getLines().mkString
    }finally {
      request.releaseConnection()
    }
  }
}
