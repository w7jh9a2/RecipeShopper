package com.example.nam.recipeshopper;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.SerializationException;
import org.jsoup.UncheckedIOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

/**
 * Created by Nam on 3/22/2018.
 */

public class GetRecipeHtmlData extends AsyncTask<String, Void, RecipeEntry> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetRecipeHtmlData";

    private RecipeEntry mRecipeEntry = null;
    private String mRecipeURL;

    private final OnDataAvailable mCallBack;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable {
        void onDataAvailable(RecipeEntry newEntry, DownloadStatus status);
    }

    public GetRecipeHtmlData(OnDataAvailable callback) {
        Log.d(TAG, "GetRecipeHtmlData: called");
        mCallBack = callback;
    }

    @Override
    protected void onPostExecute(RecipeEntry recipeEntry) {
        Log.d(TAG, "onPostExecute: starts");

        if(mCallBack != null) {
            mCallBack.onDataAvailable(mRecipeEntry, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected RecipeEntry doInBackground(String... recipeUrl) {
        Log.d(TAG, "doInBackground: starts");
        mRecipeURL = recipeUrl[0];
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(mRecipeURL);
        Log.d(TAG, "doInBackground: ends");
        return mRecipeEntry;
    }

    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts. Status = " + status);

        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: parsing begins");
            // Parsing HTML tried here
            try {
                Document recipeHTML = Jsoup.parse(data);
                Elements recipeProcedure = recipeHTML.getElementsByClass("recipe-procedure-text");
                Elements recipeIngredients = recipeHTML.getElementsByAttributeValue("itemprop", "recipeIngredient");

                mRecipeEntry = new RecipeEntry();

                // TODO: Validation & optimization

                mRecipeEntry.setTitle(recipeHTML.getElementsByAttributeValue("itemprop", "name").text());
                mRecipeEntry.setYield(recipeHTML.getElementsByAttributeValue("itemprop", "recipeYield").text());
                if(!recipeHTML.getElementsContainingOwnText("Total Time").isEmpty()) {
                    mRecipeEntry.setTotalTime(recipeHTML.getElementsContainingOwnText("Total Time").first().nextElementSibling().ownText());
                }
                if(!recipeHTML.getElementsContainingOwnText("Active Time").isEmpty()) {
                    mRecipeEntry.setActiveTime(recipeHTML.getElementsContainingOwnText("Active Time").first().nextElementSibling().ownText());
                }
                mRecipeEntry.setImageURL(recipeHTML.getElementsByAttributeValue("itemprop", "image").attr("src"));
                Log.d(TAG, "Title: " + mRecipeEntry.getTitle() + " Yield: " + mRecipeEntry.getYield() + " Total Time: " + mRecipeEntry.getTotalTime() +
                        " Active Time: " + mRecipeEntry.getActiveTime() + "\nImage:" + mRecipeEntry.getImageURL() );

                for(Element ingredient : recipeIngredients) {
                    Log.d(TAG, "Element contains: " + ingredient.ownText() + "\n");
                    mRecipeEntry.addIngredients(new Ingredient(ingredient.ownText(), mRecipeEntry));
                }

                for(Element step : recipeProcedure) {
                    // TODO: Validation
                    Log.d(TAG, "Recipe-procedure-text contains: " + step.text() + "\n");
                    mRecipeEntry.addInstructions(step.text());
                }

//                for(int i = 0; i < itempropsAttributes.size(); i++) {
//                    String attributeValue = itempropsAttributes.get(i);
//                    switch(attributeValue) {
//                        case "name":
//                            mRecipeEntry.setTitle(itempropsContents.get(i));
//                            break;
//                        default:
//                            break;
//                    }

//                }


            } catch(Selector.SelectorParseException e) {
                Log.e(TAG, "onDownloadComplete: " + e.toString());
            } catch(UncheckedIOException e) {
                Log.e(TAG, "onDownloadComplete: " + e.toString());
            } catch(SerializationException e) {
                Log.e(TAG, "onDownloadComplete: " + e.toString());
            }

        }
    }
}
