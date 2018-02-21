package ids.androidsong.ui;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
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
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.cancionesDummy;
import ids.androidsong.help.App;
import ids.androidsong.object.cancion;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class cp2 {

    private static  int CANTIDAD_CANCIONES_DUMMY = 2;
    private static int CANTIDAD_SECCIONES_DUMMY = 3;

    @Rule
    public ActivityTestRule<principal> mActivityTestRule = new ActivityTestRule<>(principal.class);

    @Before
    public void setup(){
        /*Pide canciones dummy y las inserta*/
        ArrayList<cancion> canciones = new ArrayList<>();
        cancionesDummy cancionesDummy = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY);
        canciones.addAll(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY));
        canciones.addAll(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY,"Pruebas"));
        for (cancion c : canciones){
            c.alta();
        }
    }

    @Test
    public void cp2_1() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.cancion_lista_carpeta),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frameLayout),
                                        0),
                                0),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        appCompatCheckedTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 2"),
                        isDisplayed()));
        textView.check(matches(withText("Canción Dummy 2")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 3"),
                        isDisplayed()));
        textView2.check(matches(withText("Canción Dummy 3")));

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.cancion_lista_carpeta),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frameLayout),
                                        0),
                                0),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 0"),
                        isDisplayed()));
        textView3.check(matches(withText("Canción Dummy 0")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 1"),
                        isDisplayed()));
        textView4.check(matches(withText("Canción Dummy 1")));

    }

    @Test
    public void cp2_2() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.search), withContentDescription("Buscar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(R.id.search_plate),
                                        childAtPosition(
                                                withId(R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("Canción Dummy 3"), closeSoftKeyboard());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withId(R.id.search_src_text), withText("Canción Dummy 3"),
                        isDisplayed()));
        searchAutoComplete2.perform(pressImeActionButton());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 3"),
                        isDisplayed()));
        textView.check(matches(withText("Canción Dummy 3")));

    }

    @Test
    public void cp2_3() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.cancion_detalle_secciones),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cancionmusico_detail_container),
                                        0),
                                0),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

    }

    @Test
    public void cp2_4() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(3, longClick()));

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("Abrir"),
                        childAtPosition(
                                allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(
                                                        android.widget.FrameLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(android.R.id.text1), withText("Añadir a Favoritos"),
                        childAtPosition(
                                allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(
                                                        android.widget.FrameLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(android.R.id.text1), withText("Copiar a carpeta"),
                        childAtPosition(
                                allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(
                                                        android.widget.FrameLayout.class),
                                                0)),
                                2),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(android.R.id.text1), withText("Mover a carpeta"),
                        childAtPosition(
                                allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(
                                                        android.widget.FrameLayout.class),
                                                0)),
                                3),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(android.R.id.text1), withText("Renombrar canción"),
                        childAtPosition(
                                allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(
                                                        android.widget.FrameLayout.class),
                                                0)),
                                4),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(android.R.id.text1), withText("Eliminar canción"),
                        childAtPosition(
                                allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(
                                                        android.widget.FrameLayout.class),
                                                0)),
                                5),
                        isDisplayed()));
        textView7.check(matches(isDisplayed()));

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
