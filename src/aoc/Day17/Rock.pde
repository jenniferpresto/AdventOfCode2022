class Rock {
  PVector pos;
  String name = "";
  color col;
  ArrayList<Block> blockList = new ArrayList<>();
  ArrayList<Block> leftBlocks = new ArrayList<>();
  ArrayList<Block> rightBlocks = new ArrayList<>();
  ArrayList<Block> topBlocks = new ArrayList<>();
  ArrayList<Block> bottomBlocks = new ArrayList<>();
    
  Block rightmostBlock;
  Block leftmostBlock;
  Block bottommostBlock;
  Block topmostBlock;
  
  boolean isFalling = true;
  
  Rock(int x, int y) {
    pos = new PVector(x, y);
    initializeBlocks();
    updatePos(x, y);
    setBlockExtremes();
  }
  
  int getStartingXPos() {
    int additionalWidth = (int)pos.x - (int)leftmostBlock.pos.x; 
    return BLOCK_WIDTH * 2 + additionalWidth;
  }
  
  int getStartingYPos(int highestPoint) {
    int additionalHeight = (int)bottommostBlock.pos.y + BLOCK_WIDTH - (int)pos.y;
    return highestPoint - (BLOCK_WIDTH * 3) - additionalHeight;
  }
  
  void initializeBlocks() {}
  void updateBlockPos() {}
  
  void setBlockExtremes() {
    bottommostBlock = blockList.get(0);
    rightmostBlock = blockList.get(0);
    leftmostBlock = blockList.get(0);
    topmostBlock = blockList.get(0);
    for (Block block : blockList) {
      if (block.pos.y > bottommostBlock.pos.y) {
        bottommostBlock = block;
      }
      if (block.pos.x > rightmostBlock.pos.x) {
        rightmostBlock = block;
      }
      if (block.pos.x < leftmostBlock.pos.x) {
        leftmostBlock = block;
      }
      if (block.pos.y < topmostBlock.pos.y) {
        topmostBlock = block;
      }
    }
  }
  
  void updatePos(int x, int y) {
    pos.set(x, y);
    updateBlockPos();
  }
  
  void display() {
    //noStroke();
    stroke(0);
    fill(col);
    for (Block block : blockList) {
      block.display();
    }
    //if (!isFalling) {
    //  fill(0, 0, 0);
    //  rect(pos.x, pos.y, BLOCK_WIDTH, BLOCK_WIDTH);
    //}
  }
  
  void fall() {
    updatePos((int)pos.x, (int)pos.y + BLOCK_WIDTH);
  }
  
  void applyJet(String dir) {
    if (dir.equals(">")) {
      updatePos((int)pos.x + BLOCK_WIDTH, (int)pos.y);
    } else if (dir.equals("<")) {
      updatePos((int)pos.x - BLOCK_WIDTH, (int)pos.y);
    }
  }
  
  boolean collidesOtherRockDown(Rock other) {
    //println("Testing down: " + this.name + ", " + other.name);
    
    for (Block bottomBlock : this.bottomBlocks) {
      for (Block topBlock : other.topBlocks) {
        if (bottomBlock.pos.x == topBlock.pos.x
            && bottomBlock.pos.y >= topBlock.pos.y - BLOCK_WIDTH
            && bottomBlock.pos.y - topBlock.pos.y < BLOCK_WIDTH - 1) {
          return true;
        }
      }
    }
    return false;
  }
  
  boolean collidesOtherRockRight(Rock other) {
    for (Block rightBlock : this.rightBlocks) {
      for (Block leftBlock : other.leftBlocks) {
        if (rightBlock.pos.y == leftBlock.pos.y
            && rightBlock.pos.x >= leftBlock.pos.x - BLOCK_WIDTH
            && rightBlock.pos.x - leftBlock.pos.x < BLOCK_WIDTH - 1) {
          return true;
        }
      }
    }
    return false;
  }
  
  boolean collidesOtherRockLeft(Rock other) {
    for (Block leftBlock : this.leftBlocks) {
      for (Block rightBlock : other.rightBlocks) {
        if (leftBlock.pos.y == rightBlock.pos.y
            && leftBlock.pos.x <= rightBlock.pos.x + BLOCK_WIDTH
            && rightBlock.pos.x - leftBlock.pos.x < BLOCK_WIDTH - 1) {
          return true;
        }
      }
    }
    return false;
  }
  
  boolean isOnFloor() {
    return bottommostBlock.pos.y >= FLOOR_HEIGHT - BLOCK_WIDTH;
  }
  
  boolean isOnLeftWall() {
    return leftmostBlock.pos.x <= 0;
  }
  
  boolean isOnRightWall() {
    return rightmostBlock.pos.x >= width - BLOCK_WIDTH;
  }
}

class Block {
  PVector pos = new PVector();
  
  void display() {
    rect(pos.x, pos.y, BLOCK_WIDTH, BLOCK_WIDTH);
  }
}
