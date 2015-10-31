package com.starsep.ktulu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ShowCardActivity extends AppCompatActivity {

    private final static int[] cards = {
            R.drawable.agent,
            R.drawable.burmistrz,
            R.drawable.cicha_stopa,
            R.drawable.detektor,
            R.drawable.dobry_rew,
            R.drawable.dzentelmen,
            R.drawable.dziwka,
            R.drawable.hazardzista,
            R.drawable.herszt,
            R.drawable.kat,
            R.drawable.lekarz,
            R.drawable.lornecie_oko,
            R.drawable.msciciel,
            R.drawable.ochroniarz,
            R.drawable.opoj,
            R.drawable.pastor,
            R.drawable.pijany_sedzia,
            R.drawable.plonacy_szal,
            R.drawable.poborca_podatkowy,
            R.drawable.podwojnie_pijany_sedzia,
            R.drawable.podwojny_opoj,
            R.drawable.pozeracz_umyslow,
            R.drawable.purpurowa_przyssawka,
            R.drawable.samotny_kojot,
            R.drawable.sedzia,
            R.drawable.szaman,
            R.drawable.szamanka,
            R.drawable.szantazysta,
            R.drawable.szeryf,
            R.drawable.szuler,
            R.drawable.uwodziciel,
            R.drawable.wielki_ufol,
            R.drawable.wodz,
            R.drawable.wojownik,
            R.drawable.zielona_macka,
            R.drawable.zlodziej,
            R.drawable.zly_rew
    };

    private int id = 0;
    ImageView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card);
        cardView = (ImageView) findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setImageResource(cards[++id]);
                if (id == cards.length - 1) {
                    id = 0;
                }
            }
        });
    }
}
