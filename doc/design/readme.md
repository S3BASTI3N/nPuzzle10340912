Design Document
===============

## JAVA Classes

All JAVA classes are ordered as followed:

├── GamePlay.java   
├── ImageSelection.java   
├── PuzzleGame   
│   ├── GameField.java   
│   └── GameTile.java   
├── Utils   
│   ├── BitmapLoader.java   
│   └── SquareImageView.java   
└── YouWin.java   

***

#### ImageSelection

##### public class ImageSelection extends ActionBarActivity
- The main activity of this application. It will start when the app is loaded. Of type ActionBarActivity.

---

##### private void switchToGamePlay( int imageIndex )
- Changes to the next activity GamePlay and passes the resource id of the selected image.   

int imageIndex: the position of the selected image in the GridView of this activity.

---

##### public boolean onOptionsItemSelected(MenuItem item)
- Action handler for the menu in this activity.   

MenuItem item: the selected item in the menu. A action can follow according to which item was selected.

---

##### public static int[] IMAGES()
- Returns all resource ids of images in the drawable folder whose name starts with "npuzzle".

***

#### ImageAdapter

##### public class ImageAdapter extends BaseAdapter
- A basic adapter that can hold images. Used to display square preview images in the selection activity.

---

##### public ImageAdapter( Context context, int screenWidth )
- Constructor in of the image adapter.   

Context context: the context in which the adapter is going to be used.   
int screenWidth: the screen width of the application, later used to scale the preview images in the grid.

---
        
##### public int getCount()
- Get the amount of items in the grid.  
 
---
        
##### public Object getItem(int position)
- Get an Object based on the position in the grid

int position: the position of the item in the grid.

---
        
##### public long getItemId(int position)
- Get the id of an item based on the position of the item in the grid.

int position: the position of the item in the grid.

---
        
##### public View getView(int position, View convertView, ViewGroup parent)
- return how the View of the items in this adapter should be based on the position, View and Viewgroup

int position: the position of the item in the grid.   
View concertView: if the item exist already it is given in this variable.   
ViewGroup parent: the GridView that is the parent of the item.   

***

#### GamePlay

##### public class GamePlay extends ActionBarActivity
- Class that is the second activity of this application. Contains the actual puzzle. Also adds an click
listener to the game which handles the moves of the tiles in the puzzle.

---

##### protected void onCreate(Bundle savedInstanceState)
- Function that is called when the activity is started. Will create the puzzle and display it.

Bundle savedInstanceState: the state of the application in a previous execution

---

##### public void switchToCongratulate()
- Switch to the next activity to congratulate the user. Called when the tiles of the puzzle are in
the correct order

---

##### protected void onResume()
- Called after the activity is fully loaded. Used to first display the puzzle in its correct state. 
After three seconds the pieces are shuffled.

---

##### public void updateMovesDisplay()
- Upates the amount of moves the user has made on the display





