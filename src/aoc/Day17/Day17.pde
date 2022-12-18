static final int BLOCK_WIDTH = 10;
static final int FLOOR_HEIGHT = 1700;
final int TUNNEL_WIDTH = BLOCK_WIDTH * 7;
String[] data;

ArrayList<Rock> allRocks;
int nextRockType = 0;
int windIndex = 0;
Rock fallingRock;
boolean isFallCycle = true;

Block topmostBlockInPile = null;

void setup() {
  frameRate(1000);
  data = loadStrings("../../../data/Day17.txt");
  println(data[0].length());
  colorMode(HSB, 360, 100, 100);
  size(70, 1000);
  rectMode(CORNER);
  
  allRocks = new ArrayList<>();
}

void draw() {
  if (topmostBlockInPile != null && topmostBlockInPile.pos.y > 0) {
    background(169, 39, 57);
    drawGrid();
  }
  //println("Frame # " + frameCount);

  //  determine if time for a new rock to fall
  if (fallingRock == null) {
    int highestPoint = topmostBlockInPile == null ? FLOOR_HEIGHT : (int)topmostBlockInPile.pos.y;
    fallingRock = getNewRock(nextRockType, highestPoint);
    allRocks.add(fallingRock);
    if (allRocks.size() == 99) {
      println("Debug"); //<>//
    }
    if (allRocks.size() % 100 == 0) {
      println("Added rock # " + allRocks.size());
    }
    if (allRocks.size() == 2023) {
      println("This is top of tower: " + allRocks.get(2021).topmostBlock.pos);
    }
    nextRockType++;
    isFallCycle = false;
    
      //  draw all the rocks
    if (topmostBlockInPile != null && topmostBlockInPile.pos.y > 0) {
      for (Rock rock : allRocks) {
        rock.display();
      }
    }
    return;
  }
  
  if (fallingRock != null) {
    if (isFallCycle) {
      fallCycle();
      isFallCycle = false;
    } else {
      windCycle();
      isFallCycle = true;
    }
  }
  
  //  draw all the rocks
  if (topmostBlockInPile != null && topmostBlockInPile.pos.y > 0) {
    for (Rock rock : allRocks) {
      rock.display();
    }
  }
}

void fallCycle() {
  //println("Fall cycle");
  if (fallingRock.isOnFloor()) {
    fallingRock.isFalling = false;
    setTopmostBlockInPile();
    fallingRock = null;
    return;
  }
  for (int i = allRocks.size() - 2; i > -1; i--) {
    if (allRocks.get(i) == fallingRock) {
      continue;
    }
    if (fallingRock.collidesOtherRockDown(allRocks.get(i))) {
      fallingRock.isFalling = false;
      setTopmostBlockInPile();
      fallingRock = null;
      return;
    }
  }
  //for (Rock otherRock : allRocks) {
  //  if (otherRock == fallingRock) {
  //    continue;
  //  }
  //  if (fallingRock.collidesOtherRockDown(otherRock)) {
  //    fallingRock.isFalling = false;
  //    setTopmostBlockInPile();
  //    fallingRock = null;
  //    return;
  //  }
  //}
  fallingRock.fall();
}

void windCycle() {
  String wind = data[0].substring(windIndex, windIndex + 1);
  //println("wind cycle: " + wind);
  windIndex++;
  if (windIndex > data[0].length() - 1) {
    println("Starting over with the wind");
    windIndex = 0;
  }
  if (wind.equals(">") && fallingRock.isOnRightWall()) {
    return;
  }
  if (wind.equals("<") && fallingRock.isOnLeftWall()) {
    return;
  }
  for (int i = allRocks.size() - 2; i > -1; i--) {
    if (allRocks.get(i) == fallingRock) {
      continue;
    }
    if (wind.equals(">")) {
      if (fallingRock.collidesOtherRockRight(allRocks.get(i))) {
        return;
      }
    } else {
      if (fallingRock.collidesOtherRockLeft(allRocks.get(i))) {
        return;
      }
    }
  }

  //for (Rock otherRock : allRocks) {
  //  if (otherRock == fallingRock) {
  //    continue;
  //  }
  //  if (wind.equals(">")) {
  //    if (fallingRock.collidesOtherRockRight(otherRock)) {
  //      return;
  //    }
  //  } else {
  //    if (fallingRock.collidesOtherRockLeft(otherRock)) {
  //      return;
  //    }
  //  }
  //}
  fallingRock.applyJet(wind);
}

void setTopmostBlockInPile() {
    if (topmostBlockInPile == null) {
      topmostBlockInPile = fallingRock.topmostBlock;
      return;
    }
    
    if (fallingRock.topmostBlock.pos.y < topmostBlockInPile.pos.y) {
      topmostBlockInPile = fallingRock.topmostBlock;
    }
}

void drawGrid() {
  fill(0.5);
  for (int i = 10; i < width; i += BLOCK_WIDTH) {
    line(i, 0, i, height);
  }
  for (int j = 10; j < height; j += BLOCK_WIDTH) {
    line(0, j, width, j);
  }
}

Rock getNewRock(int type, int topY) {
  Rock newRock;
  if (type % 5 == 0) {
    newRock = new HorzLine(0, 0);
  } else if (type % 5 == 1) {
    newRock = new Cross(0, 0);
  } else if (type % 5 == 2) {
    newRock = new El(0, 0); 
  } else if (type % 5 == 3) {
    newRock = new VertLine(0, 0);
  } else if (type % 5 == 4) {
    newRock = new Square(0, 0);
  } else {
    println("Shouldn't happen");
    newRock = null;
    return null;
  }
  int newX = newRock.getStartingXPos();
  int newY = newRock.getStartingYPos(topY);
  newRock.updatePos(newX, newY);
  return newRock;
}
