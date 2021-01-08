package com.example.scratchapplication.fragment.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.scratchapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsFragment extends PreferenceFragmentCompat {
    private Preference changePassword;
    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getContext());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            addPreferencesFromResource(R.xml.pref_settings);

            changePassword = findPreference("changePass");
            changePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showChangePassDialog();
                    return false;
                }
            });

    }
private void showChangePassDialog(){
    View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_password,null);
    EditText etOldPass = view.findViewById(R.id.etOldPass);
    EditText etNewPass = view.findViewById(R.id.etNewPass);
    Button btnUpdatePass = view.findViewById(R.id.btnUpdatePass);

    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setView(view);
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
    btnUpdatePass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String oldPass = etOldPass.getText().toString().trim();
            String newPass = etNewPass.getText().toString().trim();
            if(TextUtils.isEmpty(oldPass)){
                Toast.makeText(getContext(), "Please enter your current password!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(newPass.length() < 6){
                Toast.makeText(getContext(),"Password length must atleast 6 characters",Toast.LENGTH_LONG).show();
                return;
            }
            alertDialog.dismiss();
            updatePass(oldPass,newPass);
        }
    });
}

    private void updatePass(String oldPass, String newPass) {
        dialog.setMessage("Updating...");
        dialog.show();
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(getContext(),"Password Updated!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
