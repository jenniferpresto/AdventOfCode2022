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
  
  Rock(float x, float y) {
    pos = new PVector(x, y);
    initializeBlocks();
    updatePos(x, y);
    setBlockExtremes();
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
  
  void updatePos(float x, float y) {
    pos.set(x, y);
    updateBlockPos();
  }
  
  void display() {
    //noStroke();
    fill(col);
    for (Block block : blockList) {
      block.display();
    }
    //fill(0, 0, 0);
    //rect(pos.x, pos.y, blockWidth, blockWidth);
  }
  
  void fall() {
    
    updatePos(pos.x, pos.y + BLOCK_WIDTH);
  }
  
  void applyJet(String dir) {
    if (dir.equals(">")) {
      pos.x += BLOCK_WIDTH;
    } else if (dir.equals("<")) {
      pos.x -= BLOCK_WIDTH;
    }
  }
  
  boolean collidesOtherRockDown(Rock other) {
    for (Block bottomBlock : this.bottomBlocks) {
      for (Block topBlock : other.topBlocks) {
        if (bottomBlock.pos.x == topBlock.pos.x
            && bottomBlock.pos.y >= topBlock.pos.y - BLOCK_WIDTH) {
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
            && rightBlock.pos.x >= leftBlock.pos.x - BLOCK_WIDTH) {
          return true;
        }
      }
    }
    return false;
  }
  
  boolean collidesOtherRockLeft(Rock other) {
    for (Block rightBlock : this.rightBlocks) {
      for (Block leftBlock : other.leftBlocks) {
        if (rightBlock.pos.y == leftBlock.pos.y
            && rightBlock.pos.x >= leftBlock.pos.x - BLOCK_WIDTH) {
          return true;
        }
      }
    }
    return false;
  }
  
  boolean isOnFloor() {
    return bottommostBlock.pos.y >= height - BLOCK_WIDTH;
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
