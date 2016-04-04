package com.mossige.finseth.follo.inf219_mitt_uib.ui;

import android.content.Context;
import android.util.AttributeSet;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;

/**
 * Created by Andre on 01/04/2016.
 */
public class RecipientSectionTitleIndicator extends SectionTitleIndicator<String> {

    public RecipientSectionTitleIndicator(Context context) {
        super(context);
    }

    public RecipientSectionTitleIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RecipientSectionTitleIndicator(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }


    @Override
    public void setSection(String name) {
        String[] names = name.split(" ");
        setTitleText(names[names.length-1].charAt(0) + "");
    }
}
