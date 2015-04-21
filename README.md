GW2-Gem-Tracker


Main Purpose: Extracts the Gold -> Gem rate and display it in a GUI. Also allows user to set up an automatic update for the prices and a notification for when the rate drops to a user-defined threshold.

Four main files: CTG.java, customGUI.java, GridBagGUI.java, jGW2API.java

CTG:        Extracts and calculates all the gold to gem exchange rate

customGUI:  Creates a new frame for the user to input custom amounts to display

GridBagGUI: Builds the main GUI where all the information is displayed

jGW2API:    Taken from Varonth's wrapper class at (https://code.google.com/p/j-gw2-api/). Used Veroth's jGW2API.java file
            to establish connection to the API and modified it to used V2 of the API - mainly to use commerce/exchange
            of the GW2 API:2.
            
            
API information located at: http://wiki.guildwars2.com/wiki/API:2/commerce/prices
