package ids.androidsong.ui;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ids.androidsong.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PrimerTest {

    @Rule
    public ActivityTestRule<principal> mActivityTestRule = new ActivityTestRule<>(principal.class);

    @Test
    public void primerTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.mostrar_acordes_switch),
                        childAtPosition(
                                allOf(withId(R.id.switch_acordes),
                                        childAtPosition(
                                                withId(R.id.design_menu_item_action_area),
                                                0)),
                                0),
                        isDisplayed()));
        switch_.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        6),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.importar_button_path)));
        appCompatImageButton2.perform(click());

        ViewInteraction editText = onView(
                allOf(withText("/storage/emulated/0/AndroidSong/Songs")));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withText("/storage/emulated/0/AndroidSong/Songs")));
        editText2.perform(replaceText("/storage/emulated/0/Downloads"));

        ViewInteraction editText3 = onView(
                allOf(withText("/storage/emulated/0/Downloads")));
        editText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton.perform(click());

        pressBack();

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Open navigation drawer")));
        appCompatImageButton3.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        2),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.nueva_cancion_titulo),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView2),
                                        0),
                                3)));
        appCompatEditText.perform(scrollTo(), replaceText("UnaCancion"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.nueva_cancion_autor),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView2),
                                        0),
                                5)));
        appCompatEditText2.perform(scrollTo(), replaceText("UnCantante"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.nueva_cancion_letra)));
        appCompatEditText3.perform(scrollTo(), replaceText("'letra..........'"), closeSoftKeyboard());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.nueva_cancion_letra), withText("[C]\n'letra..........'")));
        appCompatEditText4.perform(scrollTo(), replaceText("'letra..........'[C]\n'..........'"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.nueva_cancion_letra), withText("'letra..........'[C]\n'..........'")));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.fab)));
        floatingActionButton3.perform(click());

        ViewInteraction editText4 = onView(
                allOf(withText("C")));
        editText4.perform(replaceText("V"));

        ViewInteraction editText5 = onView(
                allOf(withText("V")));
        editText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Aceptar")));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.nueva_cancion_letra), withText("'letra..........'[C]\n'..........'")));
        appCompatEditText6.perform(scrollTo(), replaceText("'letra..........'[C]\n'..........'[V]\n"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.nueva_cancion_letra), withText("'letra..........'[C]\n'..........'[V]\n")));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.cancion_pre_guardar), withText("Guardar")));
        appCompatButton4.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("Open navigation drawer")));
        appCompatImageButton4.perform(click());

        ViewInteraction navigationMenuItemView3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView3.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        pressBack();

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withContentDescription("Navigate up")));
        appCompatImageButton5.perform(click());

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withContentDescription("Open navigation drawer")));
        appCompatImageButton6.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
