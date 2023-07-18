package model;

public class ReportByMonth {
    public String appointByMonth;
    public int appointByMonthTotal;

    public ReportByMonth(String appointByMonth, int appointByMonthTotal) {
        this.appointByMonth = appointByMonth;
        this.appointByMonthTotal = appointByMonthTotal;
    }

    public String getAppointByMonth() {
        return appointByMonth;
    }

    public void setAppointByMonth(String appointByMonth) {
        this.appointByMonth = appointByMonth;
    }

    public int getAppointByMonthTotal() {
        return appointByMonthTotal;
    }

    public void setAppointByMonthTotal(int appointByMonthTotal) {
        this.appointByMonthTotal = appointByMonthTotal;
    }
}
