package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;

import java.util.Arrays;
import java.util.Collections;


public class PadImpl implements Pad {

	private Richting [] richtingen;
	private Richting [] omgekeerdeRichting;

	private int totalCost;

	public PadImpl(Richting[] r, int totalCost){

		this.richtingen = r;
		omgekeerdeRichting = new Richting[richtingen.length];
		this.totalCost = totalCost;
	}

	@Override
	public int getTotaleTijd() {
		return this.totalCost;
	}

	@Override
	public Richting[] getBewegingen() {

		return richtingen;
	}

	@Override
	public Pad omgekeerd() {

		Collections.reverse(Arrays.asList(richtingen));

		for (int i = 0; i < richtingen.length; i++) {
			omgekeerdeRichting[i] = richtingen[i].omgekeerd();
		}

		return new PadImpl(omgekeerdeRichting, totalCost);
	}

	@Override
	public Coordinaat volg(Coordinaat start) {

		Coordinaat current = start;

		for (int i = 0; i < richtingen.length; i++) {
			current = current.naar(richtingen[i]);
			System.out.println(i + ": " + current);
		}
		return current;
	}

}
