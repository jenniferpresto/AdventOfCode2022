Square squareRock;
Cross crossRock;
HorzLine horzRock;
VertLine vertRock;
El elRock;
ArrayList<Rock> allRocks;
int nextRockType = 0;
static final int BLOCK_WIDTH = 10;
final int TUNNEL_WIDTH = BLOCK_WIDTH * 7;
Rock fallingRock;

void setup() {
  String[] data = loadStrings("../../../testData/Day17.txt");
  for (String line : data) {
  }
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
  }
  
  if (fallingRock != null) {
    if (fallingRock.isOnFloor()) {
      fallingRock.isFalling = false;
      fallingRock = null;
    }
    if (fallingRock != null) {
      for (Rock otherRock : allRocks) {
        if (otherRock == fallingRock) {
          continue;
        }
        if (fallingRock.collidesOtherRockDown(otherRock)) {
          fallingRock.isFalling = false;
          fallingRock = null;
          break;
        }
      }
    }
    if (fallingRock != null) {
      fallingRock.fall();
    }
  }
  
  //  draw all the rocks
  for (Rock rock : allRocks) {
    rock.display();
  }
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
