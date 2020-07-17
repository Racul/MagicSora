void makingWindow(int idx, boolean is_edit) {
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

void mainWindow() {
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

void graphWindow() {
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
