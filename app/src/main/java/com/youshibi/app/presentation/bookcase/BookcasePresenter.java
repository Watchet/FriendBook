package com.youshibi.app.presentation.bookcase;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youshibi.app.AppNavigator;
import com.youshibi.app.base.BaseListContract;
import com.youshibi.app.base.BaseListPresenter;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.presentation.book.BookAdapter;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.util.DataConvertUtil;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookcasePresenter extends BaseListPresenter<BaseListContract.View, Book> {

    @Override
    public void start() {
        super.start();
        getView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppNavigator.showBookDetailActivity(view.getContext(), ((Book) adapter.getItem(position)));
            }
        });
    }

    @Override
    protected Observable<List<Book>> doLoadData(boolean isRefresh) {
        return DBManger
                .getInstance()
                .loadBookTb()
                .map(new Func1<List<BookTb>, List<Book>>() {
                    @Override
                    public List<Book> call(List<BookTb> bookTbs) {
                        Book [] books=new Book[bookTbs.size()];
                        for (int i = 0; i < bookTbs.size(); i++) {
                            books[i]= DataConvertUtil.bookTb2Book(bookTbs.get(i));
                        }
                        return Arrays.asList(books);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onLoadDataSucceed(List<Book> data, boolean isRefresh) {
        super.onLoadDataSucceed(data, isRefresh);
        getView().showTheEnd();
    }

    @Override
    public void loadMoreData() {

    }

    @Override
    protected Observable<List<Book>> doLoadMoreData() {
        return null;
    }


    @Override
    protected CommonAdapter<Book> createAdapter(List<Book> data) {
        return new BookAdapter(data);
    }
}