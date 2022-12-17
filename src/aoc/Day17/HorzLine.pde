class HorzLine extends Rock {
  HorzLine(float x, float y) {
    super(x, y);
    name = "horzLine";
    col = color(144, 100, 100);
  }

  void initializeBlocks() {
    for (int i = 0; i < 4; i++) {
      blockList.add(new Block());
    }
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    blockList.get(0).pos.set(pos.x, pos.y); // left
    blockList.get(1).pos.set(pos.x + BLOCK_WIDTH, pos.y); // second
    blockList.get(2).pos.set(pos.x + BLOCK_WIDTH * 2, pos.y); // third
    blockList.get(3).pos.set(pos.x + BLOCK_WIDTH * 3, pos.y); // right
  }

  void setBlockExtremes() {
    super.setBlockExtremes();
    topBlocks.add(blockList.get(0));
    topBlocks.add(blockList.get(1));
    topBlocks.add(blockList.get(2));
    topBlocks.add(blockList.get(3));
    leftBlocks.add(blockList.get(0));
    bottomBlocks.add(blockList.get(0));
    bottomBlocks.add(blockList.get(1));
    bottomBlocks.add(blockList.get(2));
    bottomBlocks.add(blockList.get(3));
    rightBlocks.add(blockList.get(3));
  }
  
  void display() {
    super.display();
    
  }
}
