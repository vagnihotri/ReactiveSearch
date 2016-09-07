package com.rxjava.frpsearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.rxjava.frpsearch.model.Issues;
import com.rxjava.frpsearch.network.NetworkService;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    IssueAdapter issueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Github Repo Issues Search");
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.issue_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        issueAdapter = new IssueAdapter();
        mRecyclerView.setAdapter(issueAdapter);

        SearchView searchView = (SearchView) findViewById(R.id.repo_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    searchForQuery(query);
                    hideKeyboard();
                } else {
                    clearListAndMakeToast("Please enter the repo url in user/repo format");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    issueAdapter.clear();
                }
                return true;
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void searchForQuery(String query) {
        if(!query.contains("/")) {
            clearListAndMakeToast("Please enter the repo url in user/repo format");
            return;
        }
        String items[] = query.split("/");
        String user, repo;
        if(items.length == 2) {
            user = items[0];
            repo = items[1];
            if(user.isEmpty()) {
                clearListAndMakeToast("Please enter the repo url in user/repo format");
                return;
            }
        } else {
            clearListAndMakeToast("Please enter the repo url in user/repo format");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        NetworkService.API.getIssues(user, repo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Issues>>() {
                    @Override
                    public final void onCompleted() {
                        progressDialog.dismiss();
                        hideKeyboard();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        progressDialog.dismiss();
                        clearListAndMakeToast("Unable to fetch issues");
                    }

                    @Override
                    public final void onNext(List<Issues> response) {
                        issueAdapter.addItems(response);
                    }
                });
    }

    private void clearListAndMakeToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
        issueAdapter.clear();
    }
}
