package ids.androidsong.testFuncionalidad;

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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.cancionesDummy;
import ids.androidsong.help.App;
import ids.androidsong.object.Cancion;
import ids.androidsong.ui.Principal;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class test_lista_canciones {
    private static  int CANTIDAD_CANCIONES_DUMMY = 2;
    private static int CANTIDAD_SECCIONES_DUMMY = 3;

    @Rule
    public ActivityTestRule<Principal> mActivityTestRule = new ActivityTestRule<>(Principal.class);

    @Before
    public void setup(){
        /*Pide canciones dummy y las inserta*/
        ArrayList<Cancion> canciones = new ArrayList<>();
        cancionesDummy cancionesDummy = new cancionesDummy(CANTIDAD_SECCIONES_DUMMY);
        canciones.addAll(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY));
        canciones.addAll(cancionesDummy.getCancionesDummy(CANTIDAD_CANCIONES_DUMMY,"Pruebas"));
        for (Cancion c : canciones){
            c.alta();
        }
    }

    @Test
    //inserta 2 canciones en la carpeta principal y dos en la carpeta pruebas
    //se espera que al cambiar en la lista desplegable de carpetas se muestre el contenido de cada carpeta
    public void test_mostrar_por_carpeta() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), childAtPosition(childAtPosition(withId(R.id.drawer_layout), 0), 2), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.cancion_lista_carpeta), childAtPosition(childAtPosition(withId(R.id.frameLayout), 0), 0), isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(withClassName(is("android.widget.PopupWindow$PopupBackgroundView")), 0)).atPosition(0);
        appCompatCheckedTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 2"), isDisplayed()));
        textView.check(matches(withText("Canción Dummy 2")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 3"), isDisplayed()));
        textView2.check(matches(withText("Canción Dummy 3")));

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.cancion_lista_carpeta), childAtPosition(childAtPosition(withId(R.id.frameLayout), 0), 0), isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(withClassName(is("android.widget.PopupWindow$PopupBackgroundView")), 0)).atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 0"), isDisplayed()));
        textView3.check(matches(withText("Canción Dummy 0")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 1"), isDisplayed()));
        textView4.check(matches(withText("Canción Dummy 1")));

    }

    @Test
    //inserta una serie de canciones y hace clic sobre una de ellas para mostrar
    //el detalle de la cancion
    public void test_mostrar_cancion() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(childAtPosition(withId(R.id.drawer_layout), 0), 2), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.cancion_detalle_secciones),
                        childAtPosition(childAtPosition(withId(R.id.cancionmusico_detail_container), 0), 0), isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(childAtPosition(withId(android.R.id.content), 0), 3), isDisplayed()));
        imageButton.check(matches(isDisplayed()));

    }

    @Test
    //inserta una cadena de busqueda en la vista de listado de canciones
    //se espera que matchee con una cancion y muestre el resultado
    public void test_buscar_cancion() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(childAtPosition(withId(R.id.drawer_layout), 0), 2), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.search), withContentDescription("Buscar"),
                        childAtPosition(childAtPosition(withId(R.id.toolbar), 2), 0), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        childAtPosition(allOf(withId(R.id.search_plate), childAtPosition(withId(R.id.search_edit_frame), 1)), 0), isDisplayed()));
        searchAutoComplete.perform(replaceText("Canción Dummy 3"), closeSoftKeyboard());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withId(R.id.search_src_text), withText("Canción Dummy 3"), isDisplayed()));
        searchAutoComplete2.perform(pressImeActionButton());

        ViewInteraction textView = onView(
                allOf(withId(R.id.cancion_lista_item_titulo), withText("Canción Dummy 3"), isDisplayed()));
        textView.check(matches(withText("Canción Dummy 3")));

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
