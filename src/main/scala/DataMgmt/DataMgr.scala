package DataMgmt
import java.io._
import java.time.LocalDate

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization

import scala.io.Source
import scala.math.Ordered.orderingToOrdered

object DataMgr {
  implicit private val formats = DefaultFormats

  def loadAnnualStockFinancialData(stockId:String):Map[String,List[String]] = {
    //Check if stock data in files, else download data from API
    if(!new java.io.File("./src/main/resources/"+stockId+"/"+stockId+"-Annual-Financial-Data.txt").exists){
      this.downloadAnnualStockFinancialData(stockId,5)
      Thread.sleep(3000)
    }

    val stockFile = Source.fromFile("./src/main/resources/"+stockId+"/"+stockId+"-Annual-Financial-Data.txt")
    parse(stockFile.getLines().mkString).extract[Map[String,List[String]]]
  }

  def downloadAnnualStockFinancialData(stockId:String, years:Int): Unit = {
    //Create stock folder in resources
    new java.io.File("./src/main/resources/"+stockId).mkdirs

    //TrashKeys to be removed from each map
    val isTrashKeys = List("grossProfitRatio","ebitdaratio","operatingIncomeRatio","incomeBeforeTaxRatio","netIncomeRatio",
      "symbol","fillingDate","acceptedDate","period","costAndExpenses","operatingExpenses","otherExpenses","epsdiluted",
      "weightedAverageShsOutDil","weightedAverageShsOut","link","finalLink")
    val bsTrashKeys = List("date","symbol","fillingDate","acceptedDate","period","cashAndShortTermInvestments","goodwill",
      "intangibleAssets","otherAssets","otherLiabilities","totalLiabilitiesAndStockholdersEquity","link","finalLink")
    val cfTrashKeys = List("freeCashFlow","date","symbol","fillingDate","acceptedDate","period","accountsReceivables",
      "inventory","accountsPayables","effectOfForexChangesOnCash","cashAtEndOfPeriod","cashAtBeginningOfPeriod",
      "operatingCashFlow","capitalExpenditure","link","finalLink")
    val evTrashKeys = List("symbol", "date", "minusCashAndCashEquivalents", "addTotalDebt")
    val rTrashKeys = List("symbol","date","operatingCycle","effectiveTaxRate","pretaxProfitMargin","netIncomePerEBT",
      "ebtPerEbit","ebitPerRevenue","cashFlowToDebtRatio","fixedAssetTurnover","operatingCashFlowPerShare",
      "freeCashFlowPerShare","cashPerShare","payoutRatio","cashFlowCoverageRatios","capitalExpenditureCoverageRatio",
      "dividendPaidAndCapexCoverageRatio","dividendPayoutRatio","priceBookValueRatio","priceToSalesRatio",
      "priceToFreeCashFlowsRatio","priceToOperatingCashFlowsRatio","priceCashFlowRatio","priceEarningsToGrowthRatio",
      "priceSalesRatio","debtRatio","longTermDebtToCapitalization")

    def cleanMap(trashKeys:List[String],mapList:List[Map[String,String]]): List[Map[String,String]] = mapList match {
      case Nil => Nil
      case x::xs => (x -- trashKeys) :: cleanMap(trashKeys, xs)
    }

    //Parse HttpData string into list[maps[str,str]] and delete unnecessary TrashKeys
    val isData = cleanMap(isTrashKeys, parse(HttpRequestManager.getAnnualIncomeStatement(stockId, years)).extract[List[Map[String,String]]])
    val bsData = cleanMap(bsTrashKeys, parse(HttpRequestManager.getAnnualBalanceSheet(stockId, years)).extract[List[Map[String,String]]])
    val cfData = cleanMap(cfTrashKeys, parse(HttpRequestManager.getAnnualCashFlowStatement(stockId, years)).extract[List[Map[String,String]]])
    val evData = cleanMap(evTrashKeys, parse(HttpRequestManager.getAnnualEnterpriseValue(stockId, years)).extract[List[Map[String,String]]])
    val rData = cleanMap(rTrashKeys, parse(HttpRequestManager.getAnnualRatios(stockId, years)).extract[List[Map[String,String]]])

    //Consolidate annual data into a single List[Map]
    def consolidateData(aList:List[Map[String,String]], bList:List[Map[String,String]], cList:List[Map[String,String]],
                        dList:List[Map[String,String]], eList:List[Map[String,String]]): Map[String,List[String]] ={
      aList.flatten.groupMap(_._1)(_._2) ++
        bList.flatten.groupMap(_._1)(_._2) ++
        cList.flatten.groupMap(_._1)(_._2) ++
        dList.flatten.groupMap(_._1)(_._2) ++
        eList.flatten.groupMap(_._1)(_._2)
    }

    //Parse List[Map] to Map[List]
    var consolidatedData =  consolidateData(isData,bsData,cfData,evData,rData)

    //Add missing ratios
    consolidatedData = consolidatedData +
      ("shortToLongTermDebt" -> (consolidatedData("shortTermDebt") zip consolidatedData("longTermDebt")).map(t => if(t._2 != "0.0") (t._1.toDouble / t._2.toDouble).toString else "null")) +
      ("companyMarketCapMultiplier" -> (consolidatedData("enterpriseValue") zip consolidatedData("marketCapitalization")).map(t => if(t._2 != "0.0") (t._1.toDouble / t._2.toDouble).toString else "null")) +
      ("changeInCashRatio" -> (consolidatedData("netChangeInCash") zip consolidatedData("netIncome")).map(t => if(t._2 != "0.0") (t._1.toDouble / t._2.toDouble).toString else "null")) +
      ("retainedEarningsRatio" -> (consolidatedData("retainedEarnings") zip consolidatedData("netIncome")).map(t => if(t._2 != "0.0") (t._1.toDouble / t._2.toDouble).toString else "null")) +
      ("stocksIssuedToRepurchased" -> (consolidatedData("commonStockIssued") zip consolidatedData("commonStockRepurchased")).map(t => if(t._2 != "0.0") (t._1.toDouble / t._2.toDouble).toString else "null"))


    val pwriter = new PrintWriter(new File("./src/main/resources/"+stockId+"/"+stockId+"-Annual-Financial-Data.txt"))
    pwriter.write(Serialization.write(consolidatedData))
    pwriter.close()
  }

  def loadMonthlyStockReturns(stockId:String):List[(String,Double)] = {
    //Check if stock data in files, else download data from API
    if(!new java.io.File("./src/main/resources/"+stockId+"/"+stockId+"-Monthly-Returns-Data.txt").exists){
      this.downloadStockReturnsData(stockId)
      Thread.sleep(3000)
    }

    val stockFile = Source.fromFile("./src/main/resources/"+stockId+"/"+stockId+"-Monthly-Returns-Data.txt")
    parse(stockFile.getLines().mkString).extract[List[(String,Double)]]
  }

  def downloadStockReturnsData(stockId:String) = {
    val fullData = parse(HttpRequestManager.getMonthlyStockReturns(stockId)).extract[Map[String,Any]]
    val historicalData = fullData("Monthly Adjusted Time Series").asInstanceOf[Map[String,Any]]


    def getKeyValues(map:Map[String,Any]): List[(LocalDate,String)] ={
      def listelement(key: String)= {
        val monthlyMap = map(key).asInstanceOf[Map[String,String]]

        (LocalDate.parse(key),monthlyMap("5. adjusted close"))
      }
      (for(key <- map.keys) yield listelement(key)).toList
    }

    val listAdjClose = getKeyValues(historicalData).sortWith(_._1 > _._1)

    def getMonthlyReturns(list:List[(LocalDate,String)]):List[(String,Double)]  = {
      def calculateMonthlyReturns(list:List[(LocalDate,String)]):List[(String,Double)] = list match {
        case List(x) => Nil
        case x::xs => (x._1.toString,(x._2.toDouble/xs.head._2.toDouble)-1):: calculateMonthlyReturns(xs)
      }
      calculateMonthlyReturns(list)
    }

    val monthlyReturns = getMonthlyReturns(listAdjClose)


    val pwriter = new PrintWriter(new File("./src/main/resources/"+stockId+"/"+stockId+"-Monthly-Returns-Data.txt"))
    pwriter.write(Serialization.write(monthlyReturns))
    pwriter.close()
  }

  def updateData(): Unit ={

  }
}
