package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * Utility class containing commonly used methods by Justin Reda
 * <p/>
 * The contained methods are general purpose, and not specific to any
 * application so they should work with any version of API.
 */
@SuppressWarnings("unused")
public class Utilities {

    static String TAG = "Utilities";

    /**
     * Write a text file to the downloads directory, file name and string data provided at use.
     *
     * @param filename String of the filename to write, Ex) "file.txt"
     * @param data     String of the physical data to put into the new file.
     * @return True if file written successfully
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean writeDownloadsSTRFile(String filename, String data) {

        Log.d(TAG, "Writing file [" + filename + "] to downloads directory");
        File[] downloadFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles();

        for (File downloadFile : downloadFiles) {
            if (downloadFile.getName().matches(filename)) {
                downloadFile.delete();
            }
        }

        File myFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filename);
        try {
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method writes data to the internal storage directory of the app given appropriate input parameters, filename and data.
     *
     * @param filename String of the filename to write, Ex) "file.txt"
     * @param value    String of the physical data to put into the new file.
     * @return True if file written successfully
     */
    public static boolean writeInternalStorageSTRFile(Context context, String filename, String value) {

        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Problem writing file [" + filename + "] to internal storage");
            return false;
        }

        Log.d(TAG, "File [" + filename + "]added = " + checkForFile(context, filename));
        return true;
    }

    /**
     * This will return a file from the internal storage directory of the app.
     *
     * @param filename String of the filename to be retrieved
     * @return a long string of the data from the retrieved file, empty string if problem.
     */
    public static String returnInternalStorageSTRFile(Context context, String filename) {

        FileInputStream fis;
        String data;
        if (checkForFile(context, filename)) {
            try {
                fis = context.openFileInput(filename);
                InputStreamReader in = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(in);
                data = br.readLine();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
        return data;
    }

    /**
     * This method will check to see if a file exists, given its full file path
     *
     * @param fileName String of the path to file and name of file to check for.
     * @return boolean, true if file exists, false otherwise
     */
    public static boolean checkForFile(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    /**
     * Delete a file stored in internal private app storage
     *
     * @param context  application context
     * @param filename string of filename to be deleted
     * @return true if successfully deleted
     */
    public static boolean deleteInternalFile(Context context, String filename) {
        Log.d(TAG, "Deleting internal file " + filename);
        File file = context.getFileStreamPath(filename);
        return file.delete();
    }

    /**
     * String value helper method
     *
     * @param string value to be inspected
     * @return true if not null, length greater then 0, and found text
     */
    public static boolean strNotNullNotEmpty(String string) {
        return string != null && string.length() > 0 && !string.matches("");
    }

    /**
     * EditText helper method
     *
     * @param et value to be inspected
     * @return true if not null, length greater then 0, and found text
     */
    public static boolean etNotNullNotEmpty(EditText et) {
        return et != null && et.getText() != null && et.getText().toString().length() > 0 && !et.getText().toString().matches("");
    }

    /**
     * Returns the screen density of the device
     * xxhdpi: 3.0
     * xhdpi: 2.0
     * hdpi: 1.5
     * mdpi: 1.0 (baseline)
     * ldpi: 0.75
     *
     * @param context application context
     * @return float value of screen density
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * Fetches and returns file contents as a string given a url
     *
     * @param url String path to remote file location
     * @return String contents of file at remote url
     */
    public static String getStringFromURL(String url) {

        String stringText = "";

        try {
            URL textUrl = new URL(url);
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textUrl.openStream()));
            String StringBuffer;
            while ((StringBuffer = bufferReader.readLine()) != null) {
                stringText += StringBuffer;
            }
            bufferReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringText;
    }

    public static int getRandomIntInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static double getRandomDoubleInRange(double min, double max) {
        Random random = new Random();
        return (min + (max - min) * random.nextDouble());
    }

    public static double truncateDouble(double inputNumber, int places) {
        String pattern = "#.";
        for (int i = 0; i < places; i++) {
            pattern += "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(pattern);
        return Double.valueOf(twoDForm.format(inputNumber));
    }

    public static float getRandomFloatInRange(float min, float max) {
        Random random = new Random();
        return (min + (max - min) * random.nextFloat());
    }

    public static void hideKeyboard(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getStringFromAsset(Context context, String assetPathName) {
        StringBuilder buf = new StringBuilder();
        InputStream inputStream;
        if (context != null) {
            try {
                //Log.wtf(TAG, Arrays.toString(context.getResources().getAssets().list("")));
                inputStream = context.getAssets().open(assetPathName);

                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String str;

                while ((str = in.readLine()) != null) {
                    buf.append(str);
                }

                in.close();
                //Log.wtf(TAG, buf.toString());

                return buf.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This will read a file from the downloads directory and return the content of file as a string.
     *
     * @param filename file which you want to be retrieved. A string is expected. Ex) "file.txt"
     * @return the contents of the file as a String
     */
    private String readDownloadSTRFile(String filename) {
        File[] downloadFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles();
        File goodFile = null;

        for (File downloadFile : downloadFiles) {
            if (downloadFile.getName().matches(filename)) {
                goodFile = downloadFile;
            }
        }
        if (goodFile == null) {
            return "";
        }

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(goodFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

}
