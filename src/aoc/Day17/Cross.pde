class Cross extends Rock {
  Cross(int x, int y) {
    super(x, y);
    name = "cross";
    col = color(72, 100, 100);
  }
 
  void initializeBlocks() {
    for (int i = 0; i < 5; i++) {
      blockList.add(new Block());
    }
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    blockList.get(0).pos.set(pos); // middle
    blockList.get(1).pos.set(pos.x, pos.y - BLOCK_WIDTH); // top
    blockList.get(2).pos.set(pos.x - BLOCK_WIDTH, pos.y); // left
    blockList.get(3).pos.set(pos.x + BLOCK_WIDTH, pos.y); // right
    blockList.get(4).pos.set(pos.x, pos.y + BLOCK_WIDTH); // bottom
  }
  
  void setBlockExtremes() {
    super.setBlockExtremes();
    topBlocks.add(blockList.get(1));
    topBlocks.add(blockList.get(2));
    topBlocks.add(blockList.get(3));
    leftBlocks.add(blockList.get(1));
    leftBlocks.add(blockList.get(2));
    leftBlocks.add(blockList.get(4));
    rightBlocks.add(blockList.get(1));
    rightBlocks.add(blockList.get(3));
    rightBlocks.add(blockList.get(4));
    bottomBlocks.add(blockList.get(2));
    bottomBlocks.add(blockList.get(3));
    bottomBlocks.add(blockList.get(4));
  }
}
