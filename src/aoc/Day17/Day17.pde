static final int BLOCK_WIDTH = 10;
static final int FLOOR_HEIGHT = 0;
static final int TUNNEL_WIDTH = BLOCK_WIDTH * 7;
String[] data;

ArrayList<Rock> allRocks;
int nextRockType = 0;
int windIndex = 0;
Rock fallingRock;
boolean isFallCycle = true;
boolean startedTestingCycles = false;
int numRocksInRepeatingCycle = 0;

Block topmostBlockInPile = null;

ArrayList<TopShape> allTopShapesSinceWindCycled;
ArrayList<TopShape> repeatingShapes;
int[] topBlockInEachColumn = new int[7];
HashMap<Long, Long> heightAfterEachRock;

void setup() {
  frameRate(1000);
  data = loadStrings("../../../data/Day17.txt");
  println(data[0].length());
  colorMode(HSB, 360, 100, 100);
  size(70, 1000);
  rectMode(CORNER);
  
  allRocks = new ArrayList<>();
  allTopShapesSinceWindCycled = new ArrayList<>();
  repeatingShapes = new ArrayList<>();
  heightAfterEachRock = new HashMap<>();

  for (int i = 0; i < 7; i++) {
    topBlockInEachColumn[i] = FLOOR_HEIGHT;
  }
}

void draw() {
  if (topmostBlockInPile != null && topmostBlockInPile.pos.y > 0) {
    background(169, 39, 57);
    drawGrid();
  }

  //  determine if time for a new rock to fall
  if (fallingRock == null) {    
    int highestPoint = topmostBlockInPile == null ? FLOOR_HEIGHT : (int)topmostBlockInPile.pos.y;
    fallingRock = getNewRock(nextRockType, highestPoint);
    allRocks.add(fallingRock);
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
  if (fallingRock.isOnFloor()) {
    fallingRock.isFalling = false;
    setTopmostBlockInPile();
    saveColumnHeights();
    if (startedTestingCycles) {
      testColumnHeights();
    }
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
      saveColumnHeights();
      if (startedTestingCycles) {
        testColumnHeights();
      }
      fallingRock = null;
      return;
    }
  }
  fallingRock.fall();
}

void windCycle() {
  String wind = data[0].substring(windIndex, windIndex + 1);
  //println("Wind!: " + wind);
  windIndex++;
  if (windIndex > data[0].length() - 1) {
    //println("Starting over with the wind");
    windIndex = 0;
    startedTestingCycles = true;
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

void saveColumnHeights() {
  for (Block block : fallingRock.blockList) {
    int col = (int)block.pos.x / 10;
    if (block.pos.y < topBlockInEachColumn[col]) {
      topBlockInEachColumn[col] = (int)block.pos.y;
    }
  }
  
}

void testColumnHeights() {
  //  make all of them relative to each other
  TopShape testShape = new TopShape();
  testShape.lastShape = fallingRock.name;
  testShape.windIndex = windIndex;
  int baseY = topBlockInEachColumn[0];
  for (int i = 1; i < 7; i++) {
    testShape.colHeights[i] = topBlockInEachColumn[i] - baseY;
  }
  
  //if (windIndex == 14 && fallingRock.name.equals("el")) {
  if (windIndex == 4 && fallingRock.name.equals("vertLine")) {
    println("Testing test ! Frame # " + frameCount + ", numRocks; " + allRocks.size());
    println(testShape);
  }
  
  for (int i = 0; i < allTopShapesSinceWindCycled.size(); i++) {
    if (allTopShapesSinceWindCycled.get(i).equals(testShape)) {
      //println("We have a match!!  windCycle: " + windIndex + ": " + testShape);
      //println("how big is cycle? " + allTopShapesSinceWindCycled.size() + ", we're at " + i);
      for (int j = 0; j < repeatingShapes.size(); j++) {
        if (repeatingShapes.get(j).equals(testShape)) {
          if (allRocks.get(allRocks.size() -1) != fallingRock) {
            println("Doing something wrong"); //<>//
          }
          numRocksInRepeatingCycle = repeatingShapes.size();
          long thisRockNum = (long)allRocks.size();
          long numRocksToGo = 1000000000000L - thisRockNum;
          if (numRocksToGo % numRocksInRepeatingCycle == 0) {
            println("we're whole cycles away from a trillion: " + allRocks.size());
            println("height now is: " + fallingRock.topmostBlock.pos.y);
            long numCyclesToGetThere = numRocksToGo / numRocksInRepeatingCycle;
            println("We have " + numCyclesToGetThere + " to go");
            long lastMatchingCycle = thisRockNum - numRocksInRepeatingCycle;
            long heightRightNow = (long)fallingRock.topmostBlock.pos.y;
            long heightLastTimeWeWereHere = heightAfterEachRock.get(lastMatchingCycle);
            println("Last time we were here: " + heightLastTimeWeWereHere + ", now it's " + heightRightNow);
            long heightDifference = heightRightNow - heightLastTimeWeWereHere;
            println("Each cycle adds an additional " + heightDifference + " to the pile");
            long eventualAdditionalHeight = heightDifference * numCyclesToGetThere;
            println("Eventual additional height is: " + eventualAdditionalHeight); //<>//
            long consistentHeight = eventualAdditionalHeight + heightRightNow;
            println("So this number should be consistent: " + consistentHeight);
            consistentHeight -= FLOOR_HEIGHT;
            println("******* Answer: " + (consistentHeight / BLOCK_WIDTH * -1) + " ************");
            heightAfterEachRock.put((long)allRocks.size(), (long)fallingRock.topmostBlock.pos.y);
          }
          return;
        }
      }
      heightAfterEachRock.put((long)allRocks.size(), (long)fallingRock.topmostBlock.pos.y);
      repeatingShapes.add(testShape);
      return;
    }
  }
  allTopShapesSinceWindCycled.add(testShape);
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
