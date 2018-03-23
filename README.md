# XYZ Reader by Hendercine

A mock RSS feed reader featuring banner photos and headlines and project #5 of Udacity's Android
Developer NanoDegree Program. The app is currently functional, and works, in most cases, for most
users. The goal of this version of this app is to implement changes, based on
user feedback, that will improve the UI and make it conform to Material Design.

# Initial State of XYZ Reader

As stated above, the app came pre-built and is technically functional but has some serious design
issues. The initial writing of this README will function as the upgrade plan and then will be updated
to describe the final version of the app.

# App Improvement Plan

## UI Review

### User Feedback for XYZ Reader:

1. Lyla says:
“This app is starting to shape up but it feels a bit off in quite a few places. I can't put finger
on it but it feels odd.”

2. Jay says:
“Is the text supposed to be so wonky and unreadable? It is not accessible to those of us without
perfect vision."

3. Kagure says:
“The color scheme is really sad and I shouldn't feel sad.”

## Necessary Improvements List
1. Per Lyla's comments, get the App Theme straightened up:
    1. Extend the theme from AppCompat
    2. Implement:
        1. AppBarLayout
        2. SnackBar
        3. FloatingActionButton
        4. ConstraintLayout
        5. CoordinatorLayout for the main activity.

2. Per Jay's comments, set the theme's font to Roboto and make sure that it is formatted
    declaratively. Update the TextViews with alt-text so as to be compatible with Android's
    accessibility features.

3. Per Kagure's comments, choose and implement a a consistent color theme defined in styles.xml
    that does not impact the usability of the app.

4. Properly specify elevations for app bars, FABs, and other elements specified in the
    Material Design specification.

5. Use images that are high quality, specific, and full bleed.
