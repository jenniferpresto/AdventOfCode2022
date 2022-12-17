class Square extends Rock {
  Square(float x, float y) {
    super(x, y);
    name = "square";
    col = color(0, 100, 100);
  }
  
  void initializeBlocks() {
    for (int i = 0; i < 4; i++) {
      blockList.add(new Block());
    }
    updateBlockPos();
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    blockList.get(0).pos.set(pos.x, pos.y); // top left
    blockList.get(1).pos.set(pos.x + BLOCK_WIDTH, pos.y); // top right
    blockList.get(2).pos.set(pos.x, pos.y + BLOCK_WIDTH); // bottom left
    blockList.get(3).pos.set(pos.x + BLOCK_WIDTH, pos.y + BLOCK_WIDTH); // bottom right
  }
  
  void setBlockExtremes() {
    super.setBlockExtremes();
    rightBlocks.add(blockList.get(1));
    rightBlocks.add(blockList.get(3));
    topBlocks.add(blockList.get(0));
    topBlocks.add(blockList.get(1));
    leftBlocks.add(blockList.get(0));
    leftBlocks.add(blockList.get(2));
    bottomBlocks.add(blockList.get(2));
    bottomBlocks.add(blockList.get(3));
  }
  
  void display() {
    super.display();
    
  }
}
