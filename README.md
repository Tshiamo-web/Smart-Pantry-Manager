Smart Pantry Manager 

 About the Project

Smart Pantry Manager is a Java-based Android application developed to help users keep track of the ingredients they already have at home and discover recipes that can be prepared using those ingredients.

The main idea behind the application is simple: instead of showing recipes that require ingredients the user does not have, the app checks the user's pantry before making a recipe suggestion.

For example, if a recipe requires 2 eggs, 1 onion and 1 tomato, the recipe will only appear when the pantry contains enough of all three ingredients. If an ingredient or the required quantity is missing, that recipe will not be suggested.

This makes the application focused on practical pantry management and realistic recipe suggestions.

 Main Features

 Pantry Management

Users can:

* Add ingredients to their pantry
* Enter the quantity and unit of an ingredient
* Add an optional expiry date
* View all stored pantry items
* Edit existing pantry items
* Delete pantry items
* See whether an ingredient is still good, expiring soon, or expired

 Smart Recipe Suggestions

The application contains a collection of pre-loaded recipes.

When the user opens Suggested Recipes, the application compares the recipe requirements with the ingredients stored in the pantry.

A recipe is only displayed when:

* Every required ingredient is available
* The available quantity is sufficient
* The ingredient and unit can be matched correctly

The application does not display recipes simply because the user has some of the required ingredients.

Recipe Details

Users can select an available recipe to view:

* Recipe name
* Required ingredients
* Quantities and units
* Preparation instructions

  Settings

The application also includes a Settings screen accessible from the application's navigation menu.

Application Screens

The application includes the following main screens:

1. Home — provides access to the main application features.
2. Pantry — allows ingredients to be added and managed.
3. Pantry List — displays the ingredients currently stored.
4. Suggested Recipes — displays recipes that can actually be prepared from the pantry.
5. Recipe Details — displays the ingredients and preparation steps for a selected recipe.
6. Settings — provides access to the application's settings area.

Technology Used

The application was developed using:

* Java — main programming language
* Android Studio — development environment
* XML — user interface layouts
* SQLite — local database
* RecyclerView — displaying lists of pantry items and recipes
* Android Intents— navigation between application screens

Why SQLite?

I chose SQLite as the database for Smart Pantry Manager because the application is designed to store pantry and recipe information locally on the Android device.

SQLite is built into Android, so it does not require a separate database server or an internet connection. This makes it suitable for storing the pantry items and recipe information required by this application.

The database is managed through a custom DatabaseHelper class.

 Database Information

The application uses the following main data areas:

 Pantry Items

Pantry records contain information such as:

* Ingredient name
* Quantity
* Unit
* Optional expiry date

Recipes

Recipe records contain:

* Recipe name
* Required ingredients
* Required quantities and units
* Preparation instructions

The application also pre-loads a collection of recipes when the database is initialised.

Recipe Matching

One of the main parts of the application is the recipe-matching logic.

The application checks the user's pantry against each recipe's required ingredients before displaying a suggestion.

The matching process also handles common differences such as singular and plural ingredient names and compatible unit representations.

This prevents the application from recommending a recipe when the user is missing an ingredient or does not have enough of the required quantity.

Project Structure

Some of the main classes in the project include:

* MainActivity — Home screen and main navigation
* PantryActivity — Add pantry items
* PantryListActivity — Display pantry items
* EditPantryActivity — Edit pantry items
* SuggestedRecipesActivity — Recipe matching and suggestions
* RecipeDetailActivity — Recipe information
* SettingsActivity — Settings screen
* DatabaseHelper — SQLite database creation, storage and retrieval
* PantryItem — Pantry item model
* Recipe — Recipe model
* PantryAdapter — Pantry RecyclerView adapter
* RecipeAdapter — Recipe RecyclerView adapter

 How to Run the Application

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Android Studio to complete the Gradle synchronisation.
4. Connect an Android device or start an Android Emulator.
5. Build the project.
6. Run the application.
7. The SQLite database will be created and initialised when the application starts.

Project Purpose

The purpose of Smart Pantry Manager is to combine pantry organisation with practical recipe discovery.

Instead of encouraging users to search through recipes and then determine whether they have the required ingredients, the application starts with what the user already has and identifies recipes that can be prepared from those available ingredients.

Author

Tshiamo

Java Android Application Development Project
