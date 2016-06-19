package gemTracker;

import gemTracker.jGW2API;
import java.lang.Math;

import java.io.IOException;

import org.json.*;

public class CTG {
	/*
	 * Function to display the view for gold -> gem at
	 * 10g, 50g, 100g, and 200g
	 */
	public String showTable() throws IOException, JSONException {	
		JSONObject coinObj = jGW2API.getCoins("100000");
		JSONObject coinObj50 = jGW2API.getCoins("500000");
		JSONObject coinObj100 = jGW2API.getCoins("1000000");
		JSONObject coinObj250 = jGW2API.getCoins("2500000");
		String prices;
		
		prices = new String("\n\t"+coinObj250.getInt("quantity")+" Gems\t=\t"+"250 Gold\n\n"+
							"\t"+coinObj100.getInt("quantity")+" Gems\t=\t"+"100 Gold\n\n"+
							"\t"+coinObj50.getInt("quantity")+" Gems\t=\t"+"50 Gold\n\n"+
							"\t"+coinObj.getInt("quantity")+" Gems\t=\t"+"10 Gold");
		return prices;
	} 
	
	/*
	 * Function to display the view for gem -> gold at
	 * 400 gems, 800 gems, 1200 gems, 2000 gems.
	 * Makes an attempt to calculate it since unable to extract the actual
	 * amount from the GW2 API. (No parameter for gems in the API)
	 */
	public String reverseTable() throws IOException, JSONException {
		int coin, coin2, coin3, coin4, g, s, c;
		float fee = 0.7225f; 	
		/*
		 * 0.7225 = 0.85 * 0.85 (15% transaction fee going both ways)
		 */
		StringBuilder prices;
		
		JSONObject coinObj = jGW2API.getGems("400");
		JSONObject coinObj2 = jGW2API.getGems("800");
		JSONObject coinObj3 = jGW2API.getGems("1200");
		JSONObject coinObj4 = jGW2API.getGems("2000");
		
		/*
		 * Adding coins_per_gem makes it more accurate to the actual
		 */
		coin = Math.round( (coinObj.getInt("quantity")+coinObj.getInt("coins_per_gem") )/fee);
		coin2 = Math.round( (coinObj2.getInt("quantity")+coinObj2.getInt("coins_per_gem") )/fee);
		coin3 = Math.round( (coinObj3.getInt("quantity")+coinObj3.getInt("coins_per_gem") )/fee);
		coin4 = Math.round( (coinObj4.getInt("quantity")+coinObj4.getInt("coins_per_gem") )/fee);
		
		/*
		 * Value is in copper so have to extract it to obtain the amount of
		 * gold, silver, and copper.
		 */
		c = coin4 % 100;
		s = (coin4 % 10000)/100;
		g = coin4/10000;
		prices = new StringBuilder("\n\t2000 Gems\t=\t"+g+"g\t"+s+"s\t"+c+"c\n\n");
		
		c = coin3 % 100;
		s = (coin3 % 10000)/100;
		g = coin3/10000;
		prices.append("\t1200 Gems\t=\t"+g+"g\t"+s+"s\t"+c+"c\n\n");
		
		c = coin2 % 100;
		s = (coin2 % 10000)/100;
		g = coin2/10000;
		prices.append("\t800 Gems\t=\t"+g+"g\t"+s+"s\t"+c+"c\n\n");
		
		c = coin % 100;
		s = (coin % 10000)/100;
		g = coin/10000;
		prices.append("\t400 Gems\t=\t"+g+"g\t"+s+"s\t"+c+"c");

		return prices.toString();
	}
	
	/*
	 * Calculates the custom amount for gold -> gem and display it
	 */
	public String customAmount(int g, int s, int c) throws IOException, JSONException {	 
		int input = (g*10000) + (s*100) + c;
		JSONObject coinObj = jGW2API.getCoins(String.valueOf(input));
		
		return new String("\n\n\tAmount:\t"+coinObj.getInt("quantity")+" Gems\n"+
						"\n\n\tCost:\t\t"+g+"g  "+s+"s  "+c+"c\n");
	}
	
	/*
	 * Calculates the custom amount for gem -> gold and display it.
	 * Also not precise like the gem view in reverseTable()
	 */
	public String customGemAmount(int amount) throws IOException, JSONException {
		int coin, c, g, s;
		float fee = 0.7225f;
		/*
		 * 0.85 * 0.85 = 0.7225 (15% transaction fee going both ways)
		 */
		
		JSONObject coinObj = jGW2API.getGems(String.valueOf(amount));
		
		/*
		 * Adding coins_per_gem into the formula makes it more accurate to the actual amount
		 */
		coin = Math.round( (coinObj.getInt("quantity")+coinObj.getInt("coins_per_gem") )/fee);
		
		/*
		 * Total value is given in copper, so have to extract it to figure out how much
		 * gold, silver, and copper it is.
		 */
		c = coin % 100;
		s = (coin % 10000)/100;
		g = coin/10000;
		
		return new String("\n\n\tAmount:\t"+amount+" Gems\n"+
						  "\n\n\tCost:\t\t"+g+"g  "+s+"s  "+c+"c");
	}

}
