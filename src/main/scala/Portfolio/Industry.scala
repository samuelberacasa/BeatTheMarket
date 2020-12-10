package Portfolio

import Output.Tabulator
import Portfolio.Industry._
import Portfolio.Stock.{bigCashFormatter, bigNumFormatter, cashFormatter, numFormatter, percentFormatter}

object Industry {
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

  def getStockKeyList(stock:Stock,key:String):List[String] = stock.annualFinancials.get(key) match{
    case Some(x) => x
    case None => Nil
  }
}

class Industry(stockIds:List[String]) {
  val stockList:List[Stock] = for(stockId <- stockIds) yield new Stock(stockId, 10)

  def printStockHistoricalReport(keys:List[(String,String)],year:Int, div:Double):Unit = {
    def formatRow(key:String, valueType:String):List[String] = valueType match {
      case "$/" => key::formatBigCashRow(getKeyList(key))
      case "$" => key::formatCashRow(getKeyList(key))
      case "#/" => key::formatBigNumRow(getKeyList(key))
      case "#" => key::formatNumRow(getKeyList(key))
      case "x" => key::formatxNumRow(getKeyList(key))
      case "%" => key::formatPercentRow(getKeyList(key))
      case "g" => (key+" (growth)")::formatPercentRow(getKeyGrowthList(key))
      case "g_" => (key+" (cagr)")::formatPercentRow(getKeyCAGRList(key))
      case "-" => Nil
    }

    def getKeyList(key:String):List[String] = {
      def getKeyValue(stock:Stock,key: String):String = {
        val datesStr = getStockKeyList(stock,"date")
        val datesYear = datesStr.map(x => x.take(4))
        val index = datesYear.indexOf(year.toString)
        if(index < 0){
          null
        }else{
          val keyList = getStockKeyList(stock,key)
          keyList(index)
        }
      }
      for(stock <- stockList) yield getKeyValue(stock,key)
    }

    def getKeyGrowthList(key:String):List[String] = {
      def getKeyValue(stock:Stock,key: String):String = {
        val datesStr = getStockKeyList(stock,"date")
        val datesYear = datesStr.map(x => x.take(4))
        val index = datesYear.indexOf(year.toString)
        if(index < 0 || index > datesStr.size - 2){
          null
        }else{
          val keyList = getStockKeyList(stock,key)
          (keyList(index).toDouble/keyList(index+1).toDouble-1).toString
        }
      }
      for(stock <- stockList) yield getKeyValue(stock,key)
    }

    def getKeyCAGRList(key:String):List[String] = {
      def getKeyValue(stock:Stock,key: String):String = {
        val datesStr = getStockKeyList(stock,"date")
        val datesYear = datesStr.map(x => x.take(4))
        val index = datesYear.indexOf(year.toString)
        if(index < 0 || index > datesStr.size - 2){
          null
        }else{
          val keyList = getStockKeyList(stock,key)
          (math.pow(keyList(index).toDouble/keyList(keyList.size-1).toDouble,1/(keyList.size-1-index).toDouble)-1).toString
        }
      }
      for(stock <- stockList) yield getKeyValue(stock,key)
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



    def getHeaderRow():List[String] = {
      "Stock"::stockIds
    }

    println(Tabulator.format(getHeaderRow()::(for(key <- keys) yield formatRow(key._1,key._2))))
  }

  def printStocksReturns(): Unit ={
    val header = ("Stock":: stockIds)
    val returns = "Avg Returns"::(for(stock <- stockList) yield percentFormatter.format(stock.sharpieRatio._1))
    val risk = "Avg Risk"::(for(stock <- stockList) yield percentFormatter.format(stock.sharpieRatio._2))
    val sharpie = "Sharpie Ratio"::(for(stock <- stockList) yield numFormatter.format(stock.sharpieRatio._3))

    println(Tabulator.format(header::returns::risk::sharpie::Nil))
  }
}
