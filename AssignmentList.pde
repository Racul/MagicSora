import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;


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


void setup() {
  size(1500, 1000);
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


void draw() {
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
        data_num[int(now.leftTime())] += 1;
      }

      itData2 = assig_list.iterator();
      while (itData2.hasNext()) {
        Proj now = itData2.next();
        int idx = int(now.leftTime());
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
        int idx = int(now.leftDay());
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
      yy = (9*yy - 600*tot_hour/max_time)/ 10.0;
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

void mouseClicked() {

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

void keyTyped() {
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
void keyPressed() {
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
        esti_input[0] += int(key-'0');
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

void mouseWheel(MouseEvent event) {
  float e = event.getCount();
  if (window_num == 0) {
    if (mouseX < 1500/2) {
      left_window_y -= 16* int(e);
    } else {
      right_window_y -= 16* int(e);
    }
    //println(e);
  }
}
