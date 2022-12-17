
class El extends Rock {
  El(float x, float y) {
    super(x, y);
    col = color(288, 100, 100);
  }

  void initializeBlocks() {
    for (int i = 0; i < 5; i++) {
      blockList.add(new Block());
    }
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    
    blockList.get(0).pos.set(pos.x + blockWidth, pos.y - blockWidth); // top right
    blockList.get(1).pos.set(pos.x + blockWidth, pos.y); // second from top
    blockList.get(2).pos.set(pos.x + blockWidth, pos.y + blockWidth); // right corner
    blockList.get(3).pos.set(pos.x, pos.y + blockWidth); // second from left 
    blockList.get(4).pos.set(pos.x - blockWidth, pos.y + blockWidth); // bottom left
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

  
  void display() {
    super.display();
    
  }
}
