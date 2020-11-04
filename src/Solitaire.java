import java.util.ArrayList;
import java.util.Stack;

/**
 * This class creates a game of solitaire to play
 *
 * @author David Feng
 *
 * @version   09 November 2017
 *
 */
public class Solitaire
{
  /**
   * Main method that the game runs -- creates a new Solitaire game
   *
   * @param args  the text of the program
   */
  public static void main(String[] args)
  {
    new Solitaire();
  }

  /**
   * Instance variables hold the stacks and piles of cards used
   * in the game, including the stock, waste, and foundations
   */
  private Stack<Card> stock;
  private Stack<Card> waste;
  private Stack<Card>[] foundations;
  private Stack<Card>[] piles;
  private SolitaireDisplay display;
  private boolean toggle;

  /**
   * Constructs a Solitaire object
   */
  public Solitaire()
  {
    foundations = new Stack[4];
    for (int i=0; i<foundations.length; i++)
    {
      foundations[i] = new Stack<Card>();
    }
    piles = new Stack[7];
    for (int i=0; i<piles.length; i++)
    {
      piles[i] = new Stack<Card>();
    }
    stock=new Stack<Card>();
    waste=new Stack<Card>();
    display = new SolitaireDisplay(this);
    toggle = false;
    createStock();
    deal();
  }

  /**
   * This method gets the first Card in the stock pile
   *
   * @return  the card on top of the stock
   *          or null if the stock is empty
   */
  public Card getStockCard()
  {
    if(!stock.isEmpty())
    {
      return stock.peek();
    }
    else
    {
      return null;
    }
  }

  /**
   * This method returns the first card on the waste pile
   *
   * @return  returns the card on top of the stock,
   *          or null if the stock is empty
   */
  public Card getWasteCard()
  {
    if(!waste.isEmpty())
    {
      return waste.peek();
    }
    else
    {
      return null;
    }
  }

  /**
   * This method gets the card from the foundation as specified
   * by the index
   *
   * @param index the index of the foundation to get the card from
   *
   * @precondition  0 <= index < 4
   *
   * @postcondition: returns the card on top of the given foundation,
   *                 or null if the foundation is empty
   *
   * @return  the top card of the foundation
   *          null if the foundation is empty
   */
  public Card getFoundationCard(int index)
  {
    if(!(foundations[index].isEmpty()))
    {
      return foundations[index].peek();
    }
    else
    {
      return null;
    }
  }

  /**
   * Gets the stack of cards on the given pile (as specified by index)
   *
   * @param index   the index of the pile to get from
   *
   * @precondition  0 <= index < 7
   * @postcondition returns a reference to the given pile
   *
   * @return  the stack of cards at index
   */
  public Stack<Card> getPile(int index)
  {
    if(piles != null)
    {
      return piles[index];
    }
    else
    {
      return null;
    }
  }

  /**
   * This method determines what happens when the stock is clicked
   */
  public void stockClicked()
  {
    if(display.isPileSelected() || display.isWasteSelected())
    {

    }
    else if(!stock.isEmpty())
    {
      dealThreeCards();
    }
    else
    {
      resetStock();
    }
    System.out.println("stock clicked");
  }

  /**
   * This method determines what happens when a stock is clicked -- one can
   * move the card to smoky yellow and redwood
   */
  public void wasteClicked()
  {
    if(!waste.isEmpty() && !display.isWasteSelected() && !display.isPileSelected())
    {
      display.selectWaste();
    }
    else if(display.isWasteSelected())
    {
      display.unselect();
    }
    System.out.println("Waste clicked");
  }

  /**
   * This method determines what happens after a specified foundation is clicked
   *
   * @param index the index of the foundation that is clicked
   *
   * @precondition  0 <= index < 4
   *
   * @postcondition a card is added to the foundation if possible
   */
  public void foundationClicked(int index)
  {
    if(display.isWasteSelected())
    {
      if(canAddToFoundation(waste.peek(), index))
      {
        Card card = waste.pop();
        foundations[index].push(card);
        int rank = card.getRank();
        display.addScore(rank);
        display.unselect();
      }
    }
    else if(display.isPileSelected())
    {
      if(canAddToFoundation(piles[display.selectedPile()].peek(), index))
      {
        Card card = piles[display.selectedPile()].pop();
        foundations[index].push(card);
        int rank = card.getRank();
        display.addScore(rank);
      }
      display.unselect();
    }
    System.out.println("foundation #" + index + " clicked");
  }

  /**
   * This determines whether or not the partial move cheat is on and what to do
   * if one is on
   */
  public void partialCheatClicked()
  {
    if(!toggle)
    {
      toggle = true;
      System.out.println("Toggle is on");
    }
    else
    {
      toggle = false;
      System.out.println("Toggle is off");
    }
  }

  public boolean getToggle()
  {
    return toggle;
  }

  /**
   * This method determines what to do if the new game button is clicked
   */
  public void newGameClicked()
  {
    stock = null;
    waste = null;
    piles = null;
    foundations = null;
    foundations = new Stack[4];
    for (int i=0; i<foundations.length; i++)
    {
      foundations[i] = new Stack<Card>();
    }
    piles = new Stack[7];
    for (int i=0; i<piles.length; i++)
    {
      piles[i] = new Stack<Card>();
    }
    stock=new Stack<Card>();
    waste=new Stack<Card>();
    createStock();
    deal();
    System.out.println("New Game!");
  }

  /**
   * This method determines what happened if a specified pile is clicked
   *
   * @param index the index of the pile that is to be selected
   *
   * @precondition 0 <= index < 7
   */
  public void pileClicked(int index)
  {
    if(!display.isWasteSelected() && !display.isPileSelected() && !piles[index].isEmpty())
    {
      if(piles[index].peek().isFaceUp())
      {
        display.selectPile(index);
      }
      else
      {
        piles[index].peek().turnUp();
      }
    }
    else if(display.isWasteSelected())
    {
      if(!waste.isEmpty() && canAddToPile(waste.peek(), index))
      {
        piles[index].push(waste.pop()).turnUp();
      }
      else
      {
        display.unselect();
      }
    }
    else if (display.isPileSelected() && toggle)
    {
      Stack<Card> newStack = removeFaceUpCards(display.selectedPile());
      while (!newStack.isEmpty() && !canAddToPile(newStack.peek(), index))
      {
        piles[display.selectedPile()].push(newStack.pop());
      }
      addToPile(newStack, index);
      display.unselect();
    }
    else if(display.selectedPile()==index)
    {
      display.unselect();
    }
    else if(display.selectedPile()!=-1 && display.selectedPile()!=index)
    {
      Stack<Card> s = removeFaceUpCards(display.selectedPile());
      if(canAddToPile(s.peek(), index))
      {
        addToPile(s, index);
        display.unselect();
      }
      else
      {
        addToPile(s, display.selectedPile());
      }
    }
    System.out.println("pile #" + index + " clicked");
  }

  /**
   * This method creates a stock of 52 standard cards, and this helper method
   * is called in the constructor. It also shuffles the cards.
   */
  private void createStock()
  {
    ArrayList<Card> listOfCards = new ArrayList<Card>();
    for(int i = 1; i <= 13; i++)
    {
      for(int j = 0; j < 4; j++)
      {
        if(j == 0)
        {
          listOfCards.add(new Card("c", i));
        }
        if(j == 1)
        {
          listOfCards.add(new Card("s", i));
        }
        if(j == 2)
        {
          listOfCards.add(new Card("d", i));
        }
        else if(j == 3)
        {
          listOfCards.add(new Card("h", i));
        }

      }
    }
    while(listOfCards.size() != 0)
    {
      int index = ((int) (Math.random() * listOfCards.size()));
      Card card = listOfCards.get(index);
      stock.push(card);
      listOfCards.remove(index);
    }
  }

  /**
   * This method deals the cards from the stock onto the piles. The 0th pile has 1 card,
   * the 1st with 2 cards, and so on until the 6th pile. ONLY the top card in each pile
   * is turned up.
   */
  private void deal()
  {
    for(int i = 0; i < 7; i++)
    {
      for(int j = 0; j < i; j++)
      {
        Card temp = stock.pop();
        piles[i].push(temp);
        temp.turnDown();
      }
      Card temp = stock.pop();
      piles[i].push(temp);
      temp.turnUp();
    }
  }

  /**
   * This helper method puts three cards from the stock into the waste. If there are
   * less than three cards, all the stock cards are transferred
   */
  private void dealThreeCards()
  {
    for(int i = 0; i < 3; i++)
    {
      if(!stock.isEmpty())
      {
        Card temp = stock.pop();
        temp.turnUp();
        waste.push(temp);
      }
    }
  }

  /**
   * Resets the stock by transferring all the cards in the waste into the stock
   */
  private void resetStock()
  {
    while(!waste.isEmpty())
    {
      Card temp = waste.pop();
      temp.turnDown();
      stock.push(temp);
    }
  }

  /**
   * This method tests if a card can be added to the selected pile
   *
   * @param card  the card to be checked for validation
   * @param index the index of the pile to add to
   *
   * @precondition 0 <= index < 7
   * @postcondition Returns true if the given card can be
   *                legally moved to the top of the given pile
   *
   * @return  true  if the card can be added to the given pile
   *          false otherwise
   */
  private boolean canAddToPile(Card card, int index)
  {
    if(piles[index].isEmpty())
    {
      if(card.getRank() == 13)
      {
        return true;
      }
      return false;
    }
    else
    {
      Card temp = piles[index].peek();
      String suit = temp.getSuit();
      String suitCard = card.getSuit();
      int rank = temp.getRank();
      if (rank == 1)
      {
        return false;
      }
      if (card.getRank() == 13 && piles[index].isEmpty())
      {
        return true;
      }
      if ((card.getRank() + 1 == rank))
      {
        if (suit.equals("h") || suit.equals("d"))
        {
          if (suitCard.equals("s") || suitCard.equals("c"))
          {
            return true;
          }
          return false;
        }
        if (suit.equals("s") || suit.equals("c"))
        {
          if (suitCard.equals("h") || suitCard.equals("d"))
          {
            return true;
          }
          return false;
        }
      }
    }
    return false;
  }

  /**
   * This method returns all the face up cards in a given pile index
   *
   * @param index the index at which the pile is located to remove the face
   *              up cards
   *
   * @precondition  0 <= index < 7
   * @postcondition Removes all face-up cards on the top of
   *                the given pile; returns a stack
   *                containing these cards
   *
   * @return  a stack of cards that is face up from the given pile index
   */
  private Stack<Card> removeFaceUpCards(int index)
  {
    Stack<Card> newStack = new Stack<Card>();
    boolean shouldBreak = false;
    if(!piles[index].isEmpty())
    {
      while(!shouldBreak && piles[index].peek().isFaceUp())
      {
        newStack.push(piles[index].pop());
        if(piles[index].isEmpty())
        {
          shouldBreak = true;
        }
      }
    }
    return newStack;
  }

  /**
   * This method adds a stack of cards to a given pile at index
   *
   * @param cards the stack of cards to add to the pile
   * @param index the index at which the card will be added
   *
   * @precondition  0 <= index < 7
   * @postcondition Removes elements from cards, and adds
   *                them to the given pile.
   */
  private void addToPile(Stack<Card> cards, int index)
  {
    while(!cards.isEmpty())
    {
      piles[index].push(cards.pop());
    }
  }

  /**
   * This method determines if a card can be added to the given foundation at index
   *
   * @param card  the card to check for validity
   * @param index the index of the foundation
   *
   * @precondition 0 <= index < 4
   * @postcondition Returns true if the given card can be
   *                legally moved to the top of the given
   *                foundation
   *
   * @return  true  if the card can be added to foundations[index]
   *          false otherwise
   */
  private boolean canAddToFoundation(Card card, int index)
  {
    if(foundations[index].isEmpty())
    {
      if (card.getRank() == 1)
      {
        return true;
      }
      return false;
    }
    else
    {
      int rank = foundations[index].peek().getRank();
      String suit = foundations[index].peek().getSuit();
      if(card.getRank() - 1 == rank && card.getSuit().equals(suit))
      {
        return true;
      }
      return false;
    }
  }
}