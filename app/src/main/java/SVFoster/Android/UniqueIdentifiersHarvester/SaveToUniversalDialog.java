/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.UniqueIdentifiersHarvester;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SaveToUniversalDialog extends AppCompatDialogFragment {
    private int ID = 0;
    private String PathPreset = "";
    private String Hint = "";
    private String Title = "";
    private Listener listener1 = null;

    public SaveToUniversalDialog(int ID, String PathPreset, String Hint, String Title) {
        super();

        this.ID = ID;
        this.PathPreset = PathPreset;
        this.Hint = Hint;
        this.Title = Title;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            this.listener1 = (Listener)context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(context.toString());
        }
    }

    public interface Listener{
        void SaveToUnvDialogCallback( int ID, String PathEntered );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder abldr = new AlertDialog.Builder(getActivity());
        LayoutInflater linf = getActivity().getLayoutInflater();
        View v = linf.inflate(R.layout.dialog_savetouniversal, null);

        EditText edt = v.findViewById(R.id.Edit1);
        edt.setText(this.PathPreset);
        edt.setHint(this.Hint);

        abldr.setView(v);
        abldr.setTitle(this.Title);
        abldr.setNegativeButton(R.string.saveto_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // NOP
            }
        });
        abldr.setPositiveButton(R.string.saveto_dialog_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pth = edt.getText().toString();
                SaveToUniversalDialog.this.listener1.SaveToUnvDialogCallback(ID, pth);
            }
        });

        return abldr.create();
    }
}
