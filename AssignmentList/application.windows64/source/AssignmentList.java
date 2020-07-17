import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 
import java.util.ArrayList; 
import java.util.Iterator; 
import java.util.Collections; 
import java.util.Calendar; 
import java.text.DateFormat; 
import java.text.SimpleDateFormat; 
import java.text.SimpleDateFormat; 
import java.util.Locale; 
import java.util.Date; 
import java.text.ParseException; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AssignmentList extends PApplet {







PFont myFont;
PFont codeFont;

float scale = 1;

int window_num = 0;
// 0 : show
// 1 : graph
// 10 : write
// 11 : edit

int edit_row_id = 0;

int left_window_y = 0;
int right_window_y = 0;
int right_window_y_min = -1000;
int graph_x_size = 14;

int[] leap_year = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
float[] graph_height = new float[70];

String title_input=""; 

int[] date_input={year(), month(), day(), hour(), 0};
int[] esti_input={1, 0};

Table assig_table;
ArrayList<Proj> assig_list = new ArrayList<Proj>();

Button make_work = new Button(1200, 10, 250, 60, 
  color(250, 150, 100), "New Work");
Button graph_button = new Button(1370, 90, 80, 55, 
  color(130, 170, 50, 180), "graph");

int timeline_mode = 1;
Button timeline_button = new Button(630, 10, 90, 60, 
  color(180), "Hour");
//Button refresh_button = new

int sortX = 760;
int sort_mode = 0;
Button[] sort_button = {new Button(sortX, 90, 80, 55, 
  color(250, 200, 150, 180), "urgen"), 
  new Button(sortX + 85, 90, 90, 55, 
  color(250, 200, 150, 180), "dday↑"), 
  new Button(sortX + 180, 90, 90, 55, 
  color(250, 200, 150, 180), "dday↓"), 
  new Button(sortX + 275, 90, 80, 55, 
  color(250, 200, 150, 180), "esti↑"), 
  new Button(sortX + 360, 90, 80, 55, 
  color(250, 200, 150, 180), "esti↓")};

int saveX = 50;
int saveY = 10;

Button[] graph_range_button = {
  new Button(1000, saveY, 100, 60, 
  color(100, 10, 160), "4day"), 
  new Button(1110, saveY, 100, 60, 
  color(100, 10, 160), "1week"), 
  new Button(1220, saveY, 100, 60, 
  color(100, 10, 160), "2week"), 
  new Button(1330, saveY, 100, 60, 
  color(100, 10, 160), "4week"), 
};

Button push_work = new Button(1200, saveY, 250, 60, 
  color(100, 100, 255), "Save");
Button go_back = new Button(saveX, 10, 250, 60, 
  color(210, 210, 230), "Back");
Button title = new Button(saveX, 120, 840, 50, 
  color(180, 180, 230), "");

boolean day_show = true;
Button dow_button = new Button(10, 10, 100, 60, 
  color(180), "day off");

Button[] date_button = 
  {new Button(saveX+15, 205, 85, 45, 
  color(230), nf(date_input[0], 4)), 
  new Button(saveX+115, 205, 45, 45, 
  color(230), nf(date_input[1], 2)), 
  new Button(saveX+180, 205, 45, 45, 
  color(230), nf(date_input[2], 2)), 
  new Button(saveX+565, 205, 55, 45, 
  color(230), nf(date_input[3], 2)), 
  new Button(saveX+630, 205, 40, 45, 
  color(230), "00")};

Button[] cal_mov_button = 
  {new Button(saveX + 15, 280, 40, 40, 
  color(200), "<"), 
  new Button(saveX+650, 280, 40, 40, 
  color(200), ">")};

CalendarPrint cal1, cal2;

int impX = 820;
int impY = 285;

Button[] importance_button = 
  {new Button(impX, impY, 40, 40, 
  color(200), ""), 
  new Button(impX+60, impY, 40, 40, 
  color(200), ""), 
  new Button(impX+120, impY, 40, 40, 
  color(200), ""), 
  new Button(impX+180, impY, 40, 40, 
  color(200), ""), 
  new Button(impX+240, impY, 40, 40, 
  color(200), ""), 
};

int estX = 820;
int extY = 440;
Button[] estimate_button = 
  {new Button(estX, extY, 40, 45, 
  color(230), nf(esti_input[0], 2)), 
  new Button(estX+60, extY, 40, 45, 
  color(230), nf(esti_input[1], 2))};


public void setup() {
  
  background(0);

  frame.setResizable(true);
  surface.setTitle("마법의 소라고동");

  try {
    myFont = createFont("NanumSquareRound Regular", 30);
  } 
  catch(Error e) {
    myFont = createFont("KoPubWorldDotum_Pro Medium", 30);
    try {
    } 
    catch(Error e1) {
      myFont = createFont("굴림", 32);
    }
  }
  //myFont = loadFont("KoPubWorldDotumMedium-48.vlw");
  textFont(myFont);
  fill(255);
  textAlign(CENTER, CENTER);
  textSize(40);
  text("마법의 소라고동 당기는 중", width/2, height/2);
  //codeFont = createFont("Consolas", 32);
  //textAlign(CENTER, CENTER);
  //text("", width/2, height/2);

  //get file

  try { 
    assig_table = loadTable("assignment.csv", "header");
    assig_table.rows();
  }
  catch(Exception e) {
    println(e);
    assig_table = new Table();
    assig_table.addColumn("id");
    assig_table.addColumn("name");
    assig_table.addColumn("year");
    assig_table.addColumn("month");
    assig_table.addColumn("day");
    assig_table.addColumn("hour");
    assig_table.addColumn("estimated_time");
    assig_table.addColumn("importance");
    assig_table.addColumn("done");
    saveTable(assig_table, "data/assignment.csv");
  }
  for (TableRow row : assig_table.rows()) {
    int datee = row.getInt("year");
    datee = datee*100 + row.getInt("month");
    datee = datee*100 + row.getInt("day");
    datee = datee*100 + row.getInt("hour");
    int ndate = year();
    ndate = ndate*100 + month();
    ndate = ndate*100 + day();
    ndate = ndate*100 + hour();

    //println(row.getInt("id"));
    if (datee > ndate) {
      assig_list.add(new Proj(row.getInt("id"), row.getString("name"), 
        row.getInt("year"), row.getInt("month"), row.getInt("day"), 
        row.getInt("hour"), row.getInt("estimated_time"), row.getInt("importance"), row.getInt("done")));
    } else {
    }
  }
  Collections.sort(assig_list, new Comparator<Proj>() {
    @Override
      public int compare(Proj p1, Proj p2) {
      float fp1 = p1.getUrgen();
      float fp2 = p2.getUrgen();
      if (fp1 < fp2) {
        return 1;
      } else if (fp1 > fp2) {
        return -1;
      } else {
        return 0;
      }
    }
  }
  );

  importance_button[0].click();

  try {
    cal1 = new CalendarPrint(year(), month());
    cal1.setButton(saveX, 330);
  } 
  catch(Exception e) {
  }

  title_input=""; 

  //String[] fontList = PFont.list();
  //printArray(fontList);

  sort_button[sort_mode].click();

  for (int i=0; i<4; i++) {
    graph_range_button[i].setTxtColor(color(200));
  }
  graph_range_button[2].click();
}

///////////////////////////////////////////////
///////////////////draw////////////////////////


public void draw() {
  if (left_window_y > 0) {
    left_window_y = left_window_y - (left_window_y+10) / 10;
  } else if (left_window_y < -47500) {
    left_window_y = left_window_y - (left_window_y+47500) / 10;
  }
  if (right_window_y > 0) {
    right_window_y = right_window_y - (right_window_y+10) / 10;
  } else if (right_window_y < right_window_y_min) {
    right_window_y = right_window_y - (right_window_y-right_window_y_min) / 10;
  }

  background(250);
  noStroke();
  //strokeWeight(1);
  fill(210, 190, 190);
  rectMode(CORNER);
  rect(1500, 0, width-1500, height);
  fill(0);
  textSize(20);
  textAlign(LEFT, TOP);
  text("0을 눌러 원래 화면크기 전환", 1510, 20);

  fill(50, 60, 80);
  textSize(20);
  textAlign(LEFT, BOTTOM);
  text("CopyRight by JM", 1500+10, height - 90);
  text("james1990a@sasa.hs.kr", 1500+10, height - 60);
  text("무단 복제 및 재배포를 금지합니다.", 1500+10, height - 30);

  if (window_num == 0) {
    //main
    noStroke();
    fill(250, 245, 245);
    rectMode(CORNER);
    rect(1500/2-1, 0, 1500/2+2, height); 
    fill(0);
    stroke(220, 140, 140);
    strokeWeight(2);
    line(1500/2, 0, 1500/2, height);

    //left window : time line
    fill(200);
    noStroke();
    rect(0, 0, 100, height);

    if (timeline_mode == 0) {
      //timeline mode : hour
      /////////////////////
      fill(200, 0, 0);
      stroke(200, 0, 0);
      textSize(15);
      textAlign(RIGHT, BOTTOM);
      text("now", 92, 105 + left_window_y);
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      DateFormat df = new SimpleDateFormat("MM / dd");
      for (int i=0; i<1000; i++) {
        if (i*scale*50+left_window_y > -100 && i*scale*50+left_window_y < 2*height) {
          String time;
          if (i+hour() < 24) {
            time = nf(i+hour(), 2);
          } else if ( (i+hour())%24 == 0 ) 
          {
            cal.add(Calendar.DATE, 1);
            fill(10, 10, 150);
            stroke(0);
            textSize(15);
            textAlign(LEFT, BOTTOM);
            text(df.format(cal.getTime()), 0, 105 + i*scale*50 + left_window_y);
            time= "00";
          } else 
          {
            time = nf( (i+hour())%24, 2);
          }

          fill(0);
          stroke(0);
          strokeWeight(1);
          textSize(20);
          textAlign(LEFT, BOTTOM);
          text(time+"시", 50, 130 + i*scale*50+left_window_y);
          line(0, 130 + i*scale*50+1 + left_window_y, 100, 130 + i*scale*50+1 + left_window_y);
        }
      }

      int[] data_num = new int[9999];
      int[] cnt_num = new int[9999];
      for (int i =0; i<9999; i++) {
        data_num[i] = 0;
        cnt_num[i] = 0;
      }

      Iterator<Proj> itData2 = assig_list.iterator();
      while (itData2.hasNext()) {
        Proj now = itData2.next();
        data_num[PApplet.parseInt(now.leftTime())] += 1;
      }

      itData2 = assig_list.iterator();
      while (itData2.hasNext()) {
        Proj now = itData2.next();
        int idx = PApplet.parseInt(now.leftTime());
        now.setTButton(103+635/data_num[idx] * cnt_num[idx], 50*idx + left_window_y+82, 635/data_num[idx]-5, 50);
        cnt_num[idx] += 1;
        now.setTColor();
        now.drawTB();
      }
    } else if (timeline_mode == 1) { 
      //timeline mode : day
      /////////////////////
      fill(200, 0, 0);
      stroke(200, 0, 0);
      textSize(15);
      textAlign(LEFT, BOTTOM);
      text("Today", 5, 105 + left_window_y);
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      int dow = cal.get(Calendar.DAY_OF_WEEK)%7;
      String[] calHeader = { "Sa", "Su", "Mo", "Tu", "We", "Th", "Fr"};
      DateFormat df = new SimpleDateFormat("MM/dd");
      for (int i=0; i<1000; i++) {
        if (i*scale*50+left_window_y > -100 && i*scale*50+left_window_y < 2*height) {
          fill(0);
          if (dow == 0) fill(0, 0, 160);
          else if (dow == 1) fill(120, 0, 0);
          stroke(0);
          strokeWeight(1);
          textSize(20);
          textAlign(LEFT, BOTTOM);
          text(df.format(cal.getTime()), 5, 130 + i*scale*50+left_window_y);
          line(0, 130 + i*scale*50+1 + left_window_y, 100, 130 + i*scale*50+1 + left_window_y);

          if (day_show) {
            textAlign(RIGHT, BOTTOM);
            textSize(16);
            text(calHeader[dow], 95, 130 + i*scale*50+left_window_y);
          }
        }
        cal.add(Calendar.DATE, 1);
        dow = (dow+1)%7;
      }

      int[] data_num = new int[9999];
      int[] cnt_num = new int[9999];
      for (int i =0; i<9999; i++) {
        data_num[i] = 0;
        cnt_num[i] = 0;
      }

      Iterator<Proj> itData2 = assig_list.iterator();
      while (itData2.hasNext()) {
        Proj now = itData2.next();
        data_num[now.leftDay()] += 1;
      }

      itData2 = assig_list.iterator();
      while (itData2.hasNext()) {
        Proj now = itData2.next();
        int idx = PApplet.parseInt(now.leftDay());
        now.setTButton(103+635/data_num[idx] * cnt_num[idx], 50*(idx) + left_window_y+82, 635/data_num[idx]-5, 50);
        cnt_num[idx] += 1;
        now.setTColor();
        now.drawTB();
      }
    }

    noStroke();
    fill(100);
    rect(1, 0, 1500/2+1, 80, 0, 0, 8, 8);

    timeline_button.drawB();
    dow_button.drawB();

    //right window : importance
    Iterator<Proj> itData = assig_list.iterator();
    int cnt = 0;
    while (itData.hasNext()) {
      Proj now = itData.next();
      int yy = now.getY1();
      yy = (2*yy + (175+160*cnt+right_window_y))/3;
      if (abs(yy - (175+160*cnt+right_window_y)) == 1) {
        yy = 175+160*cnt+right_window_y;
      }
      now.setButton(750, yy, 700, 150);
      if (-200 < yy && yy < height + 200) {
        now.setColor();
        now.drawB();
      }
      cnt++;
    }
    right_window_y_min = min(0, -(160*assig_list.size()-height+200));

    noStroke();
    fill(220, 110, 110);
    rect(1500/2-1, 70, 1500/2+1, 85, 0, 0, 8, 8);
    noStroke();
    fill(200, 100, 100);
    rect(1500/2-1, 0, 1500/2+1, 80, 0, 0, 8, 8);

    stroke(0);
    fill(0);
    textSize(25);
    textAlign(LEFT, CENTER);
    text(str(year())+" / "+nf(month(), 2) + " / " + nf(day(), 2) + "  .   " +
      nf(hour(), 2) + " : " + nf(minute(), 2), 1500/2 + 30, 40);
    make_work.drawB();
    graph_button.drawB();
    for (int i=0; i<5; i++) {
      sort_button[i].drawClickedB3();
    }
  }
  //////
  else if (window_num == 1) {
    //graph
    int max_time = 5;

    noStroke();
    fill(120, 100, 180);
    rect(0, 0, 1500, 85, 0, 0, 8, 8);

    go_back.drawB();


    fill(100);
    stroke(100);
    strokeWeight(5);
    line(100, 800, 1400, 800);

    fill(100);
    stroke(100);
    strokeWeight(4);
    line(100, 800, 100, 200);
    strokeWeight(1);
    line(1400, 800, 1400, 200);


    fill(100);
    stroke(100, 50, 100, 100);
    strokeWeight(1);
    for (int i=0; i<5; i++) {
      line(100, 200+i*120, 1400, 200+i*120);
    }


    //fill(150, 100);
    //stroke(150, 100);
    //strokeWeight(1);
    //for (int i=1; i<14; i++) {
    //  for (int j=200; j<800; j+=20) {
    //    ellipse(100+i*1300/14, j, 2, 2);
    //  }
    //}
    fill(100);
    stroke(100);
    strokeWeight(1);
    for (int i=0; i<=graph_x_size; i++) {
      line(100+i*1300/graph_x_size, 800, 100+i*1300/graph_x_size, 810);
    }

    int[] data_num = new int[9999];
    int[] cnt_num = new int[9999];
    for (int i =0; i<9999; i++) {
      data_num[i] = 0;
      cnt_num[i] = 0;
    }

    Iterator<Proj> itData2 = assig_list.iterator();
    while (itData2.hasNext()) {
      Proj now = itData2.next();
      if (now.getDone() == 1) continue;
      data_num[now.leftDay()] += now.getEstimatedTime();
    }
    for (int i=0; i<graph_x_size; i++) {
      max_time = max(max_time, data_num[i]);
    }
    max_time = 5*((max_time+4)/5);

    fill(0);
    stroke(0);
    strokeWeight(1);
    textAlign(LEFT, CENTER);
    textSize(16);
    text("과제소요", 30, 150);
    text("예상시간(h)", 30, 170);

    fill(0);
    stroke(0);
    strokeWeight(1);
    textAlign(RIGHT, CENTER);
    textSize(18);
    for (int i=0; i<6; i++) {
      text(str((max_time/5)*(5-i)), 95, -2+200+i*120);
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    DateFormat df = new SimpleDateFormat("MM/dd");

    for (int i=day(); i<day() + graph_x_size; i++) {
      int posid = i-day();
      int tot_hour = data_num[posid];
      float yy = graph_height[posid];
      int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
      dow = (dow + posid)%7;
      yy = (9*yy - 600*tot_hour/max_time)/ 10.0f;
      graph_height[posid] = yy;

      colorMode(HSB);
      noStroke();
      fill(183, 177, 255-255*tot_hour/(max_time+10));
      rectMode(CORNER);
      rect(100+posid*1300/graph_x_size +5, 800, 1300/graph_x_size -10, yy);
      colorMode(RGB);

      fill(0);
      if (posid == 0) fill(0, 100, 0);
      else if (dow == 1) fill(160, 20, 20);
      else if (dow == 0) fill(50, 50, 180);
      stroke(0);
      textAlign(CENTER, TOP);
      textSize(20);
      if (graph_x_size == 28) textSize(14);
      text(df.format(cal.getTime()), 100+posid*1300/graph_x_size+650/graph_x_size, 830);
      String[] calHeader = { "Sa", "Su", "Mo", "Tu", "We", "Th", "Fr"};
      textSize(12);
      text(calHeader[dow], 100+posid*1300/graph_x_size+650/graph_x_size, 810);
      cal.add(Calendar.DATE, 1);
    }

    for (int i=0; i<4; i++) {
      graph_range_button[i].drawClickedB3();
    }
  } 
  ///////
  else if (window_num/10 == 1) {

    // make window
    noStroke();
    fill(150, 150, 230);
    rect(0, 0, 1500, 80);

    noStroke();
    fill(0);
    textSize(25);
    textAlign(CENTER, CENTER);
    text("New Assignment", 1500/2, 40);

    //title input
    title.drawB();
    if (title.isClicked()) {
      title.drawL(color(10, 10, 200));
    }
    fill(255);
    textAlign(LEFT, BOTTOM);
    text("Title", saveX+20, 160);
    fill(255);
    stroke(255);
    line(saveX+85, 125, saveX+85, 165);

    fill(255); 
    textSize(25);
    textAlign(LEFT, BOTTOM);
    text (title_input, saveX+100, 160);

    if (title.isClicked() && frameCount%60 < 30) {
      fill(255);
      strokeWeight(2);
      stroke(220, 220, 0);
      line(saveX+105 + textWidth(title_input), 135, saveX+105 + textWidth(title_input), 160);
      strokeWeight(1);
    }


    //date input
    noStroke();
    fill(230);
    rectMode(CORNER);
    rect(saveX, 200, 710, 55, 8); 

    for (int i=0; i<5; i++) {
      date_button[i].drawClickedB();
    }

    //year-month
    fill(2);
    textSize(25);
    textAlign(LEFT, BOTTOM);
    text("/", saveX+95, 244);
    //month-day
    fill(2);
    textSize(25);
    textAlign(LEFT, BOTTOM);
    text("/", saveX+165, 244);
    //hour-min
    fill(2);
    textSize(25);
    textAlign(LEFT, BOTTOM);
    text(":", saveX+622, 240);
    //calendar
    fill(150);
    noStroke();
    rectMode(CORNER);
    rect(saveX, 270, 710, 60, 8);
    fill(0);
    textSize(25);
    textAlign(CENTER, CENTER);
    text(nf(cal1.getYear(), 4) + " / " + nf(cal1.getMonth(), 2), saveX + 355, 300 );

    fill(200);
    noStroke();
    rectMode(CORNER);
    rect(saveX, 330, 710, 560, 8);

    cal1.drawCal();
    cal_mov_button[0].drawB();
    cal_mov_button[1].drawB();

    //importance
    //box
    fill(230);
    noStroke();
    rectMode(CORNERS);
    rect(importance_button[0].midX() - 45, importance_button[0].midY()-105, 
      importance_button[4].midX() + 55, importance_button[0].midY()+35, 8);
    //button
    for (int i=0; i<5; i++) {
      importance_button[i].drawClickedB2();
      fill(0);
      textSize(20);
      textAlign(CENTER, CENTER);
      text(str(i+1), importance_button[i].midX(), importance_button[i].midY() - 40);
    }
    //head
    stroke(180, 180, 250);
    strokeWeight(3);
    line(importance_button[0].midX() - 40, importance_button[0].midY()-65, 
      importance_button[4].midX() + 50, importance_button[0].midY()-65);
    fill(0);
    textSize(25);
    textAlign(LEFT, BOTTOM);
    text("importance ", importance_button[0].midX() - 30, importance_button[0].midY()-70);

    //estimate_time
    //box
    fill(230);
    noStroke();
    rectMode(CORNERS);
    rect(estimate_button[0].midX() - 45, estimate_button[0].midY()-75, 
      estimate_button[1].midX() + 235, estimate_button[0].midY()+35, 8);
    //button
    for (int i=0; i<2; i++) {
      estimate_button[i].drawClickedB();
    }
    textSize(25);
    stroke(0);
    fill(0);
    textAlign(LEFT, CENTER);
    text(":", estimate_button[0].midX()+27, estimate_button[0].midY()-2);
    //head
    stroke(180, 180, 250);
    strokeWeight(3);
    line(estimate_button[0].midX() - 40, estimate_button[0].midY()-35, 
      estimate_button[1].midX() + 230, estimate_button[0].midY()-35);
    fill(0);
    textSize(25);
    textAlign(LEFT, BOTTOM);
    text("estimate time ", estimate_button[0].midX() - 30, estimate_button[0].midY()-40);

    //move button
    go_back.drawB();
    push_work.drawB();
  }
}

////////////////////////////////////////////
///////////////**mouse**////////////////////
////////////////////////////////////////////

public void mouseClicked() {

  if (!title.isMouseOn()) {
    title.unClick();
  }
  for (int i=0; i<5; i++) {
    if (!date_button[i].isMouseOn()) {
      date_button[i].unClick();
    }
  }
  for (int i=0; i<2; i++) {
    if (!estimate_button[i].isMouseOn()) {
      estimate_button[i].unClick();
    }
  }

  //println(str(mouseX)+", "+str(mouseY)+". it is mouse");

  //main window
  if (window_num == 0) {
    mainWindow();
  }

  //graph window
  else if (window_num == 1) {
    graphWindow();
  }

  //making window
  else if (window_num == 10) { 
    int idx = 0;
    if (assig_table.getRowCount() > 0) {
      idx = assig_table.getRow(assig_table.lastRowIndex()).getInt("id");
    }
    makingWindow(idx+1, false);
  }

  //edit window
  else if (window_num == 11) {
    makingWindow(edit_row_id, true);
  }
}

////////////////////////////////////////
////////////////////////////////////////
//////////////**key**///////////////////
////////////////////////////////////////
////////////////////////////////////////

public void keyTyped() {
  //println(int(key));

  //if (key == CODED) println(int(keyCode));
  if (window_num/10 == 1) {
    //title
    if (title.isClicked() && textWidth(title_input) < 720) {
      if (32 <= key && key != 127) {
        title_input = title_input + key;
      }
    }
    if (title.isClicked() && key == 8 && title_input.length() > 0) {
      title_input = title_input.substring(0, title_input.length() - 1);
    }
  }
}
public void keyPressed() {
  if (window_num == 0) {
    if (key == '0') {
      ;
      frame.setSize(1500, 1000);
      //println("hi");
    }
  }
  if (window_num/10 == 1) {
    //year
    if (date_button[0].isClicked() && date_input[0] >= 1 && date_input[0] < 4000) {
      if (key == CODED) {
        if (keyCode == 38) {
          date_input[0]+=1;
        } else if (keyCode == 40) {
          date_input[0]-=1;
        }
        date_input[0] = min(3000, max(2000, date_input[0]));
      }
      date_button[0].changeTxt(nf(date_input[0], 4));
    }
    //month
    if (date_button[1].isClicked() && date_input[1] >= 1 && date_input[1] < 13) {
      if (key == CODED) {
        if (keyCode == UP) {
          date_input[1]++;
        } else if (keyCode == DOWN) {
          date_input[1]--;
        }
        date_input[1] = min(12, max(1, date_input[1]));
      }
      date_button[1].changeTxt(nf(date_input[1], 2));
    }
    //day
    if (date_button[2].isClicked() && date_input[2] >= 1 && date_input[2] < 32) {
      if (key == CODED) {
        if (keyCode == UP) {
          date_input[2]++;
        } else if (keyCode == DOWN) {
          date_input[2]--;
        }
        date_input[2] = min(31, max(1, date_input[2]));
      }
      date_button[2].changeTxt(nf(date_input[2], 2));
    }
    //hour
    if (date_button[3].isClicked() && date_input[3] >= 0 && date_input[3] < 24) {
      if (key == CODED) {
        if (keyCode == UP) {
          date_input[3]++;
        } else if (keyCode == DOWN) {
          date_input[3]--;
        }
        date_input[3] = min(23, max(0, date_input[3]));
      }
      date_button[3].changeTxt(nf(date_input[3], 2));
    }
    //cal
    try {
      cal1 = new CalendarPrint(date_input[0], date_input[1]);
      cal1.setButton(saveX, 330);
      cal1.clickCal(date_input[2]);
    } 
    catch(Exception e) {
    }

    //estimate
    if (estimate_button[0].isClicked() && esti_input[0] >= 0 && esti_input[0] <= 99) {
      if (key == CODED) {
        if (keyCode == UP) {
          esti_input[0]++;
        } else if (keyCode == DOWN) {
          esti_input[0]--;
        }
        esti_input[0] = min(99, max(0, esti_input[0]));
      }
      if ('0' <= key && key <= '9') {
        esti_input[0] *= 10;
        esti_input[0] += PApplet.parseInt(key-'0');
        esti_input[0] %= 100;
      }
      estimate_button[0].changeTxt(nf(esti_input[0], 2));
    }
  }
  if (estimate_button[1].isClicked() && esti_input[1] >= 0 && esti_input[1] <= 99) {
    if (key == CODED) {
      if (keyCode == UP || keyCode == DOWN) {
        //if (esti_input[1] == 0) {
        //  esti_input[1] = 30;
        //} else {
        //  esti_input[1] = 0;
        //}
      }
    }
    estimate_button[1].changeTxt(nf(esti_input[1], 2));
  }
}

public void mouseWheel(MouseEvent event) {
  float e = event.getCount();
  if (window_num == 0) {
    if (mouseX < 1500/2) {
      left_window_y -= 16* PApplet.parseInt(e);
    } else {
      right_window_y -= 16* PApplet.parseInt(e);
    }
    //println(e);
  }
}
class Button {
  int x1, y1, x2, y2;
  int col;
  String txt;
  boolean clicked = false;
  int txt_color = color(0);

  Button (int tx1, int ty1, int tx2, int ty2, int c, String t) {
    x1 = tx1;
    y1 = ty1;
    x2 = tx1+tx2;
    y2 = ty1+ty2;
    col = c;
    txt = t;
  }
  public boolean isMouseOn() {
    boolean re = false;
    if ( x1 < mouseX && x2 > mouseX && y1 < mouseY && y2 > mouseY ) {
      re = true;
    }
    return re;
  }
  public void changeTxt(String ttxt) {
    txt = ttxt;
  }
  public void drawB() {
    if (!isMouseOn()) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(col);
      rect(x1, y1, x2, y2, 8);

      rectMode(CORNER);
      fill(txt_color);
      stroke(0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(4*red(col)/5, 4*green(col)/5, 4*blue(col)/5, alpha(col));
      rect(x1, y1, x2, y2, 8);
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      ////fill((5*red(col)+255)/6 , (5*green(col)+255)/6, (5*blue(col)+255)/6);
      //fill(col);
      //rect(x1+5, y1+5, x2-5, y2-5);

      rectMode(CORNER);
      fill(250);
      stroke(200, 0, 0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    }
  }
  public void drawB2() {
    if (!isMouseOn()) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(col);
      rect(x1, y1, x2, y2, 0, 10, 14, 0);

      rectMode(CORNER);
      fill(txt_color);
      stroke(0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(4*red(col)/5, 4*green(col)/5, 4*blue(col)/5, alpha(col));
      rect(x1, y1, x2, y2, 0, 10, 14, 0);
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      ////fill((5*red(col)+255)/6 , (5*green(col)+255)/6, (5*blue(col)+255)/6);
      //fill(col);
      //rect(x1+5, y1+5, x2-5, y2-5);

      rectMode(CORNER);
      fill(250);
      stroke(200, 0, 0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    }
  }
  public void drawClickedB() {
    if (!clicked && !isMouseOn()) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(col);
      rect(x1, y1, x2, y2, 8);

      rectMode(CORNER);
      fill(txt_color);
      stroke(0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else if (clicked) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(col);
      rect(x1, y1, x2, y2, 8);

      rectMode(CORNER);
      fill(100, 100, 255);
      noStroke();
      rect((x1+x2)/2 - textWidth(txt)/2-2, (y1+y2)/2-14, textWidth(txt)+4, 30);

      fill(255);
      stroke(200, 0, 0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else if ( isMouseOn() ) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      rect(x1, y1, x2, y2, 8);
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      ////fill((5*red(col)+255)/6 , (5*green(col)+255)/6, (5*blue(col)+255)/6);
      //fill(col);
      //rect(x1+5, y1+5, x2-5, y2-5);

      rectMode(CORNER);
      fill(200, 0, 0);
      stroke(200, 0, 0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    }
  }
  public void drawClickedB(int fc) {
    if (!clicked && !isMouseOn()) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(col);
      rect(x1, y1, x2, y2, 8);

      rectMode(CORNER);
      fill(fc);
      stroke(0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else if (clicked) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(col);
      rect(x1, y1, x2, y2, 8);

      rectMode(CORNER);
      fill(100, 100, 255);
      noStroke();
      rect((x1+x2)/2 - textWidth(txt)/2-2, (y1+y2)/2-14, textWidth(txt)+4, 30);

      fill(255);
      stroke(200, 0, 0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else if ( isMouseOn() ) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      rect(x1, y1, x2, y2, 8);
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      ////fill((5*red(col)+255)/6 , (5*green(col)+255)/6, (5*blue(col)+255)/6);
      //fill(col);
      //rect(x1+5, y1+5, x2-5, y2-5);

      rectMode(CORNER);
      fill(200, 0, 0);
      stroke(200, 0, 0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    }
  }
  public void drawClickedB2() {
    if (!clicked) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(col);
      rect(x1, y1, x2, y2, 16);

      rectMode(CORNER);
      fill(txt_color);
      stroke(0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else if (clicked) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(100, 180, 200);
      rect(x1, y1, x2, y2, 16);

      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(255);
      rect(x1+5, y1+5, x2-5, y2-5, 11);

      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(100, 180, 200);
      rect(x1+11, y1+11, x2-11, y2-11, 6);
    }
  }
  public void drawClickedB3() {
    if (!clicked && !isMouseOn()) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(col);
      rect(x1, y1, x2, y2, 8);

      rectMode(CORNER);
      fill(txt_color);
      stroke(0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else if (clicked) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(7*red(col)/10, 7*green(col)/10, 7*blue(col)/10, alpha(col));
      rect(x1, y1, x2, y2, 8);

      fill(txt_color);
      stroke(200, 0, 0);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    } else if ( isMouseOn() ) {
      rectMode(CORNERS);
      noStroke();
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      fill(4*red(col)/5, 4*green(col)/5, 4*blue(col)/5, alpha(col));
      rect(x1, y1, x2, y2, 8);
      //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
      ////fill((5*red(col)+255)/6 , (5*green(col)+255)/6, (5*blue(col)+255)/6);
      //fill(col);
      //rect(x1+5, y1+5, x2-5, y2-5);

      rectMode(CORNER);
      fill(txt_color);
      stroke(200);
      textSize(25);
      textAlign(CENTER, CENTER);
      text(txt, (x1+x2)/2, (y1+y2)/2-2);
    }
  }
  public void click() {
    clicked = true;
  }
  public void unClick() {
    clicked = false;
  }
  public void drawL(int c) {
    rectMode(CORNERS);
    strokeWeight(1);
    stroke(c);
    //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
    noFill();
    rect(x1, y1, x2, y2, 8);
  }
  public boolean isClicked() {
    return clicked;
  }
  public void setColor(int c) {
    col = c;
  }
  public void setTxtColor(int c) {
    txt_color = c;
  }
  public int midX() {
    return (x1+x2)/2;
  }
  public int midY() {
    return (y1+y2)/2;
  }
  public int getX1() {
    return x1;
  }
  public int getX2() {
    return x2;
  }
  public int getY1() {
    return y1;
  }
  public int getY2() {
    return y2;
  }
}




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
public void makingWindow(int idx, boolean is_edit) {
  if (go_back.isMouseOn()) {
    window_num = 0;
  }
  //title
  if (title.isMouseOn()) {
    title.click();
  }
  //date
  for (int i=0; i<5; i++) {
    if (date_button[i].isMouseOn()) {
      date_button[i].click();
    }
  }
  //calendar
  if (cal1.clickCal()) {
    date_input[0] = cal1.getYear();
    date_input[1] = cal1.getMonth();
    date_input[2] = cal1.getDay();
    date_button[0].changeTxt(nf(date_input[0], 4));
    date_button[1].changeTxt(nf(date_input[1], 2));
    date_button[2].changeTxt(nf(date_input[2], 2));
  }
  if (cal_mov_button[0].isMouseOn()) {
    int tm = cal1.getMonth() - 1;
    int ty = cal1.getYear();
    if (tm < 1) {
      tm = 12;
      ty -= 1;
    }
    try {
      cal1 = new CalendarPrint(ty, tm);
      cal1.setButton(saveX, 330);
      cal1.clickCal(date_input[2]);
    } 
    catch(Exception e) {
    }
  }
  if (cal_mov_button[1].isMouseOn()) {
    int tm = cal1.getMonth() + 1;
    int ty = cal1.getYear();
    if (tm > 12) {
      tm = 1;
      ty += 1;
    }
    try {
      cal1 = new CalendarPrint(ty, tm);
      cal1.setButton(saveX, 330);
      cal1.clickCal(date_input[2]);
    } 
    catch(Exception e) {
    }
  }
  //imp
  for (int i=0; i<5; i++) {
    if (importance_button[i].isMouseOn()) {
      for (int j=0; j<5; j++) {
        importance_button[j].unClick();
      }
      importance_button[i].click();
    }
  }
  //estimate
  for (int i=0; i<2; i++) {
    if (estimate_button[i].isMouseOn()) {
      estimate_button[i].click();
    }
  }
  //pushwork
  if (push_work.isMouseOn()) {
    if (title_input.length() > 0) {
      window_num = 0;


      TableRow newRow;
      if (!is_edit) { 
        newRow = assig_table.addRow();
      } else {
        newRow = assig_table.findRow(str(idx), "id");
      }
      newRow.setInt("id", idx);
      newRow.setString("name", title_input);
      newRow.setInt("year", date_input[0]);
      newRow.setInt("month", date_input[1]);
      newRow.setInt("day", date_input[2]);
      newRow.setInt("hour", date_input[3]);
      newRow.setInt("estimated_time", esti_input[0]);
      newRow.setInt("done", 0);
      int impt = 0;
      for (int i=0; i<5; i++) {
        if (importance_button[i].isClicked()) {
          impt = i;
        }
      }
      newRow.setInt("importance", impt+1);
      saveTable(assig_table, "data/assignment.csv");
      int datee = newRow.getInt("year");
      datee = datee*100 + newRow.getInt("month");
      datee = datee*100 + newRow.getInt("day");
      datee = datee*100 + newRow.getInt("hour");
      int ndate = year();
      ndate = ndate*100 + month();
      ndate = ndate*100 + day();
      ndate = ndate*100 + hour();

      if (datee > ndate) {
        if (is_edit) {
          Iterator<Proj> itData2 = assig_list.iterator();
          while (itData2.hasNext()) {
            Proj now = itData2.next();
            if (now.getId() == idx) {
              itData2.remove();
            }
          }
        }
        assig_list.add(new Proj(newRow.getInt("id"), newRow.getString("name"), 
          newRow.getInt("year"), newRow.getInt("month"), newRow.getInt("day"), 
          newRow.getInt("hour"), newRow.getInt("estimated_time"), newRow.getInt("importance"), 0));
        println("saved");
      } else {
      }

      Collections.sort(assig_list, new Comparator<Proj>() {
        @Override
          public int compare(Proj p1, Proj p2) {
          float fp1 = p1.getUrgen();
          float fp2 = p2.getUrgen();
          if (fp1 < fp2) {
            return 1;
          } else if (fp1 > fp2) {
            return -1;
          } else {
            return 0;
          }
        }
      }
      );
    } else {
      for (int i=0; i<5; i++) {
        date_button[i].unClick();
      }
      estimate_button[0].unClick();
      estimate_button[1].unClick();
      title.click();
    }
  }
}

public void mainWindow() {
  if (timeline_button.isMouseOn()) {
    int tm = timeline_mode;
    timeline_mode += 1;
    timeline_mode %= 2;
    if (tm == 0) {
      timeline_button.changeTxt("Hour");
    } else if (tm == 1) {
      timeline_button.changeTxt("Day");
    }
    left_window_y = 0;
  }
  if(dow_button.isMouseOn()) {
    if(day_show) {
      dow_button.changeTxt("day on");
      day_show = false;
    } else {
      dow_button.changeTxt("day off");
      day_show = true;
    }
  }
  if (graph_button.isMouseOn()) {
    window_num = 1;
    for(int i=0; i<28; i++) {
      graph_height[i] = 0;
    }
  }
  if (make_work.isMouseOn()) {
    window_num = 10;

    //reset
    title_input = "";
    date_input[0] = year();
    date_input[1] = month();
    date_input[2] = day();
    date_input[3] = hour();
    date_input[4] = 0;
    date_button[0].changeTxt(nf(date_input[0], 4));
    date_button[1].changeTxt(nf(date_input[1], 2));
    date_button[2].changeTxt(nf(date_input[2], 2));
    date_button[3].changeTxt(nf(date_input[3], 2));
    date_button[4].changeTxt("00");
    //left_window_y = right_window_y = 0;
    esti_input[0] = 1;
    esti_input[1] = 0;
    estimate_button[0].changeTxt(nf(esti_input[0], 2));
    
    for (int i=0; i<5; i++) {
      importance_button[i].unClick();
    }
    importance_button[0].click();

    try {
      cal1 = new CalendarPrint(year(), month());
      cal1.setButton(saveX, 330);
      cal1.clickCal(date_input[2]);
    } 
    catch(Exception e) {
    }
  }

  //Sort
  for (int i=0; i<5; i++) {
    if (sort_button[i].isMouseOn()) {
      sort_mode = i;
      for (int j=0; j<5; j++) {
        sort_button[j].unClick();
      }
      sort_button[i].click();
    }
  }

  Iterator<Proj> itData = assig_list.iterator();
  while (itData.hasNext()) {
    Proj now = itData.next();

    //Done
    if (now.isDoneMouseOn()) {
      now.done();
      int idx = assig_table.findRowIndex(str(now.getId()), "id");
      assig_table.setInt(idx, "done", now.getDone());
      saveTable(assig_table, "data/assignment.csv");
    }

    //updown
    int ud = now.isUpDownMouseOn();
    if (ud != 0) {
      now.addE(ud);
      int idx = assig_table.findRowIndex(str(now.getId()), "id");
      assig_table.setInt(idx, "estimated_time", now.getEstimatedTime());
      saveTable(assig_table, "data/assignment.csv");
    }

    //edit
    if (now.isEditMouseOn()) {
      window_num = 11;
      title_input = now.getTitle();
      date_input[0] = now.getYear();
      date_input[1] = now.getMonth();
      date_input[2] = now.getDay();
      date_input[3] = now.getHour();
      date_input[4] = 0;
      date_button[0].changeTxt(nf(date_input[0], 4));
      date_button[1].changeTxt(nf(date_input[1], 2));
      date_button[2].changeTxt(nf(date_input[2], 2));
      date_button[3].changeTxt(nf(date_input[3], 2));
      date_button[4].changeTxt("00");
      left_window_y = right_window_y = 0;
      esti_input[0] = now.getEstimatedTime();
      esti_input[1] = 0;
      estimate_button[0].changeTxt(nf(esti_input[0], 2));
      edit_row_id = now.getId();
      for (int i=0; i<5; i++) {
        importance_button[i].unClick();
      }
      println(now.getImportance());
      importance_button[now.getImportance()-1].click();

      try {
        cal1 = new CalendarPrint(date_input[0], date_input[1]);
        cal1.setButton(saveX, 330);
        cal1.clickCal(date_input[2]);
      } 
      catch(Exception e) {
      }
    }

    //Del
    if (now.isDelMouseOn()) {
      int idx = assig_table.findRowIndex(str(now.getId()), "id");
      println(str(idx) + ", "+str(now.getId())+", "+now.getTitle());
      if (idx >-1) {
        assig_table.removeRow(idx);
        itData.remove();
        saveTable(assig_table, "data/assignment.csv");
        assig_table = loadTable("assignment.csv", "header");
      }
    }
  }


  if (sort_mode == 0) {
    Collections.sort(assig_list, new Comparator<Proj>() {
      @Override
        public int compare(Proj p1, Proj p2) {
        float fp1 = p1.getUrgen();
        float fp2 = p2.getUrgen();
        if (fp1 < fp2) {
          return 1;
        } else if (fp1 > fp2) {
          return -1;
        } else {
          return 0;
        }
      }
    }
    );
  } else if (sort_mode == 1) {
    Collections.sort(assig_list, new Comparator<Proj>() {
      @Override
        public int compare(Proj p1, Proj p2) {
        float fp1 = p1.leftTime();
        float fp2 = p2.leftTime();
        if (fp1 < fp2) {
          return 1;
        } else if (fp1 > fp2) {
          return -1;
        } else {
          return 0;
        }
      }
    }
    );
  } else if (sort_mode == 2) {
    Collections.sort(assig_list, new Comparator<Proj>() {
      @Override
        public int compare(Proj p1, Proj p2) {
        float fp1 = p1.leftTime();
        float fp2 = p2.leftTime();
        if (fp1 < fp2) {
          return -1;
        } else if (fp1 > fp2) {
          return 1;
        } else {
          return 0;
        }
      }
    }
    );
  } else if (sort_mode == 3) {
    Collections.sort(assig_list, new Comparator<Proj>() {
      @Override
        public int compare(Proj p1, Proj p2) {
        float fp1 = p1.getEstimatedTime();
        float fp2 = p2.getEstimatedTime();
        if (fp1 < fp2) {
          return 1;
        } else if (fp1 > fp2) {
          return -1;
        } else {
          return 0;
        }
      }
    }
    );
  } else if (sort_mode == 4) {
    Collections.sort(assig_list, new Comparator<Proj>() {
      @Override
        public int compare(Proj p1, Proj p2) {
        float fp1 = p1.getEstimatedTime();
        float fp2 = p2.getEstimatedTime();
        if (fp1 < fp2) {
          return -1;
        } else if (fp1 > fp2) {
          return 1;
        } else {
          return 0;
        }
      }
    }
    );
  }
}

public void graphWindow() {
  if (go_back.isMouseOn()) {
    window_num = 0;
  }
  for (int i=0; i<4; i++) {
    if (graph_range_button[i].isMouseOn()) {
      for(int j=0; j<70; j++) {
        graph_height[j] = 0;
      }
      for(int j=0; j<4; j++) {
        graph_range_button[j].unClick();
      }
      graph_range_button[i].click();
      if(i==0) {
        graph_x_size = 4;
      } else if(i==1) {
        graph_x_size = 7;
      } else if(i==2) {
        graph_x_size = 14;
      } else if(i==3) {
        graph_x_size = 28;
      }
    }
  }
}





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
  int col;
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
  public int leftDay() {
    // how much day left
    Date d2 = new Date ();
    Date d1 = new Date ();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    try {
    d2 = sdf.parse(nf(year(),4) + nf(month(),2) + nf(day(), 2));
    d1 = sdf.parse(nf(year,4) + nf(month,2) + nf(day, 2));
    } catch(ParseException ex) {
    }
    
    int diff = PApplet.parseInt((d1.getTime() - d2.getTime())/3600000/24);
    return diff;
  }
  public float leftTime() {
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
  public int makeColor() {
    float alp = 0;
    alp = 255*sqrt((getUrgen()+0.5f)/255.5f);
    int re = color(red(col), green(col), blue(col), alp);
    return re;
  }
  public float coordinate(float scale) {
    // set position
    float y = 0;
    y = leftTime();
    y = scale * y;
    return y;
  }
  public void drawB() {
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
  public void drawTB() {
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
  public boolean isDelMouseOn() {
    if (del_button.isMouseOn()) {
      return true;
    } else {
      return false;
    }
  }
  public int isUpDownMouseOn() {
    if (up_button.isMouseOn()) {
      return 1;
    } else if (down_button.isMouseOn()) {
      return -1;
    } else {
      return 0;
    }
  }
  public boolean isDoneMouseOn() {
    if (done_button.isMouseOn()) {
      return true;
    } else {
      return false;
    }
  }
  public boolean isEditMouseOn() {
    if (edit_button.isMouseOn()) {
      return true;
    } else {
      return false;
    }
  }
  public void addE(int i) {
    estimated_time += i;
    estimated_time = max(0, estimated_time);
  }
  public int getEstimatedTime() {
    return estimated_time;
  }
  public int getImportance() {
    return importance;
  }
  public int getId() {
    return id;
  }
  public String getTitle() {
    return name;
  }
  public int getYear() {
    return year;
  }
  public int getMonth() {
    return month;
  }
  public int getDay() {
    return day;
  }
  public int getHour() {
    return hour;
  }
  public int getY1() {
    return y1;
  }
  public float getUrgen() {
    if (is_done) return 0;
    return (250 * estimated_time / leftTime())*(1 + 0.01f* importance);
  }
  public int getDone() {
    if (is_done) return 1;
    else return 0;
  }
  public void setButton(int tx1, int ty1, int tx2, int ty2) {
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
  public void setTButton(int tx1, int ty1, int tx2, int ty2) {
    if (!is_done) {
      tbut = new Button(tx1, ty1, tx2, ty2, makeColor(), "");
    } else {
      tbut = new Button(tx1, ty1, tx2, ty2, color(100, alpha(makeColor())), "");
    }
    del_tbutton = new Button(tx1 + tx2-55, ty1+ty2-45, 50, 40, color(130, 100), "Χ");
  }
  public void setColor() {
    if (!is_done) {
      but.setColor(makeColor());
    } else {
      but.setColor(color(100, alpha(makeColor())));
    }
  }
  public void setTColor() {
    if (!is_done) {
      tbut.setColor(makeColor());
    } else {
      tbut.setColor(color(100, alpha(makeColor())));
    }
  }
  public void setColor(int inp) {
    col = inp;
  }
  public void done() {
    if (is_done) {
      is_done = false;
    } else {
      is_done = true;
    }
  }
}
  public void settings() {  size(1500, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AssignmentList" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
