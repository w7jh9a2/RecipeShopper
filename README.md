# RecipeShopper
Android recipe and shopping list management app

This android application is a recipe and shopping list management and creation tool. It is compatible with
API 16: Android 4.1 and higher. While a work in progress, it is currently in a usable state. The application's
UI utilizes Fragments and RecyclerViews to intuitively present recipe information, instructions, ingredients,
and shopping list ingredients. Currently the application is compatible with recipes from the website www.seriouseats.com
that follow a specific HTML formatting. A more robust approach and further support for other websites is being developed.
The application takes the URL of the desired recipe's page and asynchronously downloads and parses the relevant information
from the HTML data. The recipe can then be selected, its information and instructions viewed, and its ingredients individually
selected to add to a shopping list. As ingredients across multiple recipes are selected, they are added to a single shopping
list. Multiple instances of the same ingredients present in the shopping list are consolidated to provide a streamlined shopping
experience. If the units of measurement mismatch, the application converts them to a unified measurement before consolidating the
ingredients. As ingredients are checked off the shopping list, they are removed from the shopping list and marked appropriately
in the corresponding recipe entries.

# Installing
The application can currently be pulled and deployed on Android devices or virtual devices supporting API 16 or higher. The easiest way
to deploy would be to open the project in Android Studio and run the app on the desired device.

# Use
The application begins on an empty list where recipe entries will be stored. Swiping right enters the ShoppingListFragment where
necessary ingredients will eventually be shown. Swiping to the left returns to the RecipeListFragment. 

To add a recipe press the floating action button with the '+' icon and paste the URL for the desired recipe from a supported site into
the text field. After pressing "ADD RECIPE FROM URL" the application may take some time as it downloads and parses the relevant data. 
New entries should be populated on the list as they complete with thumbnails retrieved from the site, the title of the recipe, active 
preparation time, total preparation time, and yield. Tapping on an entry will open the RecipeActivity to the IngredientsFragment. Here 
the ingredients for the recipe are listed with a checkbox next to every entry. Unchecking an ingredient will mark it to be added to the 
shopping list.

Swiping right from the IngredientsFragment navigates to the InstructionsFragment, where the recipe instructions are stored. Checkboxes
accompany instruction steps for users to keep track of completed steps. 

After unchecking some ingredients, the ShoppingListFragment should be populated. Navigating back to the ShoppingListFragment can be
accomplished by pressing the back button. The ShoppingListFragment should now show all ingredients that were unchecked across any
recipe entries. Multiple instances of the same ingredient will be consolidated to a single entry on the shopping list. For example,
if 1 tbsp of salt from recipe A is unchecked and 1 cup of salt from recipe B is unchecked, the application will convert the units
and list a single 1.0625 cups of salt entry in the shopping list. Checking an ingredient in the shopping list marks the ingredient as
having been purchased and removes it from the shopping list. The ingredient is also accordingly checked for any recipes the ingredient
was unchecked. Using the previous example, checking off the 1.0625 cups of salt would remove the 1.0625 cups of salt from the 
ShoppingListFragment and also check the entries for 1 tbsp salt in recipe A's ingredient list and the 1 cup of salt in recipe B's
ingredient list.


# Built With
Picasso - http://square.github.io/picasso/ - Used to download images from recipe websites and produce thumbnails

# Authors
Andrew Nam
