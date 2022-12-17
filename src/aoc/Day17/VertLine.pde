class VertLine extends Rock {
  VertLine(float x, float y) {
    super(x, y);
    col = color(216, 100, 100);
  }

  void initializeBlocks() {
    for (int i = 0; i < 4; i++) {
      blockList.add(new Block());
    }
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    blockList.get(0).pos.set(pos.x, pos.y); // top
    blockList.get(1).pos.set(pos.x, pos.y + BLOCK_WIDTH); // second
    blockList.get(2).pos.set(pos.x, pos.y + BLOCK_WIDTH * 2); // third
    blockList.get(3).pos.set(pos.x, pos.y + BLOCK_WIDTH * 3); // bottom
  }

  void setBlockExtremes() {
    super.setBlockExtremes();
    rightBlocks.add(blockList.get(0));
    rightBlocks.add(blockList.get(1));
    rightBlocks.add(blockList.get(2));
    rightBlocks.add(blockList.get(3));
    topBlocks.add(blockList.get(0));
    leftBlocks.add(blockList.get(0));
    leftBlocks.add(blockList.get(1));
    leftBlocks.add(blockList.get(2));
    leftBlocks.add(blockList.get(3));
    bottomBlocks.add(blockList.get(3));
  }
  

  void display() {
    super.display();
    
  }
}
