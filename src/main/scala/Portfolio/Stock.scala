package Portfolio
import java.time.LocalDate

import language.postfixOps
import DataMgmt.DataMgr
import Portfolio.Stock._
import Output.Tabulator

object Stock{
  val numFormatter = java.text.NumberFormat.getInstance
  numFormatter.setMaximumFractionDigits(2)
  val bigNumFormatter = java.text.NumberFormat.getInstance
  bigNumFormatter.setMaximumFractionDigits(0)
  val cashFormatter = java.text.NumberFormat.getCurrencyInstance
  cashFormatter.setMaximumFractionDigits(2)
  val bigCashFormatter = java.text.NumberFormat.getCurrencyInstance
  bigCashFormatter.setMaximumFractionDigits(0)
  val percentFormatter = java.text.NumberFormat.getPercentInstance
  percentFormatter.setMaximumFractionDigits(2)
}

class Stock (stockId:String, years:Int){
  val annualFinancials:Map[String,List[String]] = DataMgr.loadAnnualStockFinancialData(stockId)
  val annualreturns:List[(String,Double)] = DataMgr.loadMonthlyStockReturns(stockId).take(years*12)
  val sharpieRatio = calculateSharpieRatio()

  def printStockHistoricalReport(keys:List[(String,String)],div:Double):Unit = {
    def formatRow(key:String, valueType:String):List[String] = valueType match {
      case "$/" => key::formatBigCashRow(getKeyList(key))
      case "$" => key::formatCashRow(getKeyList(key))
      case "#/" => key::formatBigNumRow(getKeyList(key))
      case "#" => key::formatNumRow(getKeyList(key))
      case "x" => key::formatxNumRow(getKeyList(key))
      case "%" => key::formatPercentRow(getKeyList(key))
      case "g" => key::formatGrowthRow(getKeyList(key))
      case "g_" => key::formatCAGRRow(getKeyList(key))
      case "-" => Nil
    }

    def getKeyList(key:String):List[String] = annualFinancials.get(key) match{
      case Some(x) => x
      case None => Nil
    }

    def formatCashRow(list:List[String]):List[String] = list match {
      case Nil => Nil
      case x::xs => (if(x==null) "null" else cashFormatter.format(x.toDouble))::formatCashRow(xs)
    }

    def formatBigCashRow(list:List[String]):List[String] = list match {
      case Nil => Nil
      case x::xs => (if(x==null) "null" else bigCashFormatter.format(x.toDouble/div))::formatBigCashRow(xs)
    }

    def formatNumRow(list:List[String]):List[String] = list match {
      case Nil => Nil
      case x::xs => (if(x==null) "null" else numFormatter.format(x.toDouble))::formatNumRow(xs)
    }

    def formatBigNumRow(list:List[String]):List[String] = list match {
      case Nil => Nil
      case x::xs => (if(x==null) "null" else bigNumFormatter.format(x.toDouble/div))::formatBigNumRow(xs)
    }

    def formatxNumRow(list:List[String]):List[String] = list match {
      case Nil => Nil
      case x::xs => (if(x==null) "null" else (numFormatter.format(x.toDouble)+"x"))::formatxNumRow(xs)
    }

    def formatPercentRow(list:List[String]):List[String] = list match {
      case Nil => Nil
      case x::xs => (if(x==null) "null" else percentFormatter.format(x.toDouble))::formatPercentRow(xs)
    }

    def formatGrowthRow(list:List[String]):List[String] = list match {
      case List(x) => "null"::Nil
      case x::xs => (if(x==null) "null" else percentFormatter.format(x.toDouble/xs.head.toDouble-1)) :: formatGrowthRow(xs)
    }

    def formatCAGRRow(list:List[String]):List[String] = {
      val first = list(0)
      val last = list(list.size - 1)
      val jumps:Double = list.size - 2

      percentFormatter.format(math.pow(first.toDouble/last.toDouble,1/jumps)-1)::(for(i <- 1 until list.size) yield "null").toList
    }

    def getHeaderRow():List[String] = {
      ("date ("+stockId+")")::getKeyList("date")
    }

    println(Tabulator.format(getHeaderRow()::(for(key <- keys) yield formatRow(key._1,key._2))))
  }

  def calculateSharpieRatio():(Double,Double,Double) = {
    val avgReturns = annualreturns.foldLeft(0.0)((r,c)=>r+c._2)/annualreturns.size*12
    val risk = math.sqrt(annualreturns.map(a => math.pow(a._2 - avgReturns/12,2)).sum/annualreturns.size)*math.sqrt(12)
    val sharpie = (avgReturns - 0.0183)/risk
    (avgReturns,risk,sharpie)
  }

  def printStockReturns(): Unit = {
    println("Avg Returns: " + percentFormatter.format(sharpieRatio._1))
    println("Avg Risk: " + percentFormatter.format(sharpieRatio._2))
    println("Sharpie Ratio: " + numFormatter.format(sharpieRatio._3))
  }
}