static final int BLOCK_WIDTH = 10;
final int TUNNEL_WIDTH = BLOCK_WIDTH * 7;
String[] data;

ArrayList<Rock> allRocks;
int nextRockType = 0;
int windIndex = 0;
Rock fallingRock;
boolean isFallCycle = true;

void setup() {
  data = loadStrings("../../../testData/Day17.txt");
  colorMode(HSB, 360, 100, 100);
  size(70, 500);
  rectMode(CORNER);
  
  allRocks = new ArrayList<>();
}

void draw() {
  background(169, 39, 57);
  
  //  determine if time for a new rock to fall
  if (fallingRock == null) {
    fallingRock = getNewRock(nextRockType);
    allRocks.add(fallingRock);
    nextRockType++;
    isFallCycle = true;
    if (fallingRock.name.equals("vertLine") && allRocks.size() > 3) {
      int jennifer = 9;
    }
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
  for (Rock rock : allRocks) {
    rock.display();
  }
}

void fallCycle() {
  if (fallingRock.isOnFloor()) {
    fallingRock.isFalling = false;
    fallingRock = null;
    return;
  }
  for (Rock otherRock : allRocks) {
    if (otherRock == fallingRock) {
      continue;
    }
    if (fallingRock.collidesOtherRockDown(otherRock)) {
      fallingRock.isFalling = false;
      fallingRock = null;
      return;
    }
  }
  fallingRock.fall();
}

void windCycle() {
  if (fallingRock.isOnFloor()) {
    fallingRock.isFalling = false;
    fallingRock = null;
    return;
  }
  for (Rock otherRock : allRocks) {
    if (otherRock == fallingRock) {
      continue;
    }
    if (fallingRock.collidesOtherRockDown(otherRock)) {
      fallingRock.isFalling = false;
      fallingRock = null;
      return;
    }
  }
  String wind = data[0].substring(windIndex, windIndex + 1);
  windIndex++;
  if (windIndex > data[0].length() - 1) {
    windIndex = 0;
  }
  if (wind.equals(">") && fallingRock.isOnRightWall()) {
    return;
  }
  if (wind.equals("<") && fallingRock.isOnLeftWall()) {
    return;
  }
  for (Rock otherRock : allRocks) {
    if (otherRock == fallingRock) {
      continue;
    }
    if (wind.equals(">")) {
      if (fallingRock.collidesOtherRockRight(otherRock)) {
        return;
      }
    } else {
      if (fallingRock.collidesOtherRockLeft(otherRock)) {
        return;
      }
    }
  }
  fallingRock.applyJet(wind);
}

Rock getNewRock(int type) {
  if (type % 5 == 0) {
    return new HorzLine(BLOCK_WIDTH * 2, 0);
  } else if (type % 5 == 1) {
    return new Cross (BLOCK_WIDTH * 3, 0);
  } else if (type % 5 == 2) {
    return new El(BLOCK_WIDTH * 3, 0); 
  } else if (type % 5 == 3) {
    return new VertLine(BLOCK_WIDTH * 2, 0);
  } else if (type % 5 == 4) {
    return new Square(BLOCK_WIDTH * 2, 0);
  }
  return null;
}
