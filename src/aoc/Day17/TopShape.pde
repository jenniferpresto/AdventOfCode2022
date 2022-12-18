class TopShape {
  
  int[] colHeights = {0, 0, 0, 0, 0, 0, 0};
  String lastShape = "none";
  int windIndex = -1;

  @Override
  public String toString() {
    String output = "last shape: " + lastShape + ", wind index: " + windIndex + ":  ";
    for (int i = 0; i < 7; i++) {
      output += String.valueOf(colHeights[i]) + " ";
    }
    return output;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TopShape)) {
      return false;
    }
    final TopShape other = (TopShape)o;
    boolean isEqual = true;
    for (int i = 0; i < 7; i++) {
      if (this.colHeights[i] != other.colHeights[i]) {
        return false;
      }
    }
    return this.lastShape.equals(other.lastShape) && this.windIndex == other.windIndex;
  }
}
