package pkgCompare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {

	public static void main(String[] args) {
		doCompare();

	}

	private static void doCompare() {
		ArrayList<Animal> collAns = new ArrayList<>();
		collAns.add(new Animal("doggo", 12));
		collAns.add(new Animal("birdo", 11));
		collAns.add(new Animal("whjaly", 14));
		for (Animal a : collAns) {
			System.out.println(a.getName());
		}
		System.out.println();
		collAns.sort(new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		Collections.sort(collAns, (Animal o1, Animal o2) -> o1.getName().compareTo(o2.getName())); // Lamda experession

		for (Animal a : collAns) {
			System.out.println(a.getName());
		}

		collAns.stream().forEach((c) -> {
			System.out.println(c);
		});

		//Shortest version
		collAns.stream().sorted((a, b) -> a.getName().compareTo(b.getName()))
				.forEach(System.out::println);
		;

	}

}
