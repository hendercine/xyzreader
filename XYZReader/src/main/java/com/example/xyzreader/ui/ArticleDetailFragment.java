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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    @BindView(R.id.collapsing_toolbar_backdrop_img)
    ImageView mPhotoView;

//    TODO: Clear unneccessary commented code
//    @BindView(R.id.meta_bar)
//    LinearLayout metaBar;
    @BindView(R.id.detail_article_title)
    TextView mTitleView;
    @BindView(R.id.article_byline)
    TextView mByLineView;
    @BindView(R.id.article_body)
    TextView mBodyView;
//    @BindView(R.id.back_btn)
//    ImageButton mBackButton;
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
    @Nullable
    @BindView(R.id.card)
    CardView mCard;

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
        final String body = Html.fromHtml(cursor.getString(ArticleLoader.Query.BODY)).toString();
        String photo = cursor.getString(ArticleLoader.Query.PHOTO_URL);

        if (mCollapsingToolbarLayout != null && mToolbar != null &&
                mAppBarLayout != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            //                int titleMaxWidth = Objects.requireNonNull(mToolbar).getWidth();
//                int currentTitleWidth = measureTitleWidth(title);
//
//                if (currentTitleWidth > titleMaxWidth) {
//                    mToolbar.setTitleTextAppearance(getContext(), R.style.AppTheme_ExpandedTitleTextAppearance_OverFlow);
//                }
//                    Objects.requireNonNull(mCollapsingToolbarLayout).setTitle(title);
//                    title.length();
//                    mCollapsingToolbarLayout.setExpandedTitleTextAppearance(
//                            R.style.AppTheme_ExpandedTitleTextAppearance);
//                    mCollapsingToolbarLayout.setCollapsedTitleTextAppearance
//                            (R.style.TextAppearance_AppCompat_Title);
//            if (mCard == null)
                mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (Math.abs(verticalOffset) == mAppBarLayout.getTotalScrollRange()) {
                            // Collapsed
                            mCollapsingToolbarLayout.setTitleEnabled
                                    (true);
                            mCollapsingToolbarLayout.setTitle(title);
                            mToolbar.setSubtitle(author);
                            mTitleView.setVisibility(View.GONE);
                            mByLineView.setVisibility(View.GONE);
                        } else if (verticalOffset == 0) {
                            // Expanded
                            mCollapsingToolbarLayout.setTitleEnabled(false);
                            mToolbar.setSubtitle(null);
                            mTitleView.setText(title);
                            mByLineView.setText(author);
                            mTitleView.setVisibility(View.VISIBLE);
                            mByLineView.setVisibility(View.VISIBLE);
                        }
                    }
                });
//                mToolbar.setSubtitle(author);
//            } else {
//                ActionBar ab = Objects.requireNonNull(getActivity()).getActionBar();
//                if (ab != null) {
//                    ab.setCustomView(mCollapsingToolbarLayout);
//                    ab.setDisplayShowHomeEnabled(true);
//                    ab.setDisplayHomeAsUpEnabled(true);
//                    ab.setDisplayShowCustomEnabled(true);
//                    ab.setDisplayShowTitleEnabled(true);
//                    ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
//                    ab.setTitle(R.string.detail_title_id);
//                    ab.setSubtitle(R.string.detail_subtitle_id);
//                }
//                mBackButton.setVisibility(View.VISIBLE);
//            }

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Objects.requireNonNull(getActivity()).finish();
                }
            });
        }

        mBodyView.setText(body);

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
//                        Bitmap bitmap = ((BitmapDrawable) resource.getCurrent()).getBitmap();
//                        changeUIColors(bitmap);
//                        Slide slide = new Slide();
//                        slide.setSlideEdge(Gravity.END);
//
//                        TransitionManager.beginDelayedTransition(Objects.requireNonNull(mAppBarLayout), slide);
//                        mTitleView.setVisibility(View.VISIBLE);

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
                            .setText(body)
                            .getIntent(), getString(R.string.action_share)));
                }
            });
        } else if (mShareAction != null){
            mShareAction.setVisibility(View.VISIBLE);
            mShareAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(Objects.requireNonNull(getActivity()))
                            .setType("text/plain")
                            .setText(body)
                            .getIntent(), getString(R.string.action_share)));
                }
            });

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) { }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    private void changeUIColors(Bitmap bitmap) {
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(@NonNull Palette palette) {
//                int defaultColor = 0x673AB7;
//                int darkMutedColor = palette.getDarkMutedColor(defaultColor);
//                metaBar.setBackgroundColor(darkMutedColor);
//                if (mCollapsingToolbarLayout != null) {
//                    mCollapsingToolbarLayout.setContentScrimColor(darkMutedColor);
//                    mCollapsingToolbarLayout.setStatusBarScrimColor(darkMutedColor);
//                }
//            }
//        });
//    }
}
