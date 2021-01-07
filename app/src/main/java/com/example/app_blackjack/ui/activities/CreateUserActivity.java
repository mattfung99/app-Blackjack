package com.example.app_blackjack.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.app_blackjack.model.Game;
import com.google.gson.Gson;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import com.example.app_blackjack.model.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CreateUserActivity extends AppCompatActivity {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();
    private User user;

    // Flags
    private boolean FLAG_PHOTO_MODIFIED;

    // Requests
    private static final int GALLERY_REQUEST = 9;
    private static final int CAMERA_REQUEST = 11;

    // UI Widgets
    private ImageView displayImageUser;
    private EditText eTextUsername;
    private EditText eTextPassword;
    private EditText eTextRePassword;
    private TextView textUsername;
    private TextView textPassword;
    private TextView textInvalidUsername;
    private TextView textInvalidPassword;
    private TextView textInvalidRePassword;
    private Button btnSaveUser;

    // Boolean Flags
    private boolean isValidUsername;
    private boolean isValidPassword;
    private boolean isValidRePassword;

    // Photo Path
    private Uri photoPath;

    // Global for Assigning Photo ID
    public static long PHOTO_ID_NUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        retrieveConstantFromSharedPref();
        displayActivityState();
        setupActivityButtons();
    }

    private void displayActivityState() {
        btnSaveUser = findViewById(R.id.btnCreateUser);
        setupUserProfile();
        if (getIntent().getBooleanExtra("edit", false)) {
            setTitle(getString(R.string.edit_user_activity_name));
            user = dHandler.getUser();
            setDefaultFields();
            btnSaveUser.setText(getString(R.string.btn_save_changes));
            displayEditSelectedImage();
        } else {
            setTitle(getString(R.string.create_user_activity_name));
            btnSaveUser.setText(getString(R.string.btn_create_user));
            displayAddSelectedImage();
        }
    }

    private void setDefaultFields() {
        eTextUsername.setText(user.getUsername());
        eTextPassword.setText(user.getPassword());
        eTextRePassword.setText(user.getPassword());
    }

    private void displayEditSelectedImage() {
        // Check if child already has a profile picture set
        if (!user.getFilepath().equals(getString(R.string.empty_file_path))) {
            // Display the custom set profile picture
            displayImageUser.setImageBitmap(setUserPhoto(Uri.fromFile(new File(user.getFilepath()))));
        } else {
            // Display default profile picture
            displayImageUser.setImageResource(R.drawable.default_user);
        }
    }

    private void displayAddSelectedImage() {
        // Display default profile picture
        displayImageUser.setImageResource(R.drawable.default_user);
    }

    private Bitmap setUserPhoto(Uri path) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(path);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setupUserProfile() {
        displayImageUser = (ImageView)findViewById(R.id.user_profile_picture);
        eTextUsername = (EditText)findViewById(R.id.editTextEnterUsername);
        eTextPassword = (EditText)findViewById(R.id.editTextEnterPassword);
        eTextRePassword = (EditText)findViewById(R.id.editTextReenterPassword);
        textUsername = (TextView)findViewById(R.id.textEnterUsername);
        textPassword = (TextView)findViewById(R.id.textEnterPassword);
        textInvalidUsername = (TextView)findViewById(R.id.textUsernameTaken);
        textInvalidPassword = (TextView)findViewById(R.id.textInvalidPassword);
        textInvalidRePassword = (TextView)findViewById(R.id.textInvalidRePassword);
        setupFieldsVisibility();
        setupReadInput();
        FLAG_PHOTO_MODIFIED = false;
    }

    private void setupReadInput() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateFields();
            }
        };
        eTextUsername.addTextChangedListener(textWatcher);
        eTextPassword.addTextChangedListener(textWatcher);
        eTextRePassword.addTextChangedListener(textWatcher);
    }

    private void validateFields() {
        isValidUsername = validateUsername();
        isValidPassword = validatePassword();
        isValidRePassword = validateRePassword();
    }

    private boolean validateUsername() {
        SharedPreferences userPref = getSharedPreferences("PREF_USERS", MODE_PRIVATE);
        String keyFromPref = userPref.getString(eTextUsername.getText().toString(), null);
        if (eTextUsername.getText().toString().matches(getString(R.string.empty_string))) {
            textInvalidUsername.setText(getString(R.string.invalid_empty_username));
            textInvalidUsername.setTextColor(ContextCompat.getColor(this, R.color.invalid_red));
            return false;
        } else if (eTextUsername.getText().toString().matches(".*\\s+.*")) {
            textInvalidUsername.setText(getString(R.string.invalid_username_contains_space));
            textInvalidUsername.setTextColor(ContextCompat.getColor(this, R.color.invalid_red));
            return false;
        } else if ((!getIntent().getBooleanExtra("edit", false)) &&
                (keyFromPref != null ||
                        eTextUsername.getText().toString().matches("error") ||
                        eTextUsername.getText().toString().matches("randomGame") ||
                        eTextUsername.getText().toString().contains("Game"))) {
            textInvalidUsername.setText(getString(R.string.invalid_username_taken));
            textInvalidUsername.setTextColor(ContextCompat.getColor(this, R.color.invalid_red));
            return false;
        } else {
            textInvalidUsername.setText(getString(R.string.valid_username));
            textInvalidUsername.setTextColor(ContextCompat.getColor(this, R.color.valid_green));
            return true;
        }
    }

    private boolean validatePassword() {
        if (eTextPassword.getText().toString().matches(getString(R.string.empty_string))) {
            textInvalidPassword.setText(getString(R.string.invalid_empty_password));
            textInvalidPassword.setTextColor(ContextCompat.getColor(this, R.color.invalid_red));
            return false;
        } else if (eTextPassword.getText().toString().matches(".*\\s+.*")) {
            textInvalidPassword.setText(getString(R.string.invalid_password_contains_space));
            textInvalidPassword.setTextColor(ContextCompat.getColor(this, R.color.invalid_red));
            return false;
        } else if (!eTextPassword.getText().toString().matches(".*\\d.*")) {
            textInvalidPassword.setText(getString(R.string.invalid_password_no_number));
            textInvalidPassword.setTextColor(ContextCompat.getColor(this, R.color.invalid_red));
            return false;
        } else {
            textInvalidPassword.setText(getString(R.string.valid_password));
            textInvalidPassword.setTextColor(ContextCompat.getColor(this, R.color.valid_green));
            return true;
        }
    }

    private boolean validateRePassword() {
        if (eTextRePassword.getText().toString().matches(eTextPassword.getText().toString())) {
            textInvalidRePassword.setText(getString(R.string.valid_re_password));
            textInvalidRePassword.setTextColor(ContextCompat.getColor(this, R.color.valid_green));
            return true;
        } else {
            textInvalidRePassword.setText(getString(R.string.invalid_re_password));
            textInvalidRePassword.setTextColor(ContextCompat.getColor(this, R.color.invalid_red));
            return false;
        }
    }

    private void setupFieldsVisibility() {
        if (!getIntent().getBooleanExtra("edit", false)) {
            textUsername.setText(getString(R.string.text_enter_username));
            textPassword.setText(getString(R.string.text_enter_password));
        } else {
            textUsername.setText(getString(R.string.text_enter_new_username));
            textPassword.setText(getString(R.string.text_enter_new_password));
        }
    }

    private void setupActivityButtons() {
        setupSaveUserButton();
        setupDeleteButton();
        setupSelectPhotoButton();
    }

    private void setupSaveUserButton() {
        // Save User
        btnSaveUser.setOnClickListener(v -> {
            if (getIntent().getBooleanExtra("edit", false)) {
                editUser();
            } else {
                createUser();
            }
        });
    }

    private void setupDeleteButton() {
        // Delete User
        Button buttonDeleteChild = findViewById(R.id.btnDelete);
        if (!getIntent().getBooleanExtra("edit", false)) {
            buttonDeleteChild.setVisibility(View.INVISIBLE);
        } else {
            buttonDeleteChild.setVisibility(View.VISIBLE);
            buttonDeleteChild.setOnClickListener(v -> deleteUser());
        }
    }

    private void createUser() {
        if (!isValidUsername || !isValidPassword || !isValidRePassword) {
            Toast.makeText(this, getString(R.string.toast_invalid_input), Toast.LENGTH_SHORT).show();
        } else {
            User newUser = new User(eTextUsername.getText().toString(), eTextPassword.getText().toString(), getString(R.string.empty_string));
            newUser.setFilepath(FLAG_PHOTO_MODIFIED ?
                    photoPath.toString() :
                    newUser.getFilepath()
            );
            saveUserIntoSharedPref(newUser);
            saveGameIntoSharedPref(null, newUser.getUsername());
            Toast.makeText(this, getString(R.string.toast_user_created), Toast.LENGTH_SHORT).show();
            saveConstantIntoSharedPref();
            finish();
        }
    }

    private void editUser() {
        String newUsername = eTextUsername.getText().toString();
        String existingUsername = user.getUsername();
        if (!isValidUsername || !isValidPassword || !isValidRePassword) {
            Toast.makeText(this, getString(R.string.toast_invalid_input_edit), Toast.LENGTH_SHORT).show();
        } else {
            user.setUsername(newUsername);
            user.setPassword(eTextPassword.getText().toString());
            user.setFilepath(FLAG_PHOTO_MODIFIED ?
                    photoPath.toString() :
                    user.getFilepath()
            );
            if (!newUsername.matches(existingUsername)) {
                deleteUserFromSharedPref(existingUsername);
                deleteGameFromSharedPref(existingUsername + "Game");
                saveUserSessionIntoSharedPref();
                saveGameIntoSharedPref(dHandler.getGame(), user.getUsername() + "Game");
            }
            saveUserIntoSharedPref(user);
            Toast.makeText(this, getString(R.string.toast_changes_saved), Toast.LENGTH_SHORT).show();
            saveConstantIntoSharedPref();
            finish();
        }
    }

    private void deleteUser() {
        deleteUserFromSharedPref(user.getUsername());
        dHandler.setUser(new User("error", "error404", ""));
        dHandler.setUserLoggedIn(false);
        saveLoggedOutSessionIntoSharedPref(false);
        Toast.makeText(this, getString(R.string.toast_user_deleted), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupSelectPhotoButton() {
        // Select Photo
        Button buttonSelectPhoto = findViewById(R.id.btnSelectPhoto);
        buttonSelectPhoto.setOnClickListener(v -> displayImageSelectionOptions());
    }

    private void displayImageSelectionOptions() {
        final String[] selectionOptions = getResources().getStringArray(R.array.photo_options);
        AlertDialog.Builder selectionBuilder = new AlertDialog.Builder(this);
        selectionBuilder.setTitle(R.string.title_select_photo_alert_dialog)                                        // Will display alert dialog with two options
                .setItems(selectionOptions, (dialog, whichOption) ->
                        photoSelectionOptions(whichOption));
        AlertDialog photoDialog = selectionBuilder.create();
        photoDialog.show();
    }

    private void photoSelectionOptions(int selectedOption) {
        switch (selectedOption) {
            case 0:
                fetchGalleryPhoto();                                                            // Choose photo from gallery
                break;
            case 1:
                fetchCameraPhoto();                                                             // Take photo with camera
                break;
        }
    }

    private void fetchGalleryPhoto() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    private void fetchCameraPhoto() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {        // Choose photo from gallery
            Bitmap bitmap = setUserPhoto(data.getData());
            displayImageUser.setImageBitmap(bitmap);
            assert bitmap != null;
            photoPath = savePhoto(this, bitmap);
        } else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {   // Take photo with camera
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            displayImageUser.setImageBitmap(bitmap);
            photoPath = savePhoto(this, bitmap);
        }
        FLAG_PHOTO_MODIFIED = true;
    }

    private Uri savePhoto(Context context, Bitmap bitmap) {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        ContextWrapper wrapper = new ContextWrapper(context);
        File createFile = wrapper.getDir("Images", MODE_PRIVATE);
        createFile = new File(createFile, "snap_" + timeStamp + "_" + PHOTO_ID_NUM + ".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(createFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            PHOTO_ID_NUM++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.parse(createFile.getAbsolutePath());
    }

    private void saveUserIntoSharedPref(User newUser) {
        saveObjIntoSharedPref(newUser, newUser.getUsername(), "PREF_USERS");
    }

    private void deleteUserFromSharedPref(String deleteUsername) {
        deleteObjFromSharedPref(deleteUsername, "PREF_USERS");
    }

    private void saveGameIntoSharedPref(Game currGame, String key) {
        saveObjIntoSharedPref(currGame, key + "Game", "PREF_GAMES");
    }

    private void deleteGameFromSharedPref(String deleteGameKey) {
        deleteObjFromSharedPref(deleteGameKey, "PREF_GAMES");
    }

    private void saveObjIntoSharedPref(Object obj, String objName, String dir) {
        SharedPreferences pref = getSharedPreferences(dir, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(objName, json);
        editor.apply();
    }

    private void deleteObjFromSharedPref(String objName, String dir) {
        SharedPreferences userPref = getSharedPreferences(dir, MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPref.edit();
        userEditor.remove(objName);
        userEditor.apply();
    }

    private void saveUserSessionIntoSharedPref() {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        sessionEditor.putString("userSessionKey", user.getUsername());
        sessionEditor.apply();
    }

    private void saveConstantIntoSharedPref() {
        SharedPreferences prefs = getSharedPreferences("PREF_PHOTO_CONST", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("photoIDNum", PHOTO_ID_NUM);
        editor.apply();
    }

    private void saveLoggedOutSessionIntoSharedPref(boolean userNotLoggedOut) {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        sessionEditor.putString("userSessionKey", "error");
        sessionEditor.putBoolean("userLoggedIn", userNotLoggedOut);
        sessionEditor.apply();
    }

    private void retrieveConstantFromSharedPref() {
        SharedPreferences prefs = getSharedPreferences("PREF_PHOTO_CONST", MODE_PRIVATE);
        PHOTO_ID_NUM = prefs.getLong("photoIDNum", 1);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CreateUserActivity.class);
    }
}