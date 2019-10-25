package erquar.com.countdown.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import erquar.com.countdown.R;
import erquar.com.countdown.adapters.WeekendAdapter;
import erquar.com.countdown.configuration.GlobalVars;
import erquar.com.countdown.configuration.NationCalender;
import erquar.com.countdown.configuration.Utils;
import erquar.com.countdown.views.clockWidget.ClockViewSurface;

public class MainActivity extends Activity implements View.OnClickListener{

    // view
    TextView tvCurDate;
    TextView tvResultDate;
    TextView tvEmpty;
    ClockViewSurface mClockViewSurface;
    RecyclerView recyclerView;

    // value
    WeekendAdapter recyclerAdaper;
    Boolean festFlag = false;
    int inputTypeFlag = 2;
    List<String> resultData;
    int[] inputDelta = new int[]{30, 7, 1};
    String[] inputTypeString = new String[] {"M", "W", "D"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getParamsFromIntent();
        getViewsByID();
        initializeData();
        setOnListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.help:                                 // click help
                showHelp();
                return true;
            case R.id.sendToFriend:                         // send eMail to a friend
                showEmailDlg();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getParamsFromIntent () {

    }

    public void getViewsByID () {
        tvCurDate = (TextView)findViewById(R.id.curDate);
        tvResultDate = (TextView)findViewById(R.id.tvResultDate);
        tvEmpty = (TextView)findViewById(R.id.tv_empty);
        mClockViewSurface = (ClockViewSurface) findViewById(R.id.clockView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    public void initializeData () {
        // check short cut icon
        if(!getSharedPreferences(Utils.APP_PREFERENCE, Activity.MODE_PRIVATE).getBoolean(Utils.IS_ICON_CREATED, false)){
            addShortcut();
            getSharedPreferences(Utils.APP_PREFERENCE, Activity.MODE_PRIVATE).edit().putBoolean(Utils.IS_ICON_CREATED, true).commit();
        }
        // init values
        resultData = new ArrayList<String>();

        // init actionbar
        ActionBar mActionBar = getActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(0xff606060));
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        // init view
        mClockViewSurface.setClockFormat(12);
        tvCurDate.setText(NationCalender.getCurrentDate());
        tvResultDate.setText(GlobalVars.getDateValue(this));

        // set up adapters
        recyclerAdaper = new WeekendAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdaper);

        festFlag = GlobalVars.getFestValue(this);
        inputTypeFlag = GlobalVars.getInputType(this);
        String startDate = GlobalVars.getStartDateValue(this);
        int incDays = GlobalVars.getDaysValue(this);
        int passVal = GlobalVars.getPassDaysValue(this);
        int repeatVal = GlobalVars.getRepeatValue(this);
        calculateDate(NationCalender.convertStringToDate(startDate), incDays, passVal, repeatVal);
    }


    public void calculateDate(Date startDate, int days, int passDays, int repeatDays) {

        if(days - passDays < 0) {                     // already passed
            GlobalVars.showAlert(this, "Err", "Date produced was passed. Please retry input day.");
            return;
        }

        resultData = new ArrayList<String>();
        for(int i = 0; i < repeatDays; i ++) {
            int incDays = days;
            if(i == 0)
                incDays = days - passDays;
            else
                incDays = days;
            if(festFlag)
                incDays = NationCalender.getWorkingDaysBetweenTwoDates(getStartDate(), incDays);

            Calendar c = Calendar.getInstance(); // Get Calendar Instance
            c.setTime(startDate);

            c.add(Calendar.DATE, incDays);  // add n days
            Date resultdate = new Date(c.getTimeInMillis());   // Get new time
            String dateInString = NationCalender.getDateFormat().format(resultdate);
            startDate = resultdate;
            resultData.add(dateInString);
        }
        // save date
        GlobalVars.putStartDateValue(this, tvCurDate.getText().toString());
        GlobalVars.putFesteValue(this, festFlag);
        GlobalVars.putInputType(this, inputTypeFlag);
        GlobalVars.putDaysValue(this, days);
        GlobalVars.putRepeatValue(this, repeatDays);
        GlobalVars.putPassDaysValue(this, passDays);

        // update holoday recyclerview
        if(resultData.size() == 0)
            tvEmpty.setVisibility(View.VISIBLE);
        else
            tvEmpty.setVisibility(View.GONE);
        recyclerAdaper.notifyData(resultData);
    }

    // get default valus
    public void defaultValesDisplay () {
        resultData = new ArrayList<String>();
        int days = 7;
        int passDays = 0;
        Date startDate = NationCalender.convertStringToDate(NationCalender.getCurrentDate());
        for(int i = 0; i < 5; i ++) {
            int incDays = days;
            if(i == 0)
                incDays = days - passDays;
            else
                incDays = days;

            Calendar c = Calendar.getInstance(); // Get Calendar Instance
            c.setTime(startDate);

            c.add(Calendar.DATE, incDays);  // add n days
            Date resultdate = new Date(c.getTimeInMillis());   // Get new time
            String dateInString = NationCalender.getDateFormat().format(resultdate);
            startDate = resultdate;
            resultData.add(dateInString);
        }

        // update holoday recyclerview
        recyclerAdaper.notifyData(resultData);
    }
    public Date getStartDate () {
        String startDate = GlobalVars.getStartDateValue(this);
        Calendar c = Calendar.getInstance();
        DateFormat format = DateFormat.getDateInstance();
        try {
            c.setTime(format.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateStart = new Date(c.getTimeInMillis());

        return dateStart;
    }
    public void setOnListener ()  {
        findViewById(R.id.btnProduce).setOnClickListener(this);
        findViewById(R.id.btnReset).setOnClickListener(this);
        findViewById(R.id.clockView).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnProduce:                    // click Produce Button
                showInputDayDlg();
                break;
            case R.id.btnReset:                     // click Reset Button
                resetProduce();
                break;
            case R.id.clockView:                    // click clock
//                GlobalVars.getFestivalDays(this, "a", "a");
//                new ApiAsyncTask(this).execute();

                resetProduce();
                showInputDayDlg();
                break;
        }
    }

    // reset produce
    public void resetProduce () {
        tvResultDate.setText("");
        festFlag = false;
    }

    // show alert dialog for input days
    public void showInputDayDlg () {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.layout_prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final ViewGroup passedViewGroup = (ViewGroup) promptsView.findViewById(R.id.layout_passed);
        final Button btnInputType = (Button)promptsView.findViewById(R.id.btnInputType);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        final EditText passedInput = (EditText) promptsView.findViewById(R.id.editPassed);
        final EditText repeatInput = (EditText) promptsView.findViewById(R.id.editRepeat);
        final CheckBox checkBox = (CheckBox) promptsView.findViewById(R.id.checkbox);
        final CheckBox checkBoxFes = (CheckBox) promptsView.findViewById(R.id.checkboxFestival);
        final CheckBox checkBoxRepeat = (CheckBox) promptsView.findViewById(R.id.repeatCheck);

        // init view
        checkBox.setEnabled(false);
        checkBoxFes.setEnabled(false);
        checkBoxRepeat.setEnabled(false);
        btnInputType.setText(inputTypeString[GlobalVars.getInputType(this)]);

        // set past values from preferrence
        int days_val = GlobalVars.getDaysValue(this);
        int pass_val = GlobalVars.getPassDaysValue(this);
        int repeat_val = GlobalVars.getRepeatValue(this);
        Boolean festival_flag = GlobalVars.getFestValue(this);
        final int inputType_val = GlobalVars.getInputType(this);

        days_val = days_val / inputDelta[inputType_val];
        if(days_val > 0) {
            userInput.setText(String.valueOf(days_val));
            checkBox.setEnabled(true);
            checkBoxFes.setEnabled(true);
            checkBoxRepeat.setEnabled(true);
        }
        if(pass_val > 0) {
            checkBox.setChecked(true);
            passedViewGroup.setVisibility(View.VISIBLE);
            passedInput.setText(String.valueOf(pass_val));
        }
        if(repeat_val > 0) {
            checkBoxRepeat.setChecked(true);
            repeatInput.setVisibility(View.VISIBLE);
            repeatInput.setText(String.valueOf(repeat_val));
        }
        checkBoxFes.setChecked(festival_flag);

        // set cursor at end of the text
        userInput.setSelection(userInput.getText().length());
        passedInput.setSelection(passedInput.getText().length());
        repeatInput.setSelection(repeatInput.getText().length());

        // set listener on checkbox
        passedInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = passedInput.getText().toString();
                int d = 0;
                if(!s.toString().equals(""))
                    d = Integer.parseInt(s.toString());
                if(input.equals("")) {

                } else if(d == 0 && input.equals("0")) {
                        passedInput.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        repeatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = repeatInput.getText().toString();
                int d = 0;
                if(!s.toString().equals(""))
                    d = Integer.parseInt(s.toString());
                if(input.equals("")) {

                } else if(d == 0 && input.equals("0")) {
                    repeatInput.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String input = userInput.getText().toString();
                Log.e("charsequence", s.toString());
                int d = 0;
                if(s.toString().equals(""))
                    d = 0;
                else
                    d = Integer.parseInt(s.toString());
                if(input.equals("") && d == 0) {
                    checkBox.setChecked(false);
                    checkBoxFes.setChecked(false);
                    checkBoxRepeat.setChecked(false);
                    passedInput.setText("");
                    repeatInput.setText("");
                    passedViewGroup.setVisibility(View.GONE);
                    repeatInput.setVisibility(View.GONE);
                    checkBox.setEnabled(false);
                    checkBoxFes.setEnabled(false);
                    checkBoxRepeat.setEnabled(false);
                } else {
                    checkBox.setEnabled(true);
                    checkBoxFes.setEnabled(true);
                    checkBoxRepeat.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = userInput.getText().toString();
                int d = 0;
                if(!s.toString().equals(""))
                    d = Integer.parseInt(s.toString());
                if(input.equals("")) {
                    checkBox.setChecked(false);
                    checkBoxFes.setChecked(false);
                    checkBoxRepeat.setChecked(false);
                    passedInput.setText("");
                    repeatInput.setText("");
                    passedViewGroup.setVisibility(View.GONE);
                    repeatInput.setVisibility(View.GONE);
                    checkBox.setEnabled(false);
                    checkBoxFes.setEnabled(false);
                    checkBoxRepeat.setEnabled(false);
                } else {
                    if(d == 0 && input.equals("0")) {
                        userInput.setText("");
                        checkBox.setChecked(false);
                        checkBoxFes.setChecked(false);
                        checkBoxRepeat.setChecked(false);
                        passedInput.setText("");
                        repeatInput.setText("");
                        passedViewGroup.setVisibility(View.GONE);
                        repeatInput.setVisibility(View.GONE);
                        checkBox.setEnabled(false);
                        checkBoxFes.setEnabled(false);
                        checkBoxRepeat.setEnabled(false);

                    } else {
                        checkBox.setEnabled(true);
                        checkBoxFes.setEnabled(true);
                        checkBoxRepeat.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {                     // checked
                    passedViewGroup.setVisibility(View.VISIBLE);
                    passedInput.setFocusableInTouchMode(true);
                    passedInput.requestFocus();
                    showKeyborad();
                } else {                            // unchecked
                    passedViewGroup.setVisibility(View.GONE);
                    passedInput.setText("");
                }
            }
        });

        checkBoxRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    repeatInput.setVisibility(View.VISIBLE);
                    repeatInput.setFocusableInTouchMode(true);
                    repeatInput.requestFocus();
                    showKeyborad();
                } else {
                    repeatInput.setVisibility(View.GONE);
                    repeatInput.setText("");
                }
            }
        });
        checkBoxFes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                festFlag = isChecked;
            }
        });
        promptsView.findViewById(R.id.btnDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        int val = NationCalender.getDaysBetweenDates(NationCalender.convertStringToDate(NationCalender.getCurrentDate()), newDate.getTime());
                        passedInput.setText(String.valueOf(val));
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
        promptsView.findViewById(R.id.btnInputType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, (Button)promptsView.findViewById(R.id.btnInputType));
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_input_type, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.input_month:
                                if(inputTypeFlag == 0)
                                    break;
                                inputTypeFlag = 0;
                                break;
                            case R.id.input_week:
                                if(inputTypeFlag == 1)
                                    break;
                                inputTypeFlag = 1;
                                break;
                            case R.id.input_day:
                                if(inputTypeFlag == 2)
                                    break;
                                inputTypeFlag = 2;
                                break;
                        }
                        btnInputType.setText(inputTypeString[inputTypeFlag]);
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Result",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                clearData();
                                Integer days = 0;
                                Integer passVal = 0;
                                Integer repeatVal = 1;
                                String inputDays = userInput.getText().toString();
                                String passedDays = passedInput.getText().toString();
                                String repeatStr = repeatInput.getText().toString();
                                if(inputDays.equals("")) {
                                    GlobalVars.resetPreferenceData(MainActivity.this);
                                    return;
                                }

                                if(repeatStr.equals(""))
                                    repeatVal = 1;
                                else
                                    repeatVal = Integer.valueOf(repeatStr);
                                days = Integer.valueOf(inputDays);
                                days = days * inputDelta[inputTypeFlag];
                                if(!passedDays.equals(""))
                                    passVal = Integer.valueOf(passedDays);
                                String startDate = NationCalender.getCurrentDate();
                                calculateDate(NationCalender.convertStringToDate(startDate), days, passVal, repeatVal);
                                hideKeyboard(userInput);
                            }
                        })
                .setNegativeButton("Reset",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                hideKeyboard(userInput);
                                userInput.setText("");
//                                dialog.cancel();
                            }
                        });
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // show it
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideKeyboard(userInput);
                userInput.setText("");
            }
        });

//        // back button action on Alert Dialog
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                // TODO Auto-generated method stub
                initValues();
                dialog.dismiss();

            }
        });
    }

    public void clearData() {
        tvEmpty.setVisibility(View.VISIBLE);
        resultData = new ArrayList<String>();
        recyclerAdaper.notifyData(resultData);
    }

    // init all vaules
    public void initValues () {
        GlobalVars.putStartDateValue(this, "");
        GlobalVars.putFesteValue(this, festFlag);
        GlobalVars.putInputType(this, inputTypeFlag);
        GlobalVars.putDaysValue(this, 0);
        GlobalVars.putRepeatValue(this, 0);
        GlobalVars.putPassDaysValue(this, 0);
        clearData();
    }

    // show email dialog
    public void showEmailDlg () {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.layout_send_email, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText senderAddr = (EditText) promptsView
                .findViewById(R.id.senderAddr);
        final EditText senderPW = (EditText) promptsView.findViewById(R.id.senderPassword);
        final EditText recipient = (EditText) promptsView.findViewById(R.id.recipientAddr);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String strSender = senderAddr.getText().toString();
                                String strSenderPW = senderPW.getText().toString();
                                String strRecipient = recipient.getText().toString();
                                if(strRecipient.equals("")) {
                                    GlobalVars.showAlert(MainActivity.this, "EMail Err", "Invalid Contact. Please try again later.");
                                    return;
                                }
                                GlobalVars.sendeMail(MainActivity.this, "New App link", getString(R.string.help_string), strSender, strRecipient
                                                        , strSenderPW);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // show it
        alertDialog.show();
    }

    // show help view
    public void showHelp () {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.layout_about, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final TextView tvHelp = (TextView) promptsView.findViewById(R.id.tvHelp);
        tvHelp.setClickable(true);
        tvHelp.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<html><body>It is an app that produces a countdown of days. " +
                "Please contact <a href='andreatami@gmail.com'> andreatami@gmail.com </a> for more information.<br/>" +
                "<br/> iPhone version <a href='http://www.google.com'> http://www.google.com </a></body></html>" +
                "<br/> Android version <a href='http://www.google.com'> http://www.google.com </a></body></html>";
        tvHelp.setText(Html.fromHtml(text));
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // show it
        alertDialog.show();
    }


    // app icon shortcut on home screen
    private void addShortcut() {
        Intent shortcut = new Intent(this, SplashActivity.class);
        shortcut.setAction(Intent.ACTION_MAIN);
        shortcut.putExtra("duplicate", false);

        Intent add = new Intent();
        add.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcut);
        add.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        add.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.calendar));
        add.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        sendBroadcast(add);
    }


    // show keyboard
    private void showKeyborad () {
        if(imm == null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

    }

    // hide keyboard
    private void hideKeyboard (EditText FOCUSABLE_VIEW) {
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                FOCUSABLE_VIEW.getWindowToken(), 0);
        imm = null;
    }

    @Override
    public void onBackPressed() {
        hideKeyboard(new EditText(this));
        super.onBackPressed();
    }

    InputMethodManager imm = null;

}
