package br.com.estimulos.app.ui.configuracao;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by caioc_000 on 06/03/2016.
 */
public class DatePickerFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create and return the date picker dialog
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        ConfDadosJogoActivity context = (ConfDadosJogoActivity) getActivity();
        return new DatePickerDialog(context, context, year, month, day);
    }
}
