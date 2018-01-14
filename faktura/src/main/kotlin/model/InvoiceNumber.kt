package model

enum class InvoiceNumber(val description: String) {
  YEARLY("Rocznie (n/yyyy)"),
  MONTHLY("Miesięcznie (n/mm/yyyy)")
}