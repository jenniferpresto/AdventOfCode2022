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
    println(name + " falling one");
    updatePos(pos.x, pos.y + BLOCK_WIDTH);
  }
  
  void applyJet(String dir) {
    println(name + " apply jet " + dir);
    if (dir.equals(">")) {
      updatePos(pos.x + BLOCK_WIDTH, pos.y);
    } else if (dir.equals("<")) {
      updatePos(pos.x -= BLOCK_WIDTH, pos.y);
    }
  }
  
  boolean collidesOtherRockDown(Rock other) {
    //println("Testing down: " + this.name + ", " + other.name);
    
    for (Block bottomBlock : this.bottomBlocks) {
      for (Block topBlock : other.topBlocks) {
        if (bottomBlock.pos.x == topBlock.pos.x
            && bottomBlock.pos.y >= topBlock.pos.y - BLOCK_WIDTH) {
              println("hit: " + this.name + " at "  + this.pos + " lands on " + other.name + " at " + other.pos );
              println("Blocks that hit: bot " + bottomBlock.pos + ", top: " + topBlock.pos);
          return true;
        }
      }
    }
    return false;
  }
  
  boolean collidesOtherRockRight(Rock other) {
    //println("Testing right: " + this.name + ", " + other.name);
    for (Block rightBlock : this.rightBlocks) {
      for (Block leftBlock : other.leftBlocks) {
        if (rightBlock.pos.y == leftBlock.pos.y
            && rightBlock.pos.x >= leftBlock.pos.x - BLOCK_WIDTH) {
              println("hit: " + this.name + " at "  + this.pos + " collides right on " + other.name + " at " + other.pos );
              println("Blocks that hit: right " + rightBlock.pos + ", left: " + leftBlock.pos);
          return true;
        }
      }
    }
    return false;
  }
  
  boolean collidesOtherRockLeft(Rock other) {
    //println("Testing left: " + this.name + ", " + other.name);
    for (Block leftBlock : this.leftBlocks) {
      for (Block rightBlock : other.rightBlocks) {
        if (leftBlock.pos.y == rightBlock.pos.y
            && leftBlock.pos.x <= rightBlock.pos.x + BLOCK_WIDTH) {
              println("hit: " + this.name + " at "  + this.pos + " collides left on " + other.name + " at " + other.pos );
              println("Blocks that hit: left " + leftBlock.pos + ", right: " + rightBlock.pos);
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
