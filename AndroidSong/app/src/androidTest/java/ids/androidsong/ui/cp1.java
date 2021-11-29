package ids.androidsong.ui;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.support.test.espresso.DataInteraction;
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
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ids.androidsong.R;
import ids.androidsong.help.App;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class cp1 { //ya esta - eliminar

    @Rule
    public ActivityTestRule<Principal> mActivityTestRule = new ActivityTestRule<>(Principal.class);

    //crea una cancion
    //el test falla porque abre otro programa. no matchea con la ventana del programa
    @Test
    public void cp1_1() {
        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Open navigation drawer")));
        appCompatImageButton3.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.nueva_cancion_carpeta)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.nueva_cancion_titulo),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView2),
                                        0),
                                3)));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.nueva_cancion_titulo),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView2),
                                        0),
                                3)));
        appCompatEditText2.perform(scrollTo(), replaceText("Cancion1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.nueva_cancion_autor),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView2),
                                        0),
                                5)));
        appCompatEditText3.perform(scrollTo(), replaceText("Autor1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.nueva_cancion_tono),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView2),
                                        0),
                                11)));
        appCompatEditText4.perform(scrollTo(), replaceText("A"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.nueva_cancion_transporte),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView2),
                                        0),
                                13)));
        appCompatEditText5.perform(scrollTo(), replaceText("2"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.cancion_pre_guardar), withText("Guardar"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(
                                                is("android.support.design.widget.CoordinatorLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Cancion1")));
        textView.check(matches(withText("Cancion1")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.cancion_lista_item_carpeta), withText("Principal")));
        textView2.check(matches(withText("Principal")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.cancion_detalle_secciones),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cancionmusico_detail_container),
                                        0),
                                0),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));

    }

    @After
    public void destroy(){
        try {
            App.GetDBHelper().clearDb();
            App.CloseDB();
        } catch (Exception e) {
            System.out.print("Error restaurando BD\n");
        }
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
