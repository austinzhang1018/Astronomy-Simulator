public class Location
{
  private long row;
  private long col;
  
  public Location(long r, long c)
  {
    row = r;
    col = c;
  }
  
  public long getX()
  {
    return row;
  }
  
  public long getY()
  {
    return col;
  }
  
  public boolean equals(Location otherLoc)
  {
    return row == otherLoc.getX() && col == otherLoc.getY();
  }
  
  public String toString()
  {
    return "(" + row + ", " + col + ")";
  }
}