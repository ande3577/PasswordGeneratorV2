package org.dsanderson.passwordgeneratorv2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.dsanderson.password_generator.core.LettersAndNumberPasswordGenerator;
import org.dsanderson.password_generator.core.LettersOnlyPasswordGenerator;
import org.dsanderson.password_generator.core.NumbersOnlyPasswordGenerator;
import org.dsanderson.password_generator.core.PasswordGenerator;


public class PasswordGeneratorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener,
            Spinner.OnItemSelectedListener {
        final int LETTERS_NUMBERS_AND_SPECIAL_CHAR_INDEX = 0;
        final int LETTERS_ONLY_INDEX = 1;
        final int NUMBERS_AND_LETTERS_INDEX = 2;
        final int NUMBERS_ONLY_INDEX = 3;

        final int INITIAL_PASSWORD_TYPE_INDEX = LETTERS_NUMBERS_AND_SPECIAL_CHAR_INDEX;
        int passwordType = INITIAL_PASSWORD_TYPE_INDEX;

        final String PASSWORD_KEY = "passwordKey";



        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_password_generator, container, false);
            rootView.findViewById(R.id.generateButton).setOnClickListener(this);
            rootView.findViewById(R.id.clearButton).setOnClickListener(this);
            ((TextView) rootView.findViewById(R.id.length)).setText(Integer.toString(PasswordGenerator.DEFAULT_LENGTH));
            setupPasswordType((Spinner) rootView.findViewById(R.id.passwordType));
            if(savedInstanceState != null) {
                String password = savedInstanceState.getString(PASSWORD_KEY);
                if (password.length() > 0) {
                    ((TextView) rootView.findViewById(R.id.password)).setText(password);
                    rootView.findViewById(R.id.passwordLayout).setVisibility(View.VISIBLE);
                }
            }
            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putString(PASSWORD_KEY, getPasswordView().getText().toString());
        }

        void setupPasswordType(Spinner spinner) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.password_types, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            passwordType = INITIAL_PASSWORD_TYPE_INDEX;
            spinner.setSelection(INITIAL_PASSWORD_TYPE_INDEX);
        }

        TextView getLengthView() {
            return (TextView) getView().findViewById(R.id.length);
        }

        int getLengthValue() {
            return Integer.parseInt(getLengthView().getText().toString());
        }

        String getKeywordValue() {
            return ((TextView) getView().findViewById(R.id.keyword)).getText().toString();
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.generateButton:
                    onGenerateButtonClicked();
                    break;
                case R.id.clearButton:
                    onClearButtonClicked();
                    break;
            }
        }

        void onGenerateButtonClicked() {
            PasswordGenerator passwordGenerator = newPasswordGenerator(passwordType);
            String password;
            try {
                password = passwordGenerator.generate(getLengthValue(), getKeywordValue());
            } catch (Exception ex) {
                password = ex.getMessage();
            }
            setPasswordValue(password);
            showPassword();
        }

        void onClearButtonClicked() {
            setPasswordValue("");
            hidePassword();
        }

        void setPasswordValue(String password) {
            getPasswordView().setText(password);
        }

        TextView getPasswordView() {
            return (TextView) getView().findViewById(R.id.password);
        }

        void showPassword() {
            getPasswordLayoutView().setVisibility(View.VISIBLE);
        }

        void hidePassword() {
            getPasswordLayoutView().setVisibility(View.GONE);
        }

        RelativeLayout getPasswordLayoutView() {
            return (RelativeLayout) getView().findViewById(R.id.passwordLayout);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            passwordType = i;
            if (i == NUMBERS_ONLY_INDEX) {
                hideKeyword();
            } else {
                showKeyword();
            }
        }

        void hideKeyword() {
            setKeywordVisibility(View.GONE);
            ((TextView) getView().findViewById(R.id.keyword)).setText("");
        }

        void showKeyword() {
            setKeywordVisibility(View.VISIBLE);
        }

        void setKeywordVisibility(int visibility) {
            getView().findViewById(R.id.keywordLabel).setVisibility(visibility);
            getView().findViewById(R.id.keyword).setVisibility(visibility);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // do nothing here
        }

        PasswordGenerator newPasswordGenerator(int passwordType) {
            switch(passwordType) {
                case LETTERS_ONLY_INDEX:
                    return new LettersOnlyPasswordGenerator();
                case NUMBERS_AND_LETTERS_INDEX:
                    return new LettersAndNumberPasswordGenerator();
                case NUMBERS_ONLY_INDEX:
                    return new NumbersOnlyPasswordGenerator();
                default:
                    return new PasswordGenerator();
            }
        }

    }

}
