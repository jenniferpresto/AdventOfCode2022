
class El extends Rock {
  El(int x, int y) {
    super(x, y);
    name = "el";
    col = color(288, 100, 100);
  }

  void initializeBlocks() {
    for (int i = 0; i < 5; i++) {
      blockList.add(new Block());
    }
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    
    blockList.get(0).pos.set(pos.x + BLOCK_WIDTH, pos.y - BLOCK_WIDTH); // top right
    blockList.get(1).pos.set(pos.x + BLOCK_WIDTH, pos.y); // second from top
    blockList.get(2).pos.set(pos.x + BLOCK_WIDTH, pos.y + BLOCK_WIDTH); // right corner
    blockList.get(3).pos.set(pos.x, pos.y + BLOCK_WIDTH); // second from left 
    blockList.get(4).pos.set(pos.x - BLOCK_WIDTH, pos.y + BLOCK_WIDTH); // bottom left
  }
  
  void setBlockExtremes() {
    super.setBlockExtremes();
    topBlocks.add(blockList.get(0));
    topBlocks.add(blockList.get(3));
    topBlocks.add(blockList.get(4));
    leftBlocks.add(blockList.get(0));
    leftBlocks.add(blockList.get(1));
    leftBlocks.add(blockList.get(4));
    rightBlocks.add(blockList.get(0));
    rightBlocks.add(blockList.get(1));
    rightBlocks.add(blockList.get(2));
    bottomBlocks.add(blockList.get(2));
    bottomBlocks.add(blockList.get(3));
    bottomBlocks.add(blockList.get(4));
  }
}
