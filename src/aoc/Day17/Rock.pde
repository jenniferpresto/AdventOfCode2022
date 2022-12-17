class Rock {
  PVector position;
  color col;
  Block[] blocks = new Block[4];
  int blockWidth = 10;
  
  
  Rock(float x, float y) {
    position = new PVector(x, y);
    for (int i = 0; i < 4; i++) {
      blocks[i] = new Block();
    }
  }
  
  void updatePos(float x, float y) {
    position.set(x, y);
  }
  
  void display() {
    //noStroke();
    fill(col);
    for (int i = 0; i < 4; i++) {
      blocks[i].display(blockWidth);
    }
  }
  
  void fall() {
    updatePos(position.x, position.y + blockWidth);
  }
  
  boolean isOnFloor() {
    Block lowestBlock = blocks[0];
    for (Block block : blocks) {
      if (block.position.y > lowestBlock.position.y) {
        lowestBlock = block;
      }
    }
    println("Lowest block pos: " + lowestBlock.position);
    return lowestBlock.position.y >= height - blockWidth/2;
  }
}

class Block {
  PVector position = new PVector();
  void display(float blockWidth) {
    rect(position.x, position.y, blockWidth, blockWidth);
  }
  
  boolean doesCollide(float x, float y) {
    return false;
  }
  
}

class Square extends Rock {
  Square(float x, float y) {
    super(x, y);
    col = color(0, 100, 100);
  }
  
  void updatePos(float x, float y) {
    super.updatePos(x, y);
    blocks[0].position.set(position.x - blockWidth/2, position.y - blockWidth/2);
    blocks[1].position.set(position.x + blockWidth/2, position.y - blockWidth/2);
    blocks[2].position.set(position.x - blockWidth/2, position.y + blockWidth/2);
    blocks[3].position.set(position.x + blockWidth/2, position.y + blockWidth/2);
  }
  
  void display() {
    super.display();
    
  }
}

class Cross extends Rock {
  Cross(float x, float y) {
    super(x, y);
    col = color(90, 100, 100);
  }
  
  void updatePos(float x, float y) {
    super.updatePos(x, y);
  }
  
  void display() {
    super.display();
    
  }
}

class Line extends Rock {
  Line(float x, float y) {
    super(x, y);
    col = color(180, 100, 100);
  }
  
  void updatePos(float x, float y) {
    super.updatePos(x, y);
  }
  
  void display() {
    super.display();
    
  }
}

class El extends Rock {
  El(float x, float y) {
    super(x, y);
    col = color(270, 100, 100);
  }
  
  void updatePos(float x, float y) {
    super.updatePos(x, y);
  }
  
  void display() {
    super.display();
    
  }
}
