Square squareRock = new Square(50, 50);
Cross crossRock = new Cross(100, 20);
Line lineRock = new Line(300, 10);
El elRock = new El(400,10);

void setup() {
  println("Setting up");
  size(500, 500);
  colorMode(HSB, 360, 100, 100);
  rectMode(CORNER);
  println("Dnoe setting up");

}

void draw() {
  background(169, 39, 57);
  squareRock.display();
  crossRock.display();
  lineRock.display();
  elRock.display();
  if (!squareRock.isOnFloor()) {
    squareRock.fall();
  }
  if (!crossRock.isOnFloor()) {
    crossRock.fall();
  }
  if (!lineRock.isOnFloor()) {
    lineRock.fall();
  }
  if (!elRock.isOnFloor()) {
    elRock.fall();
  }
}
