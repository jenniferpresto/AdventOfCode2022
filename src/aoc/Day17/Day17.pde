Square squareRock;
Cross crossRock;
HorzLine horzRock;
VertLine vertRock;
El elRock;

void setup() {
  String[] data = loadStrings("../../../testData/Day17.txt");
  for (String line : data) {
  }
  colorMode(HSB, 360, 100, 100);
  size(500, 500);
  rectMode(CORNER);
  squareRock = new Square(50, 50);
  crossRock = new Cross(100, 20);
  horzRock = new HorzLine(300, 10);
  vertRock = new VertLine(350, 10);
  elRock = new El(400,10);
}

void draw() {
  background(169, 39, 57);
  squareRock.display();
  crossRock.display();
  horzRock.display();
  vertRock.display();
  elRock.display();
  if (!squareRock.isOnFloor()) {
    squareRock.fall();
  }
  if (!crossRock.isOnFloor()) {
    crossRock.fall();
  }
  if (!horzRock.isOnFloor()) {
    horzRock.fall();
  }
  if (!vertRock.isOnFloor()) {
    vertRock.fall();
  }
  if (!elRock.isOnFloor()) {
    elRock.fall();
  }
}
