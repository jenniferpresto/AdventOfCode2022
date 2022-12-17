class Rock {
  PVector pos;
  color col;
  ArrayList<Block> blockList = new ArrayList<>(); 
  int blockWidth = 10;
  
  
  Rock(float x, float y) {
    pos = new PVector(x, y);
    initializeBlocks();
    updatePos(x, y);
  }
  
  void initializeBlocks() {}
  void updateBlockPos() {}
  
  void updatePos(float x, float y) {
    pos.set(x, y);
    updateBlockPos();
  }
  
  
  void display() {
    //noStroke();
    fill(col);
    for (Block block : blockList) {
      block.display(blockWidth);
    }
    fill(0, 0, 0);
    rect(pos.x, pos.y, blockWidth, blockWidth);
  }
  
  void fall() {
    updatePos(pos.x, pos.y + blockWidth);
  }
  
  boolean isOnFloor() {
    Block lowestBlock = blockList.get(0);
    Block rightmostBlock = blockList.get(0);
    Block leftmostBlock = blockList.get(0);
    for (Block block : blockList) {
      if (block.pos.y > lowestBlock.pos.y) {
        lowestBlock = block;
      }
      if (block.pos.x > rightmostBlock.pos.x) {
        rightmostBlock = block;
      }
      if (block.pos.x < leftmostBlock.pos.x) {
        leftmostBlock = block;
      }
    }
    return lowestBlock.pos.y >= height - blockWidth;
  }
}

class Block {
  PVector pos = new PVector();
  void display(float blockWidth) {
    rect(pos.x, pos.y, blockWidth, blockWidth);
  }
 
}

class Square extends Rock {
  Square(float x, float y) {
    super(x, y);
    col = color(0, 100, 100);
  }
  
  void initializeBlocks() {
    for (int i = 0; i < 4; i++) {
      blockList.add(new Block());
    }
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    blockList.get(0).pos.set(pos.x, pos.y);
    blockList.get(1).pos.set(pos.x + blockWidth, pos.y);
    blockList.get(2).pos.set(pos.x, pos.y + blockWidth);
    blockList.get(3).pos.set(pos.x + blockWidth, pos.y + blockWidth);
  }
  
  void display() {
    super.display();
    
  }
}

class Cross extends Rock {
  Cross(float x, float y) {
    super(x, y);
    col = color(72, 100, 100);
  }
 
  void initializeBlocks() {
    for (int i = 0; i < 5; i++) {
      blockList.add(new Block());
    }
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    blockList.get(0).pos.set(pos);
    blockList.get(1).pos.set(pos.x, pos.y - blockWidth);
    blockList.get(2).pos.set(pos.x - blockWidth, pos.y);
    blockList.get(3).pos.set(pos.x + blockWidth, pos.y);
    blockList.get(4).pos.set(pos.x, pos.y + blockWidth);
  }
  
  void display() {
    super.display();
    
  }
}

class HorzLine extends Rock {
  HorzLine(float x, float y) {
    super(x, y);
    col = color(144, 100, 100);
  }

  void initializeBlocks() {
    for (int i = 0; i < 4; i++) {
      blockList.add(new Block());
    }
  }
  
  void updateBlockPos() {
    super.updateBlockPos();
    blockList.get(0).pos.set(pos.x, pos.y);
    blockList.get(1).pos.set(pos.x + blockWidth, pos.y);
    blockList.get(2).pos.set(pos.x + blockWidth * 2, pos.y);
    blockList.get(3).pos.set(pos.x + blockWidth * 3, pos.y);
  }
  
  void display() {
    super.display();
    
  }
}


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
    blockList.get(0).pos.set(pos.x, pos.y);
    blockList.get(1).pos.set(pos.x, pos.y + blockWidth);
    blockList.get(2).pos.set(pos.x, pos.y + blockWidth * 2);
    blockList.get(3).pos.set(pos.x, pos.y + blockWidth * 3);
  }
  
  void display() {
    super.display();
    
  }
}

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
    
    blockList.get(0).pos.set(pos.x + blockWidth, pos.y - blockWidth);
    blockList.get(1).pos.set(pos.x + blockWidth, pos.y);
    blockList.get(2).pos.set(pos.x + blockWidth, pos.y + blockWidth);
    blockList.get(3).pos.set(pos.x, pos.y + blockWidth);
    blockList.get(4).pos.set(pos.x - blockWidth, pos.y + blockWidth);
  }
  
  void display() {
    super.display();
    
  }
}
