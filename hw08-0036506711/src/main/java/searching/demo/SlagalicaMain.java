package searching.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

public class SlagalicaMain {
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Program exprects 1 argument!");
			return;
		}
		String configString = args[0];
		String[] parts = configString.split("(?!^)");
		if (parts.length != 9) {
			System.out.println("Wrong number of elements!");
			return;
		}
		int[] config;
		try {
			config = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
		} catch (NumberFormatException e) {
			System.out.println("Wrong number format!");
			return;
		}
		for (int i = 0; i < 9; ++i) {
			if (!Arrays.toString(config).contains(String.valueOf(i))) {
				System.out.println("Configuration has to contain all digits from 0 to 8!");
				return;
			}
		}
		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(config));
		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);
		if (rješenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
		} else {
			System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
			List<KonfiguracijaSlagalice> lista = new ArrayList<>();
			Node<KonfiguracijaSlagalice> trenutni = rješenje;
			while (trenutni != null) {
				lista.add(trenutni.getState());
				trenutni = trenutni.getParent();
			}
			Collections.reverse(lista);
			lista.stream().forEach(k -> {
				System.out.println(k);
				System.out.println();
			});
			SlagalicaViewer.display(rješenje);
		}
	}
	
}
