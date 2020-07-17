import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.text.ParseException;

class Proj {
  // project name
  String name;
  int id;

  // deadline
  int year;
  int month;
  int day;
  int hour;
  int minute;

  // show color
  color col;
  boolean is_done=false;

  // emergency
  int estimated_time;
  int importance;
  //1~5

  //right button
  Button but;
  Button del_button;
  Button done_button; 
  Button up_button;
  Button down_button;
  Button edit_button;
  int y1 = height;

  //timeline button
  Button tbut;
  Button del_tbutton;
  Button done_tbutton;

  Proj(int idx, String n, int y, int mon, int d, int h, int est, int imp, int tdone) {
    id = idx;
    name = n;
    year = y;
    month = mon;
    day = d;
    hour = h;
    minute = 0;
    estimated_time = est;
    importance = imp;
    col = color(200, 20, 20);
    if (tdone == 1) is_done = true;
  }
  int leftDay() {
    // how much day left
    Date d2 = new Date ();
    Date d1 = new Date ();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    try {
    d2 = sdf.parse(nf(year(),4) + nf(month(),2) + nf(day(), 2));
    d1 = sdf.parse(nf(year,4) + nf(month,2) + nf(day, 2));
    } catch(ParseException ex) {
    }
    
    int diff = int((d1.getTime() - d2.getTime())/3600000/24);
    return diff;
  }
  float leftTime() {
    // how much hour left
    Date d2 = new Date ();
    Date d1 = new Date ();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
    try {
    d2 = sdf.parse(nf(year(),4) + nf(month(),2) + nf(day(), 2) + nf(hour(), 2)+"00");
    d1 = sdf.parse(nf(year,4) + nf(month,2) + nf(day, 2) + nf(hour, 2)+"00");
    } catch(ParseException ex) {
    }
    
    float diff = d1.getTime() - d2.getTime();
    diff /= 3600000;
    return diff;
  }
  color makeColor() {
    float alp = 0;
    alp = 255*sqrt((getUrgen()+0.5)/255.5);
    color re = color(red(col), green(col), blue(col), alp);
    return re;
  }
  float coordinate(float scale) {
    // set position
    float y = 0;
    y = leftTime();
    y = scale * y;
    return y;
  }
  void drawB() {
    but.drawB2();
    del_button.drawB();
    if (!is_done) {
      up_button.drawB();
      down_button.drawB();
    }

    String tname = name;
    int wid = but.getX2() - but.getX1();
    if (textWidth(tname) >= wid-170) {
      while (textWidth(tname) >= wid-185) {
        tname = tname.substring(0, tname.length() -1);
      }
      tname = tname +"...";
    }

    textAlign(LEFT, TOP);
    fill(0);
    textSize(25);
    text(tname, but.getX1()+10, but.getY1()+15);

    textAlign(LEFT, TOP);
    fill(10, 100, 10);
    textSize(25);
    if(is_done) {
      text("estimated time : 0h", but.getX1()+10, but.getY1()+70);
    } else {
      text("estimated time : "+str(estimated_time)+"h", but.getX1()+10, but.getY1()+70);
    }

    textAlign(LEFT, TOP);
    colorMode(HSB);

    fill(27, 212, 106);
    colorMode(RGB);
    textSize(25);
    int left_time = floor(leftTime());
    if (left_time < 24) {
      text("left time          : "+nf(left_time, 2)+"h", but.getX1()+10, but.getY1()+100);
    } else if (left_time % 24 > 0) {
      text("left time          : "+str(left_time/24)+"d " + nf(left_time%24, 2)+"h", but.getX1()+10, but.getY1()+100);
    } else {
      text("left time          : "+str(left_time/24)+"d", but.getX1()+10, but.getY1()+100);
    }

    //bettery
    fill(250, 225, 225);
    stroke(200, 150, 150);
    rectMode(CORNER);
    rect(but.getX2() -70, but.getY1() + 5, 60, 80, 5, 5, 0, 0);

    fill(200, 155, 155);
    stroke(220, 175, 175); 
    line(but.getX2() -68, but.getY1() + 25, but.getX2() -7, but.getY1() + 25);
    line(but.getX2() -68, but.getY1() + 45, but.getX2() -7, but.getY1() + 45);
    line(but.getX2() -68, but.getY1() + 65, but.getX2() -7, but.getY1() + 65);

    fill(180, 100, 100);
    noStroke();
    rectMode(CORNER);
    float rad = min(1, sqrt(getUrgen()/255));
    rect(but.getX2() -69, but.getY1() + 5 + 80 - 80 * rad, 59, 80 * rad, 8*rad, 1+7*rad, 0, 0);

    done_button.drawB();
    edit_button.drawB();
  }
  void drawTB() {
    tbut.drawB();
    //del_tbutton.drawB();
    textAlign(LEFT, TOP);
    fill(0);
    if(is_done) fill(120);
    textSize(22);
    String tname = name;
    int wid = tbut.getX2() - tbut.getX1();
    if (textWidth(tname) >= wid-20) {
      while (textWidth(tname) >= wid-35) {
        tname = tname.substring(0, tname.length() -1);
      }
      tname = tname +"...";
    }
    text(tname, tbut.getX1()+10, tbut.getY1()+10);
  }
  boolean isDelMouseOn() {
    if (del_button.isMouseOn()) {
      return true;
    } else {
      return false;
    }
  }
  int isUpDownMouseOn() {
    if (up_button.isMouseOn()) {
      return 1;
    } else if (down_button.isMouseOn()) {
      return -1;
    } else {
      return 0;
    }
  }
  boolean isDoneMouseOn() {
    if (done_button.isMouseOn()) {
      return true;
    } else {
      return false;
    }
  }
  boolean isEditMouseOn() {
    if (edit_button.isMouseOn()) {
      return true;
    } else {
      return false;
    }
  }
  void addE(int i) {
    estimated_time += i;
    estimated_time = max(0, estimated_time);
  }
  int getEstimatedTime() {
    return estimated_time;
  }
  int getImportance() {
    return importance;
  }
  int getId() {
    return id;
  }
  String getTitle() {
    return name;
  }
  int getYear() {
    return year;
  }
  int getMonth() {
    return month;
  }
  int getDay() {
    return day;
  }
  int getHour() {
    return hour;
  }
  int getY1() {
    return y1;
  }
  float getUrgen() {
    if (is_done) return 0;
    return (250 * estimated_time / leftTime())*(1 + 0.01* importance);
  }
  int getDone() {
    if (is_done) return 1;
    else return 0;
  }
  void setButton(int tx1, int ty1, int tx2, int ty2) {
    if (!is_done) {
      but = new Button(tx1, ty1, tx2, ty2, makeColor(), "");
    } else {
      but = new Button(tx1, ty1, tx2, ty2, color(100, alpha(makeColor())), "");
    }
    del_button = new Button(tx1 + tx2-70, ty1+ty2-55, 60, 50, color(130, 100), "X");
    up_button = new Button(tx1 + 250, ty1 + 70, 50, 30, color(130, 100), "∧");
    down_button = new Button(tx1 + 305, ty1 + 70, 50, 30, color(130, 100), "∨");
    y1 = ty1;
    
    if (is_done) {
      done_button = new Button(tx1 + tx2-165, ty1+ty2-55, 90, 50, color(130, 30), "Done!");
      done_button.setTxtColor(color(140));
    } else { 
      done_button = new Button(tx1 + tx2-165, ty1+ty2-55, 90, 50, color(130, 80), "Done");
    }
    
    edit_button = new Button(tx1 + tx2-165, ty1+ty2-110, 90, 50, color(130, 80), "Edit");
  }
  void setTButton(int tx1, int ty1, int tx2, int ty2) {
    if (!is_done) {
      tbut = new Button(tx1, ty1, tx2, ty2, makeColor(), "");
    } else {
      tbut = new Button(tx1, ty1, tx2, ty2, color(100, alpha(makeColor())), "");
    }
    del_tbutton = new Button(tx1 + tx2-55, ty1+ty2-45, 50, 40, color(130, 100), "Χ");
  }
  void setColor() {
    if (!is_done) {
      but.setColor(makeColor());
    } else {
      but.setColor(color(100, alpha(makeColor())));
    }
  }
  void setTColor() {
    if (!is_done) {
      tbut.setColor(makeColor());
    } else {
      tbut.setColor(color(100, alpha(makeColor())));
    }
  }
  void setColor(color inp) {
    col = inp;
  }
  void done() {
    if (is_done) {
      is_done = false;
    } else {
      is_done = true;
    }
  }
}
