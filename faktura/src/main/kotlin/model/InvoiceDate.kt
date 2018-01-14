package model

enum class InvoiceDate(val description: String) {
  LAST("Ostatni dzien roboczy obecnego miesiaca"),
  FIRST("Pierwszy dzien roboczy następnego miesiaca"),
  TODAY("Dzien generowania faktury")
}