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


#### ImageSelection

##### public class ImageSelection extends ActionBarActivity
- The main activity of this application. It will start when the app is loaded. Of type ActionBarActivity.

###### private void switchToGamePlay( int imageIndex )
- Changes to the next activity GamePlay and passes the resource id of the selected image.   

int imageIndex, the position of the selected image in the GridView of this activity.


###### public boolean onOptionsItemSelected(MenuItem item)
- Action handler for the menu in this activity.   

MenuItem item, the selected item in the menu. A action can follow according to which item was selected.


###### public static int[] IMAGES()
- Returns all resource ids of images in the drawable folder whose name starts with "npuzzle".






