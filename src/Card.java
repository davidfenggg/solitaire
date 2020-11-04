/**
 * This outlines what information a card should contain and what a card can do
 *
 * @author David Feng
 * @version November 2nd, 2017
 */
public class Card
{
  /**
   * Instance variable stores the suit of the card
   */
  public String suit;
  /**
   * Instance variable stores the rank of the card
   */
  public int rank;
  /**
   * Instance variable stores the face up/down status of the card
   */
  public boolean isFaceUp;

  /**
   * Constructs a Card object
   *
   * @param suit  the suit of the card created
   * @param num   the rank/number of the card created
   */
  public Card(String suit, int num)
  {
    this.suit = suit;
    rank = num;
    isFaceUp = false;
  }

  /**
   * Retrieves the suit of the card
   *
   * @return  the suit that this suit is a part of
   */
  public String getSuit()
  {
    return suit;
  }

  /**
   * Retrieves the rank of the card
   *
   * @return  the rank of the card
   */
  public int getRank()
  {
    return rank;
  }

  /**
   * Checks if the card is red
   *
   * @return  true  if the card is a diamond or the heart
   *          false if the card is a spade or a club
   */
  public boolean isRed()
  {
    if(getSuit().equals("d")||getSuit().equals("h"))
    {
      return true;
    }
    return false;
  }

  /**
   * Checks if the card is face up
   *
   * @return  true  if the card is face up
   *          false if the card is face down
   */
  public boolean isFaceUp()
  {
    return isFaceUp;
  }

  /**
   * Flips the card face up
   */
  public void turnUp()
  {
    isFaceUp = true;
  }

  /**
   * Flips the card face down
   */
  public void turnDown()
  {
    isFaceUp = false;
  }

  /**
   * Returns the string associated with a card in order to access the image
   *
   * @return  the file name of the card
   */
  public String getFileName()
  {
    String modifiedRank = "";
    if (!isFaceUp)
    {
      return "cards//back.gif";
    }
    if (getRank() == 1)
    {
      modifiedRank = "a";
    }
    else if (getRank() == 11)
    {
      modifiedRank = "j";
    }
    else if (getRank() == 12)
    {
      modifiedRank = "q";
    }
    else if (getRank() == 13)
    {
      modifiedRank = "k";
    }
    else if (getRank() == 10)
    {
      modifiedRank = "t";
    }
    else
    {
      modifiedRank = modifiedRank + getRank();
    }
    return "/Users/davidfeng/Desktop/Personal/Old CS Projects/SolitaireFeng/cards/"
        + modifiedRank + getSuit() + ".gif";
  }

}
