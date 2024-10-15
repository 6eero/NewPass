package com.gero.newpass.view.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;

import com.gero.newpass.R;
import com.gero.newpass.databinding.FragmentSettingsBinding;
import com.gero.newpass.encryption.EncryptionHelper;
import com.gero.newpass.model.SettingData;
import com.gero.newpass.utilities.DialogHelper;
import com.gero.newpass.utilities.VibrationHelper;
import com.gero.newpass.view.activities.MainViewActivity;
import com.gero.newpass.view.adapters.SettingsAdapter;

import java.util.ArrayList;

import dev.trindadedev.ui.base.NewPassFragment;

public class SettingsFragment extends NewPassFragment {

    private static final int REQUEST_CODE_IMPORT_DOCUMENT = 2;
    private ImageButton buttonBack;
    private FragmentSettingsBinding binding;
    private ListView listView;
    private String url;
    private Intent intent;
    private EncryptedSharedPreferences encryptedSharedPreferences;
    static final int DARK_THEME = 0;
    static final int LOCK_SCREEN = 1;
    static final int CHANGE_LANGUAGE = 2;
    static final int CHANGE_PASSWORD = 3;
    static final int EXPORT = 4;
    static final int IMPORT = 5;
    static final int WEBSITE = 6;
    static final int GITHUB = 7;
    static final int SHARE = 8;
    static final int REPORT_ISSUE = 9;
    static final int APP_VERSION = 10;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(binding);
        Activity activity = this.getActivity();

        ArrayList<SettingData> arrayList = new ArrayList<>();
        encryptedSharedPreferences = EncryptionHelper.getEncryptedSharedPreferences(requireContext());

        buttonBack.setOnClickListener(v -> {
            if (activity instanceof MainViewActivity) {
                ((MainViewActivity) activity).onBackPressed();
            }
        });

        createSettingsList(arrayList);

        SettingsAdapter settingsAdapter = new SettingsAdapter(requireContext(), R.layout.list_row, arrayList, getActivity());

        listView.setAdapter(settingsAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {

            switch (position) {

                case CHANGE_LANGUAGE:
                    VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);

                    DialogFragment languageDialogFragment = new LanguageDialogFragment();
                    languageDialogFragment.setCancelable(false);
                    languageDialogFragment.show(requireActivity().getSupportFragmentManager(), "Language Dialog");

                    break;

                case CHANGE_PASSWORD:
                    VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);
                    DialogHelper.showChangePasswordDialog(requireContext(), encryptedSharedPreferences);
                    break;

                case EXPORT:
                    VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);
                    DialogHelper.showExportingDialog(requireContext());
                    break;

                case IMPORT:
                    VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);

                    Intent intentImport = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intentImport.addCategory(Intent.CATEGORY_OPENABLE);
                    intentImport.setType("*/*");
                    startActivityForResult(intentImport, REQUEST_CODE_IMPORT_DOCUMENT);
                    break;

                case WEBSITE:
                    VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);
                    url = "https://www.newpass.solutions/";
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    break;

                case GITHUB:
                    VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);
                    url = "https://github.com/6eero/NewPass";
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    break;

                case SHARE:
                    VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_text));
                    startActivity(Intent.createChooser(shareIntent, "Share with..."));
                    break;

                case REPORT_ISSUE:
                    VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);
                    url = "https://github.com/6eero/NewPass/blob/master/SECURITY.md";
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    break;

                case APP_VERSION:
                    Toast.makeText(requireContext(), "\uD83D\uDE80⚡", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }


    private void createSettingsList(ArrayList<SettingData> arrayList) {
        arrayList.add(new SettingData(DARK_THEME, R.drawable.settings_icon_dark_theme, getString(R.string.settings_dark_theme), false, true, 1));
        arrayList.add(new SettingData(LOCK_SCREEN, R.drawable.icon_open_lock, getString(R.string.use_screen_lock_to_unlock), false, true, 2));
        arrayList.add(new SettingData(CHANGE_LANGUAGE, R.drawable.settings_icon_language, getString(R.string.settings_change_language)));
        arrayList.add(new SettingData(CHANGE_PASSWORD, R.drawable.settings_icon_lock, getString(R.string.settings_change_password)));
        arrayList.add(new SettingData(EXPORT, R.drawable.icon_export, getString(R.string.settings_export_db)));
        arrayList.add(new SettingData(IMPORT, R.drawable.icon_import, getString(R.string.settings_import_db)));
        arrayList.add(new SettingData(WEBSITE, R.drawable.website, getString(R.string.website), true));
        arrayList.add(new SettingData(GITHUB, R.drawable.settings_icon_github, getString(R.string.settings_github), true));
        arrayList.add(new SettingData(SHARE, R.drawable.settings_icon_share, getString(R.string.settings_share_newpass), true));
        arrayList.add(new SettingData(REPORT_ISSUE, R.drawable.report_issue, getString(R.string.report_an_issue_securely), true));
        arrayList.add(new SettingData(APP_VERSION, R.drawable.settings_icon_version, getString(R.string.app_version) + getAppVersion()));
    }

    private String getAppVersion() {
        String versionName = "";

        try {
            PackageManager packageManager = requireActivity().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(requireActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AppVersion", "Error getting app version", e);
        }
        return versionName;
    }

    private void initViews(FragmentSettingsBinding binding) {
        buttonBack = binding.backButton;
        listView = binding.listView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri fileURL;

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_IMPORT_DOCUMENT) {
                if (data != null) {
                    fileURL = data.getData();

                    DialogHelper.showImportingDialog(requireContext(), fileURL);
                }
            }
        }
    }
}