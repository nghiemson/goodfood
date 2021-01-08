package com.example.scratchapplication.fragment.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.scratchapplication.ForgotPasswordActivity;
import com.example.scratchapplication.MainActivity;
import com.example.scratchapplication.R;
import com.example.scratchapplication.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static android.content.ContentValues.TAG;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SignInFragment extends Fragment {
    public static final int AUTHENTICATION_DURATION_SECONDS = 20;
    public static final String KEY_NAME = "key";
    public static final String TRANSFORMATION = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
            + KeyProperties.ENCRYPTION_PADDING_PKCS7;
    public static final String CHARSET_NAME = "UTF-8";
    public static final String STORAGE_FILE_NAME = "credentials";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private final static int RC_SIGN_IN = 123;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    private TextView signUp;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView forgotPass;
    private TextView textError;
    private LinearLayout buttonSignInFB;
    private LinearLayout buttonSignInGoogle;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressDialog dialog;
    private KeyguardManager keyguardManager;
    private CheckBox saveCredentials;
    private LinearLayout loginWithFingerprint;
    private BiometricPrompt.PromptInfo promptInfo;
    private View.OnClickListener loginOncLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String usernameString = usernameEditText.getText().toString().trim();
            String passwordString = passwordEditText.getText().toString().trim();
            if (saveCredentials.isChecked() && !usernameString.equals("") && !passwordString.equals("")) {

               saveCredentialsAndLogin();

            } else {

                if (!usernameString.equals("") && !passwordString.equals("")) {
                    dialog.setMessage("Logging in...");
                    dialog.show();
                    signInWithUser(usernameString, passwordString);
                } else {
                    Toast.makeText(getContext(), "Input is empty", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        v.findViewById(R.id.btn_signin).setOnClickListener(loginOncLickListener);
        signUp = v.findViewById(R.id.idSignUp);
        dialog = new ProgressDialog(getContext());
        buttonSignInFB = v.findViewById(R.id.btn_signin_fb);
        buttonSignInGoogle = v.findViewById(R.id.btn_signin_google);
        passwordEditText = v.findViewById(R.id.txtPass);
        usernameEditText = v.findViewById(R.id.txtEmailOrUsername);
        forgotPass = v.findViewById(R.id.tvForgotPass);
        textError = v.findViewById(R.id.textError);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        keyguardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
        saveCredentials = v.findViewById(R.id.saveCredentials);
        loginWithFingerprint = v.findViewById(R.id.loginWithFingerprint);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ForgotPasswordActivity.class));
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint authentication ")
                .setSubtitle("Verify it's you")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

        loginWithFingerprint.setOnClickListener(v1 ->{
            loginWithFingerprint();
        });

        buttonSignInFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email", "phone_number"));
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.login_container, new SignUpFragment()).commit();
            }
        });
        return v;
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {
        // Callback for biometric authentication result
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                Log.e(TAG, "Error code: " + errorCode + "error String: " + errString);
                super.onAuthenticationError(errorCode, errString);

                Toast.makeText(getContext(), "Fingerprint "+errString,
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Log.i(TAG, "onAuthenticationSucceeded");
                super.onAuthenticationSucceeded(result);
                loginWithFingerprint();
            }

        @Override
        public void onAuthenticationFailed () {
            super.onAuthenticationFailed();
            Toast.makeText(getContext(), "Authentication failed",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    };
}
    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback1() {
        // Callback for biometric authentication result
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                Log.e(TAG, "Error code: " + errorCode + "error String: " + errString);
                super.onAuthenticationError(errorCode, errString);

                Toast.makeText(getContext(), "Fingerprint "+errString,
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Log.i(TAG, "onAuthenticationSucceeded");
                super.onAuthenticationSucceeded(result);
                saveCredentialsAndLogin();
            }

            @Override
            public void onAuthenticationFailed () {
                super.onAuthenticationFailed();
                Toast.makeText(getContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        };
    }

    private Executor getMainThreadExecutor() {
        return new MainThreadExecutor();
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable r) {
            handler.post(r);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!keyguardManager.isKeyguardSecure()) {
            Toast.makeText(getContext(),
                    "Secure lock screen hasn't set up. Go to 'Settings -> Security -> Screenlock' to set up a lock screen",
                    Toast.LENGTH_LONG).show();
        }
        saveCredentials.setEnabled(keyguardManager.isKeyguardSecure());
        loginWithFingerprint.setEnabled(keyguardManager.isKeyguardSecure());

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveCredentialsAndLogin() {
        if (canAuthenticateWithStrongBiometrics()) {
            try {
                // encrypt the password
                String passwordString = passwordEditText.getText().toString();
                SecretKey secretKey = createKey();
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptionIv = cipher.getIV();
                byte[] passwordBytes = passwordString.getBytes(CHARSET_NAME);
                byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);
                String encryptedPassword = Base64.encodeToString(encryptedPasswordBytes, Base64.DEFAULT);

                // store the login data in the shared preferences
                // only the password is encrypted, IV used for the encryption is stored
                String usernameString = usernameEditText.getText().toString();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(STORAGE_FILE_NAME, Activity.MODE_PRIVATE).edit();
                editor.putString("username", usernameString);
                editor.putString("password", encryptedPassword);
                editor.putString("encryptionIv", Base64.encodeToString(encryptionIv, Base64.DEFAULT));
                editor.apply();
                dialog.setMessage("Logging in...");
                dialog.show();
                signInWithUser(usernameString, passwordString);
            } catch (UserNotAuthenticatedException e) {
                showBiometricPrompt1();
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException
                    | BadPaddingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loginWithFingerprint() {
        if(canAuthenticateWithStrongBiometrics()){
        try {
            // load login data from shared preferences (
            // only the password is encrypted, IV used for the encryption is loaded from shared preferences
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(STORAGE_FILE_NAME, Activity.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            if (username == null) {
                Toast.makeText(getContext(), "You must first login with password and check Remember Me.", Toast.LENGTH_LONG).show();
                return;
            }
            String base64EncryptedPassword = sharedPreferences.getString("password", null);
            String base64EncryptionIv = sharedPreferences.getString("encryptionIv", null);
            byte[] encryptionIv = Base64.decode(base64EncryptionIv, Base64.DEFAULT);
            byte[] encryptedPassword = Base64.decode(base64EncryptedPassword, Base64.DEFAULT);

            // decrypt the password
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryptionIv));
            byte[] passwordBytes = cipher.doFinal(encryptedPassword);
            String password = new String(passwordBytes, CHARSET_NAME);
            dialog.setMessage("Logging in...");
            dialog.show();
            signInWithUser(username, password);
        } catch (UserNotAuthenticatedException e) {
            showBiometricPrompt();

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException
                | BadPaddingException | InvalidAlgorithmParameterException
                | UnrecoverableKeyException | KeyStoreException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }

    }}

    @RequiresApi(api = Build.VERSION_CODES.M)
    private SecretKey createKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_DURATION_SECONDS)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Failed to create a symmetric key", e);
        }
    }
    private void showBiometricPrompt() {
         BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationCallback();
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, getMainThreadExecutor(), authenticationCallback);
            biometricPrompt.authenticate(promptInfo);
        }

    private void showBiometricPrompt1() {
        BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationCallback1();
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, getMainThreadExecutor(), authenticationCallback);
        biometricPrompt.authenticate(promptInfo);
    }

    private boolean canAuthenticateWithStrongBiometrics() {
        return BiometricManager.from(getContext())
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    private void signInWithUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            dialog.dismiss();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            textError.setText( task.getException().getMessage());
                        }

                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*private void createRequest() {
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getContext(),gso);
    }

    private void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user){
        String avatar = null;
        if (user.getPhotoUrl() != null){
             avatar = user.getPhotoUrl().toString();
        }
        String name = user.getDisplayName();
        String uid = user.getUid();
        ArrayList followers = new ArrayList();
        User dataUser = new User(name, avatar,"",0, followers);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        FirebaseDatabase dataTemp = myRef.child(uid).getDatabase();
        if (dataTemp == null){
            myRef.child(uid).setValue(dataUser);
        }else {
        }
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("CHECK", false);
        startActivity(intent);
        getActivity().finish();
    }

    public void buttonClickFB(View v){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "User cancel it!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser myuserobj = mAuth.getCurrentUser();
                    updateUI(myuserobj);
                }else {
                    Toast.makeText(getContext(), "Could not register to firebase", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
