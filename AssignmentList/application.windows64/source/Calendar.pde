import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CalendarPrint {


  Calendar cal = Calendar.getInstance();


  private String[] calHeader = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
  private String[][] calDate = new String[6][7];

  private int cal_widith=calHeader.length; 
  private int startDay;   
  private int lastDay;    
  private int inputDate;  
  private int calX, calY;

  private int nyear, nmonth, nday;

  private Button[] day_button = new Button[50];


  public CalendarPrint(int year, int month) throws Exception {
    nyear = year;
    nmonth = month;
    if (month<1 || month>12) {
      System.out.println("mon is between 1~12.");
      throw new Exception();
    } else {
      inputDate = 1;
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, month-1);
      cal.set(Calendar.DATE, 1);

      startDay = cal.get(Calendar.DAY_OF_WEEK);
      lastDay = cal.getActualMaximum(Calendar.DATE); 

      int row = 0;
      for (int i=1; inputDate<=lastDay; i++) {

        if (i<startDay) calDate[row][i-1]="  ";
        else {
          calDate[row][(i-1)%cal_widith]=nf(inputDate, 2);
          inputDate++;

          if (i%cal_widith==0) row++;
        }
      }
    }
  }

  public void setButton(int x, int y) {
    calX = x;
    calY = y;
    int row=0;
    for (int j=startDay; j<lastDay+startDay; j++) {
      day_button[j] = new Button(x+((j-1)%cal_widith)*100, 50+y+row*80, 100, 80, 
        color(200), calDate[row][(j-1)%cal_widith]);

      if ((j-1)%cal_widith==cal_widith-1) {
        row++;
      }
    }
  }

  public void drawCal() {
    for (int i=0; i<7; i++) {
      fill(100);
      if (i==0) fill(100, 10, 10);
      if (i==6) fill(10, 10, 100);
      textSize(25);
      textAlign(CENTER, BOTTOM);
      text(calHeader[i], calX+i*100+50, calY+40);
    }
    stroke(100);
    strokeWeight(1);
    line(calX+10, calY+45, calX+700, calY+45);
    for (int j=startDay; j<startDay+lastDay; j++) {
      if (nyear == year() && nmonth == month() && j-startDay+1 == day()) {
        day_button[j].drawClickedB(color(50, 130, 50));
      } else {
        day_button[j].drawClickedB();
      }
    }
  }

  public void clickCal(int day) {
    for (int i=startDay; i<lastDay+startDay; i++) {
      day_button[i].unClick();
    }
    day_button[startDay+day-1].click();
    nday = day;
  }
  public boolean clickCal() {
    boolean re=false;
    for (int i=startDay; i<lastDay+startDay; i++) {
      if (day_button[i].isMouseOn()) {
        re = true;
      }
    }
    if (re) {
      for (int i=startDay; i<lastDay+startDay; i++) {
        if (day_button[i].isMouseOn()) {
          day_button[i].click();
          nday = i+1 - startDay;
        } else {
          day_button[i].unClick();
        }
      }
    }
    return re;
  }

  public void printCal() {
    String cal_str = "";
    for (int i=0; i<cal_widith; i++) {
      cal_str += calHeader[i]+" ";
    }
    cal_str += "\n";

    int row=0;
    for (int j=1; j<lastDay+startDay; j++) {

      cal_str += calDate[row][(j-1)%cal_widith]+" ";

      if ((j-1)%cal_widith==cal_widith-1) {
        cal_str += "\n";
        row++;
      }
    }
    println(cal_str);
  }

  public int getLastDay() {
    return lastDay;
  }
  public int getDay() {
    return nday;
  }
  public int getYear() {
    return nyear;
  }
  public int getMonth() {
    return nmonth;
  }
}
