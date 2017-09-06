import calendar
import unittest

import datetime

from models import Invoice, Client


class TestInvoiceDateCalculations(unittest.TestCase):
    def test_endDayOfPreviousMonthDateCalculation(self):
        current_date = datetime.datetime.now()
        invoice = Invoice(None, Client(None, None, None, 0, None, 0, date_day_type=0), 0, None)
        previous_month = current_date.month - 1
        latest_day_of_previous_month = calendar.monthrange(current_date.year, previous_month)[1]
        self.assertTrue(invoice.date.month == previous_month and invoice.date.day == latest_day_of_previous_month)

    def test_endDayOfPreviousMonthDateCalculationForFirstMonthInTheYear(self):
        invoice = Invoice(None, Client(None, None, None, 0, None, 0, date_day_type=0), 0, None,
                          date=datetime.datetime(year=2018, month=1, day=15))
        self.assertTrue(invoice.date.month == 12 and invoice.date.day == 31
                        and invoice.date.year == 2017)

    def test_firstDayOfCurrentMonthCalculation(self):
        current_date = datetime.datetime.now()
        invoice = Invoice(None, Client(None, None, None, 0, None, 0, date_day_type=1), 0, None)
        self.assertTrue(invoice.date.month == current_date.month and invoice.date.day == 1)

    def test_currentDayIsCurrent(self):
        current_date = datetime.datetime.now()
        invoice = Invoice(None, Client(None, None, None, 0, None, 0, date_day_type=2), 0, None)
        self.assertTrue(current_date.month == invoice.date.month and current_date.day == invoice.date.day)


if __name__ == '__main__':
    unittest.main()
