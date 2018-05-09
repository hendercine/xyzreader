package com.example.xyzreader.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ArticleDetailFragment";
    public static final String ARG_ITEM_ID = "ARG_ITEM_ID";

    private Unbinder unbinder;
    private long mItemId;
    private String mBody;

    @BindView(R.id.collapsing_toolbar_backdrop_img)
    ImageView mPhotoView;
    @BindView(R.id.toolbar_title_layout)
    LinearLayout mToolbarTitleLayout;
    @BindView(R.id.detail_article_title)
    TextView mTitleView;
    @BindView(R.id.article_byline)
    TextView mByLineView;
    @BindView(R.id.article_body)
    TextView mBodyView;
    @Nullable
    @BindView(R.id.share_action)
    ImageButton mShareAction;
    @Nullable
    @BindView(R.id.share_fab)
    FloatingActionButton mShareFab;
    @Nullable
    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;
    @Nullable
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Nullable
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_detail,
                container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor == null || cursor.isClosed() || !cursor.moveToFirst()) {
            return;
        }

        final String title = cursor.getString(ArticleLoader.Query.TITLE);
        final String author = Html.fromHtml(
                DateUtils.getRelativeTimeSpanString(
                        cursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_ALL).toString()
                        + " by "
                        + cursor.getString(ArticleLoader.Query.AUTHOR)).toString();
        mBody = Html.fromHtml(cursor.getString(ArticleLoader.Query.BODY)).toString();
        String photo = cursor.getString(ArticleLoader.Query.PHOTO_URL);

        if (mCollapsingToolbarLayout != null && mToolbar != null &&
                mAppBarLayout != null && mShareAction != null) {
                mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        Fade fade = new Fade();
                        if (Math.abs(verticalOffset) == mAppBarLayout.getTotalScrollRange()) {
                            // Collapsed
                            TransitionManager.beginDelayedTransition
                                    (mAppBarLayout, fade);
                            mCollapsingToolbarLayout.setTitleEnabled(true);
                            mCollapsingToolbarLayout.setTitle(title);
                            mToolbar.setSubtitle(author);
                            mTitleView.setVisibility(View.GONE);
                            mByLineView.setVisibility(View.GONE);
                                activateShareButton();
                        } else if (verticalOffset == 0) {
                            // Expanded
                            mCollapsingToolbarLayout.setTitleEnabled(false);
                            mToolbar.setSubtitle(null);
                            mTitleView.setText(title);
                            mByLineView.setText(author);
                            mTitleView.setVisibility(View.VISIBLE);
                            mByLineView.setVisibility(View.VISIBLE);
                            mShareAction.setVisibility(View.GONE);
                        } else {
                            // Mid-scroll
                            mCollapsingToolbarLayout.setTitleEnabled(true);
                            mCollapsingToolbarLayout.setTitle(title);
                            mToolbar.setSubtitle(author);
                            mTitleView.setVisibility(View.GONE);
                            mByLineView.setVisibility(View.GONE);
                            mShareAction.setVisibility(View.GONE);
                        }
                    }
                });

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Objects.requireNonNull(getActivity()).finish();
                }
            });
        }

        mBodyView.setText(mBody);

        Glide.with(this)
                .load(photo)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(mPhotoView);

        // Set FloatingActionButton to display a share dialog.
        if (mShareFab != null) {
            mShareFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(Objects.requireNonNull(getActivity()))
                            .setType("text/plain")
                            .setText(mBody)
                            .getIntent(), getString(R.string.action_share)));
                }
            });
        }
    }

    // Required method
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) { }

    // Unbind views for ButterKnife
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // Helper method to display a functional share button in the toolbar.
    private void activateShareButton() {
        if (mShareAction != null) {
            mShareAction.setVisibility(View.VISIBLE);
            mShareAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(Objects.requireNonNull(getActivity()))
                            .setType("text/plain")
                            .setText(mBody)
                            .getIntent(), getString(R.string.action_share)));
                }
            });
        }
    }
}
