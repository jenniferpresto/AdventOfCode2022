class Rock {
  PVector pos;
  color col;
  ArrayList<Block> blockList = new ArrayList<>();
  ArrayList<Block> leftBlocks = new ArrayList<>();
  ArrayList<Block> rightBlocks = new ArrayList<>();
  ArrayList<Block> topBlocks = new ArrayList<>();
  ArrayList<Block> bottomBlocks = new ArrayList<>();
  
  int blockWidth = BLOCK_WIDTH;
  
  Block rightmostBlock;
  Block leftmostBlock;
  Block bottomBlock;
  Block topBlock;
  
  Rock(float x, float y) {
    pos = new PVector(x, y);
    initializeBlocks();
    updatePos(x, y);
    setBlockExtremes();
  }
  
  void initializeBlocks() {}
  void updateBlockPos() {}
  
  void setBlockExtremes() {
    bottomBlock = blockList.get(0);
    rightmostBlock = blockList.get(0);
    leftmostBlock = blockList.get(0);
    topBlock = blockList.get(0);
    for (Block block : blockList) {
      if (block.pos.y > bottomBlock.pos.y) {
        bottomBlock = block;
      }
      if (block.pos.x > rightmostBlock.pos.x) {
        rightmostBlock = block;
      }
      if (block.pos.x < leftmostBlock.pos.x) {
        leftmostBlock = block;
      }
      if (block.pos.y < topBlock.pos.y) {
        topBlock = block;
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
      block.display(BLOCK_WIDTH);
    }
    //fill(0, 0, 0);
    //rect(pos.x, pos.y, blockWidth, blockWidth);
  }
  
  void fall() {
    updatePos(pos.x, pos.y + blockWidth);
  }
  
  void applyJet(String dir) {
    if (dir.equals(">")) {
      pos.x += BLOCK_WIDTH;
    } else if (dir.equals("<")) {
      pos.x -= BLOCK_WIDTH;
    }
  }
  
  boolean isOnFloor() {
    return bottomBlock.pos.y >= height - blockWidth;
  }
  
  boolean isOnLeftWall() {
    return leftmostBlock.pos.x <= 0;
  }
  
  boolean isOnRightWall() {
    return rightmostBlock.pos.x >= width - blockWidth;
  }
}

class Block {
  PVector pos = new PVector();
  void display(float blockWidth) {
    rect(pos.x, pos.y, blockWidth, blockWidth);
  }
 
}
