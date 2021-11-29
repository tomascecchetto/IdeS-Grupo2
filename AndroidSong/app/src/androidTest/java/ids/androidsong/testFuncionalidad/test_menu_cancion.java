package ids.androidsong.testFuncionalidad;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ids.androidsong.R;
import ids.androidsong.cancionesDummy;
import ids.androidsong.object.Cancion;
import ids.androidsong.ui.Principal;

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
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class test_menu_cancion {
    private static int CANTIDAD_CANCIONES_DUMMY = 1;
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
    //muestra una cancion
    //hace click sobre el boton pantalla completa
    //se espera que se muestre la cancion en el modo pantalla completa
    public void test_cancion_fullscreen() {
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.cancion_fullscreen_secciones),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));
    }

    @Test
    //este no funciona
    public void test_cancion_compartir(){
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.cancion_detalle_share),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                10),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction listView = onView(
                allOf(IsInstanceOf.<View>instanceOf(android.widget.ListView.class),
                        withParent(allOf(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        listView.check(matches(isDisplayed()));
    }

    @Test
    //inserta una cancion y hace clic sobre el icono informacion de la misma
    //se espera que muestre todos los atributos de la cancion
    public void test_cancion_atributos() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), childAtPosition(childAtPosition(withId(R.id.drawer_layout), 0), 2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista), childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(childAtPosition(withId(android.R.id.content), 0), 3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.cancion_detalle_info),
                        childAtPosition(childAtPosition(withId(android.R.id.content), 0), 9),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.atributo_nombre), withText("autor"),
                        childAtPosition(childAtPosition(withId(R.id.lista_atributos), 3), 0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.atributo_nombre), withText("himno_numero"),
                        childAtPosition(childAtPosition(withId(R.id.lista_atributos), 4), 0),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.atributo_nombre), withText("interprete"),
                        childAtPosition(childAtPosition(withId(R.id.lista_atributos), 5), 0),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.atributo_nombre), withText("presentacion"),
                        childAtPosition(childAtPosition(withId(R.id.lista_atributos), 6), 0),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.atributo_nombre), withText("tono"),
                        childAtPosition(childAtPosition(withId(R.id.lista_atributos), 8), 0),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.atributo_nombre), withText("tono"),
                        childAtPosition(childAtPosition(withId(R.id.lista_atributos), 8), 0),
                        isDisplayed()));
        textView6.check(matches(isDisplayed()));

    }

    @Test
    //inserta una cancion y hacle clic sobre el icono editar de la misma
    //se espera que muestre la pantalla para editar la cancion
    public void test_cancion_editar() {
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
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.cancion_detalle_editar),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.editar_cancion_letra)));
        editText.check(matches(isDisplayed()));
    }

    @Test//no funca
    //inserta una cancion y hace click sobre el menu subir semitono
    //se espera que la cancion pase de C a C#
    public void test_cancion_subrir_tono(){
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.cancion_lista),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.cancion_detalle_opciones), withContentDescription("Icon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.cancion_detalle_sharp),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.seccion_contenido), withText("C#    G      Ab"),
                        withParent(withParent(withId(R.id.cancion_detalle_secciones))),
                        isDisplayed()));
        textView.check(matches(withText("C#    G      Ab ")));


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

