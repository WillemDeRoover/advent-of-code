package _2020.day21;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Puzzle {

	private static final Pattern pattern = Pattern.compile("([a-z]+)");
	private static final Map<String, String> ingredientMap = new HashMap<>();

	public static void main(String[] args) throws IOException {
		List<String[]> foods = Files.lines(Paths.get("src/_2020/day21/input.txt"))
				.map(s -> s.split("contains"))
				.collect(Collectors.toList());

		Map<String, List<String>> allergenMap = new HashMap<>();
		for (String[] food : foods){
			List<String> ingredients = getAllergens(food[0]);
			List<String> allergens = getAllergens(food[1]);
			for (String allergen : allergens) {
				if(allergenMap.containsKey(allergen)) {
					List<String> currentIngredients = allergenMap.get(allergen);
					currentIngredients.retainAll(ingredients);

				} else {
					allergenMap.put(allergen, new ArrayList<>(ingredients));
				}
			}
		}

		Set<String> remainingFoods = allergenMap.values().stream().flatMap(List::stream).collect(toSet());
		int wrongIngredients = 0;
		for (String[] food : foods) {
			List<String> ingredients = new ArrayList<>(getAllergens(food[0]));
			ingredients.removeAll(remainingFoods);
			wrongIngredients += ingredients.size();
		}
		System.out.println(wrongIngredients);




		matchFoodAndAllergen(new ArrayList<>(allergenMap.keySet()), allergenMap);
		String canonicalIngredientList = ingredientMap.entrySet().stream()
				.sorted(comparing(Map.Entry::getValue))
				.map(Map.Entry::getKey)
				.collect(joining(","));
		System.out.println(canonicalIngredientList);

	}

	private static boolean matchFoodAndAllergen(List<String> allergens, Map<String, List<String>> allergenMap) {
		if(allergens.isEmpty()) {
			return  true;
		}
		String allergen = allergens.get(0);
		List<String> ingredients = allergenMap.get(allergen);
		for (String ingredient : ingredients) {
			if(ingredientMap.containsKey(ingredient)) {
				continue;
			}
			ingredientMap.put(ingredient, allergen);
			List<String> updatedAllergens = new ArrayList<>(allergens);
			updatedAllergens.remove(allergen);

			if(matchFoodAndAllergen(updatedAllergens, allergenMap)) {
				return true;
			} else {
				ingredientMap.remove(ingredient);
			}
		}
		return false;
	}

	private static List<String> getAllergens(String s) {
		Matcher matcher = pattern.matcher(s);
		List<String> allergens = new ArrayList<>();
		while(matcher.find()) {
			allergens.add(matcher.group(0));
		}
		return allergens;
	}

}
