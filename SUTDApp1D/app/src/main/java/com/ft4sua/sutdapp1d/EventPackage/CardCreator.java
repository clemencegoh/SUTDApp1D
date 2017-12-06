package com.ft4sua.sutdapp1d.EventPackage;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swonlek on 6/12/2017.
 */

public class CardCreator {
    static CardCreator cardCreator;
    List<CardParent> cardParents;

    public CardCreator(Context context) {
        cardParents = new ArrayList<>();
        for (int i = 0; i <= 10; i++){
            CardParent card = new CardParent("Event " +i,"Date, Time, Location");
            cardParents.add(card);
        }
    }

    public static CardCreator get (Context context){
        if(cardCreator == null){
            cardCreator = new CardCreator(context);
        }
        return cardCreator;
    }

    public List<CardParent> getAll() {
        return cardParents;
    }
}
