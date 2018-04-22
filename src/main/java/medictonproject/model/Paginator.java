package medictonproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Paginator<T> {
  
  @JsonIgnore
  private List<T> itemList;

  private int itemsPerPage;
  private int currentPage;
  private int totalItems;
  private List<T> actualPageItems;

  private int firstPage;
  private int lastPage;
  private int nextPage;
  private int prevPage;


  public Paginator( List<T> itemList, int page, int itemsPerPage ) {
    this.totalItems = itemList.size();
    this.itemList = itemList;
    this.currentPage = totalItems > 0 ? page : 1;
    this.itemsPerPage = itemsPerPage;

    firstPage = 1;
    lastPage = (int)Math.ceil((double)totalItems / itemsPerPage);
    nextPage = (currentPage == lastPage) ? -1 : currentPage + 1;
    prevPage = (currentPage == firstPage) ? -1 : currentPage - 1;

    setActualPageItems();
  }
  
  private void setActualPageItems() {
    int upperBound = currentPage * itemsPerPage;
    if( upperBound > totalItems )
      upperBound = totalItems;
    
    int lowerBound = totalItems == 0 ? 0 : (currentPage - 1) * itemsPerPage;

    if(lowerBound > upperBound)
      actualPageItems = new ArrayList<>();
    else
      actualPageItems = itemList.subList( lowerBound, upperBound );
  }
  
  public List<T> getItemList()
  {
    return itemList;
  }
  
  public void setItemList( List<T> itemList )
  {
    this.itemList = itemList;
  }
  
  public int getItemsPerPage()
  {
    return itemsPerPage;
  }
  
  public void setItemsPerPage( int itemsPerPage )
  {
    this.itemsPerPage = itemsPerPage;
  }
  
  public int getCurrentPage()
  {
    return currentPage;
  }
  
  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }
  
  public List<T> getActualPageItems()
  {
    return actualPageItems;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }
}
