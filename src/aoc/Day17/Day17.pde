Square squareRock = new Square(50, 50);

void setup() {
  size(500, 500);
  colorMode(HSB, 360, 100, 100);
  rectMode(CENTER);

}

void draw() {
  background(169, 39, 57);
  squareRock.display();
  if (!squareRock.isOnFloor()) {
    squareRock.fall();
  }
}
