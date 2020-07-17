class Button {
  int x1, y1, x2, y2;
  color col;
  String txt;
  boolean clicked = false;
  color txt_color = color(0);

  Button (int tx1, int ty1, int tx2, int ty2, color c, String t) {
    x1 = tx1;
    y1 = ty1;
    x2 = tx1+tx2;
    y2 = ty1+ty2;
    col = c;
    txt = t;
  }
  boolean isMouseOn() {
    boolean re = false;
    if ( x1 < mouseX && x2 > mouseX && y1 < mouseY && y2 > mouseY ) {
      re = true;
    }
    return re;
  }
  void changeTxt(String ttxt) {
    txt = ttxt;
  }
  void drawB() {
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
  void drawB2() {
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
  void drawClickedB() {
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
  void drawClickedB(color fc) {
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
  void drawClickedB2() {
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
  void drawClickedB3() {
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
  void click() {
    clicked = true;
  }
  void unClick() {
    clicked = false;
  }
  void drawL(color c) {
    rectMode(CORNERS);
    strokeWeight(1);
    stroke(c);
    //stroke(2*red(col)/3, 2*green(col)/3, 2*blue(col)/3);
    noFill();
    rect(x1, y1, x2, y2, 8);
  }
  boolean isClicked() {
    return clicked;
  }
  void setColor(color c) {
    col = c;
  }
  void setTxtColor(color c) {
    txt_color = c;
  }
  int midX() {
    return (x1+x2)/2;
  }
  int midY() {
    return (y1+y2)/2;
  }
  int getX1() {
    return x1;
  }
  int getX2() {
    return x2;
  }
  int getY1() {
    return y1;
  }
  int getY2() {
    return y2;
  }
}
